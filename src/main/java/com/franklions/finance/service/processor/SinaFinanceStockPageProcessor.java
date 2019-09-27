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

/**新浪财经股票信息爬取
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
            //取出内容
            FinanceStockDay dayInfo = new FinanceStockDay();

            Selectable stock = page.getHtml().xpath("//*[@id=\"stockName\"]");
            dayInfo.setStockCode(stock.xpath("//span/text(0)").get().trim().substring(1, 7));
            dayInfo.setStockName(stock.xpath("//i/text(0)").get().trim());
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
            dayInfo.setMainIn(strToDecimal(flowTable.xpath("//tr/td[1]/text(0)").get().trim()));
            dayInfo.setMainOut(strToDecimal(flowTable.xpath("//tr/td[2]/text(0)").get().trim()));
            dayInfo.setRetailIn(strToDecimal(flowTable.xpath("//tr/td[3]/text(0)").get().trim()));
            dayInfo.setRetailOut(strToDecimal(flowTable.xpath("//tr/td[4]/text(0)").get().trim()));

            dayInfo.setIsDeleted(false);
            dayInfo.setGmtCreate(new Date());
            dayInfo.setGmtModified(new Date());
            dayInfo.setTs(System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();

            page.putField("dayInfo", mapper.writeValueAsString(dayInfo));
        } catch (Exception e) {
            logger.error("爬取股票代码过程中序列化股票数据异常：",e);
            //保存失败重新爬取
            Request newRequest = page.getRequest();
            newRequest.setUrl(page.getUrl().get()+"?numtime="+ Math.random());
            page.addTargetRequest(page.getRequest());
            page.setSkip(true);
        }
    }

    private BigDecimal strToDecimal(String str) {
        if(str == null || "".equals(str)){
            return BigDecimal.ZERO;
        }

        str = str.replaceAll("手","")
                .replaceAll("万","")
                .replaceAll("亿","")
                .replaceAll("元","")
                .replaceAll("%","");

        try {
            return new BigDecimal(str);
        }catch (Exception ex){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:69.0) Gecko/20100101 Firefox/69.0");
    }
}
