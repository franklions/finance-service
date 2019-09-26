package com.franklions.finance.service.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceFundInfo;
import com.franklions.finance.domain.FinanceStockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 东方财富 股票代码
 * @author flsh
 * @version 1.0
 * @date 2019-09-18
 * @since Jdk 1.8
 */
public class EastMoneyStockPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EastMoneyStockPageProcessor.class);

    @Override
    public void process(Page page) {

        List<Selectable> shSelectNodes = page.getHtml().xpath("//*[@id=\"quotesearch\"]/ul[1]/li").nodes();
        List<Selectable> szSelectNodes = page.getHtml().xpath("//*[@id=\"quotesearch\"]/ul[2]/li").nodes();

        List<FinanceStockInfo> stockInfos = new ArrayList<>();
        List<FinanceFundInfo> fundInfos = new ArrayList<>();

        if(shSelectNodes != null && shSelectNodes.size() >0){
            for(Selectable item:shSelectNodes){
                String itemText = item.xpath("//a/text(0)").get();
                String itemCode = itemText.substring(itemText.indexOf("(") + 1, itemText.length() - 1);

                if(itemCode.matches("^(((002|000|200|300|600|900)[\\d]{3})|60[\\d]{4})$")){
                    FinanceStockInfo info = getFinanceStockInfo(itemText, "sh");
                    stockInfos.add(info);
                }else{
                    FinanceFundInfo fInfo = getFinanceFundInfo(itemText,"sh");
                    fundInfos.add(fInfo);
                }

            }
        }

        if(szSelectNodes != null || szSelectNodes.size() >0){
            for(Selectable item:szSelectNodes){
                String itemText = item.xpath("//a/text(0)").get();
                String itemCode = itemText.substring(itemText.indexOf("(") + 1, itemText.length() - 1);
                if(itemCode.matches("^(((002|000|200|300|600|900)[\\d]{3})|60[\\d]{4})$")){
                    FinanceStockInfo info = getFinanceStockInfo(itemText, "sz");
                    stockInfos.add(info);
                }else{
                    FinanceFundInfo fInfo = getFinanceFundInfo(itemText,"sz");
                    fundInfos.add(fInfo);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            page.putField("stocks", mapper.writeValueAsString(stockInfos));
            page.putField("funds", mapper.writeValueAsString(fundInfos));
        } catch (JsonProcessingException e) {
            logger.error("爬取股票代码过程中序列化股票数据保存异常：",e);
        }
    }

    private FinanceFundInfo getFinanceFundInfo(String itemText, String sz) {
        FinanceFundInfo info = new FinanceFundInfo();
        info.setGmtCreate(new Date());
        info.setGmtModified(new Date());
        info.setTs(System.currentTimeMillis());
        info.setIsDeleted(false);
        info.setFundType(sz);
        info.setFundCode(itemText.substring(itemText.indexOf("(") + 1, itemText.length() - 1));
        info.setFundName(itemText.substring(0, itemText.indexOf("(")));
        return info;
    }

    private FinanceStockInfo getFinanceStockInfo(String itemText, String sh) {
        FinanceStockInfo info = new FinanceStockInfo();
        info.setGmtCreate(new Date());
        info.setGmtModified(new Date());
        info.setTs(System.currentTimeMillis());
        info.setIsDeleted(false);
        info.setStockType(sh);
        info.setStockCode(itemText.substring(itemText.indexOf("(") + 1, itemText.length() - 1));
        info.setStockName(itemText.substring(0, itemText.indexOf("(")));
        return info;
    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:69.0) Gecko/20100101 Firefox/69.0")
                .setTimeOut(6000)
                .setSleepTime(5000);
    }
}