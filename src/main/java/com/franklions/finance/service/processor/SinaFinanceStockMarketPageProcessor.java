package com.franklions.finance.service.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockDay;
import com.franklions.finance.domain.FinanceStockMarket;
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
 * 新浪财经股票大盘信息爬取
 * @author flsh
 * @version 1.0
 * @date 2019-09-20
 * @since Jdk 1.8
 */
public class SinaFinanceStockMarketPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SinaFinanceStockMarketPageProcessor.class);

    @Override
    public void process(Page page) {
        try {

            //检查是否已退市 未上市 停牌股
            try {
                String closed = page.getHtml().xpath("//*[@id=\"closed\"]/text(0)").get();
                if(closed.trim().equals("已退市") ||closed.trim().equals("未上市")){
                    page.putField("closed", "true");
                    page.putField("stockCode", page.getHtml().xpath("//*[@id=\"stockName\"]/span/text(0)").get().trim().substring(1, 7));
                    return ;
                }

                String closedStyle = page.getHtml().xpath("//*[@id=\"closed\"]/@style").get();
                if(closedStyle.trim().equals("display: block;")){
                    page.setSkip(true);
                    return;
                }
            }catch (Exception  ex){

            }

            //取出内容
            FinanceStockMarket maketInfo = new FinanceStockMarket();

            Selectable stock = page.getHtml().xpath("//*[@id=\"stockName\"]");
            maketInfo.setStockCode(stock.xpath("//span/text(0)").get().trim().substring(1, 7));
            maketInfo.setStockName(page.getHtml().xpath("//*[@id=\"stockName\"]/text(0)").get().trim());
            maketInfo.setStockDate(page.getHtml().xpath("//*[@id=\"hqTime\"]/text(0)").get().trim().substring(0, 10));
            //股票价格信息
            maketInfo.setCurrent(strToDecimal(page.getHtml().xpath("//*[@id=\"price\"]/text(0)").get().trim()));
            maketInfo.setChange(strToDecimal(page.getHtml().xpath("//*[@id=\"change\"]/text(0)").get().trim()));
            maketInfo.setChangePct(page.getHtml().xpath("//*[@id=\"changeP\"]/text(0)").get().trim());

            List<Selectable> hq_details = page.getHtml().xpath("//*[@id=\"hqDetails\"]/table/tbody/tr").nodes();

            maketInfo.setOpen(strToDecimal(hq_details.get(0).xpath("//tr/td[1]/text(0)").get().trim()));
            maketInfo.setHigh(strToDecimal(hq_details.get(0).xpath("//tr/td[2]/text(0)").get().trim()));

            maketInfo.setClose(strToDecimal(hq_details.get(1).xpath("//tr/td[1]/text(0)").get().trim()));
            maketInfo.setLow(strToDecimal(hq_details.get(1).xpath("//tr/td[2]/text(0)").get().trim()));

            maketInfo.setVolume(strToDecimal(hq_details.get(2).xpath("//tr/td[1]/text(0)").get().trim()));
            maketInfo.setSwing(strToDecimal(hq_details.get(2).xpath("//tr/td[2]/text(0)").get().trim()));

            maketInfo.setAmount(strToDecimal(hq_details.get(3).xpath("//tr/td[1]/text(0)").get().trim()));


            maketInfo.setGmtCreate(new Date());
            maketInfo.setGmtModified(new Date());
            maketInfo.setTs(System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();
            page.putField("closed", "false");
            page.putField("marketInfo", mapper.writeValueAsString(maketInfo));
        } catch (Exception e) {
            logger.error("爬取大盘数据过程中序列化股票数据异常："+page.getHtml().xpath("//*[@id=\"stockName\"]").get(),e);
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
