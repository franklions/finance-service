package com.franklions.finance.service.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.franklions.finance.utils.Utils.strToDecimal;

/**
 * 新浪财经股票信息爬取
 * @author flsh
 * @version 1.0
 * @date 2019-09-20
 * @since Jdk 1.8
 */
public class SinaFinanceStockPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SinaFinanceStockPageProcessor.class);

    @Override
    public void process(Page page) {
        try {
            //检查是否已退市 未上市 停牌股
            try {
                String closed = page.getHtml().xpath("//*[@id=\"closed\"]").get();
                if(closed.trim().equals("已退市") ||closed.trim().equals("未上市")){
                    page.putField("closed", "true");
                    page.putField("stockCode", page.getHtml().xpath("//*[@id=\"stockName\"]/span/text(0)").get().trim().substring(1, 7));
                    return ;
                }

                String closedStyle = page.getHtml().xpath("//*[@id=\"trading\"]/@style").toString();
                if(closedStyle.trim().equals("display: none;")){
                    page.setSkip(true);
                    return;
                }

            }catch (Exception  ex){
                logger.error("获取退市信息异常.",ex);
            }

            //取出内容
            FinanceStockDay dayInfo = new FinanceStockDay();
            Selectable stock = page.getHtml().xpath("//*[@id=\"stockName\"]");
            String stockCode = stock.xpath("//span/text(0)").get().trim().substring(1, 7);
            String stockName = stock.xpath("//i/text(0)").get().trim();
            dayInfo.setStockCode(stockCode);
            dayInfo.setStockName(stockName);
            dayInfo.setCompany(page.getHtml().xpath("/html/body/div[7]/div[4]/div[12]/p[2]/text(0)").get().trim());
            List<Selectable> blocks = page.getHtml().xpath("/html/body/div[7]/div[4]/div[12]/p[15]/a").nodes();

            String blockStr = blocks.stream().map(b -> b.xpath("//a/text(0)").get().trim()).collect(Collectors.joining(","));
            dayInfo.setBlock(blockStr);
            dayInfo.setStockDate(page.getHtml().xpath("//*[@id=\"hqTime\"]/text(0)").get().trim().substring(0, 10));

            //机构评级
            try {
                Selectable grade = page.getHtml().xpath("/html/body/div[7]/div[3]/div[8]/div[2]/div[3]/div[2]/div/div/@style");
                if (grade != null && grade.get() != null && grade.get().length() > 0) {
                    Integer igrade = 0;
                    try {
                        igrade = Integer.valueOf(grade.toString().replaceAll("left:", "").replaceAll("px;", ""));
                        igrade = Double.valueOf(((igrade + 5) / 58.0) - 0.5 + 1).intValue();
                    } catch (NumberFormatException e) {
                        igrade = 0;
                    }
                    dayInfo.setStockGrade(igrade);
                }
            }catch (NullPointerException e){
                dayInfo.setStockGrade(0);
            }
            //股票价格信息
            dayInfo.setCurrent(strToDecimal(page.getHtml().xpath("//*[@id=\"price\"]/text(0)").get().trim()));
            dayInfo.setChange(strToDecimal(page.getHtml().xpath("//*[@id=\"change\"]/text(0)").get().trim()));

            List<Selectable> hq_details = page.getHtml().xpath("//*[@id=\"hqDetails\"]/table/tbody/tr").nodes();
            dayInfo.setOpen(strToDecimal(hq_details.get(0).xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setVolume(strToDecimal(hq_details.get(0).xpath("//tr/td[2]/text(0)").get().trim()));
            dayInfo.setSwing(strToDecimal(hq_details.get(0).xpath("//tr/td[3]/text(0)").get().trim()));

            dayInfo.setHigh(strToDecimal(hq_details.get(1).xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setAmount(strToDecimal(hq_details.get(1).xpath("//tr/td[2]/text(0)").get().trim()));
            dayInfo.setTurnover(strToDecimal(hq_details.get(1).xpath("//tr/td[3]/text(0)").get().trim()));

            dayInfo.setLow(strToDecimal(hq_details.get(2).xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setFixTotalShare(strToDecimal(hq_details.get(2).xpath("//tr/td[2]/text(0)").get().trim()));
            dayInfo.setPb(strToDecimal(hq_details.get(2).xpath("//tr/td[3]/text(0)").get().trim()));

            dayInfo.setClose(strToDecimal(hq_details.get(3).xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setCvs(strToDecimal(hq_details.get(3).xpath("//tr/td[2]/text(0)").get().trim()));
            dayInfo.setPe(strToDecimal(hq_details.get(3).xpath("//tr/td[3]/text(0)").get().trim()));

            dayInfo.setZgb(strToDecimal(hq_details.get(4).xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setLtgb(strToDecimal(hq_details.get(4).xpath("//tr/td[2]/text(0)").get().trim()));
            //主力散户资金流向
            Selectable flowTable = page.getHtml().xpath("//*[@id=\"MRFlow\"]/table/tbody/tr[2]");
           try {
               dayInfo.setMainIn(strToDecimal(flowTable.xpath("//tr/td[1]/text(0)").get().trim()));
               dayInfo.setMainOut(strToDecimal(flowTable.xpath("//tr/td[2]/text(0)").get().trim()));
               dayInfo.setRetailIn(strToDecimal(flowTable.xpath("//tr/td[3]/text(0)").get().trim()));
               dayInfo.setRetailOut(strToDecimal(flowTable.xpath("//tr/td[4]/text(0)").get().trim()));
           }catch (Exception ex){
               dayInfo.setMainIn(BigDecimal.ZERO);
               dayInfo.setMainOut(BigDecimal.ZERO);
               dayInfo.setRetailIn(BigDecimal.ZERO);
               dayInfo.setRetailOut(BigDecimal.ZERO);
           }
            dayInfo.setIsDeleted(false);
            dayInfo.setGmtCreate(new Date());
            dayInfo.setGmtModified(new Date());
            dayInfo.setTs(System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();
            page.putField("closed", "false");
            page.putField("dayInfo", mapper.writeValueAsString(dayInfo));
        } catch (Exception e) {
            logger.error("爬取股票代码过程中序列化股票数据异常："+page.getHtml().xpath("//*[@id=\"stockName\"]").get(),e);
            //保存失败重新爬取
            Request newRequest = page.getRequest();
            newRequest.setUrl(page.getUrl().get()+"?numtime="+ Math.random());
            page.addTargetRequest(page.getRequest());
            page.setSkip(true);
        }
    }



    @Override
    public Site getSite() {
        return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:69.0) Gecko/20100101 Firefox/69.0");
//        return  Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
    }
}
