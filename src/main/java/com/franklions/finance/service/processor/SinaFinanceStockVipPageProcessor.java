package com.franklions.finance.service.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockGrade;
import com.franklions.finance.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.franklions.finance.utils.Utils.strToDecimal;

/**
 * 新浪股票评级抓取
 *
 * @author flsh
 * @version 1.0
 * @date 2019-10-08
 * @since Jdk 1.8
 */
public class SinaFinanceStockVipPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SinaFinanceStockPageProcessor.class);

    @Override
    public void process(Page page) {

        Map<String,String[]> params = new HashMap<>();
        try {
            Utils.parseParameters(params,page.getUrl().get().split("\\?")[1],"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<Selectable> nodeList = page.getHtml().xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr").nodes();

        List<FinanceStockGrade> gradeList = new ArrayList<>();
        if (nodeList != null && nodeList.size() > 0) {
            for (Selectable node : nodeList) {
                if (node.xpath("//tr/td[1]/text(0)").get().trim().equals("股票代码")) {
                    continue;
                }

                FinanceStockGrade newGrade = new FinanceStockGrade();
                newGrade.setStockCode(node.xpath("//tr/td[1]/a/text(0)").get().trim());
                newGrade.setStockName(node.xpath("//tr/td[2]/a/span/text(0)").get().trim());
                newGrade.setTarget(strToDecimal(node.xpath("//tr/td[3]/text(0)").get().trim()));
                newGrade.setStockGrade(node.xpath("//tr/td[4]/text(0)").get().trim());
                newGrade.setCompany(node.xpath("//tr/td[5]/text(0)").get().trim());
                newGrade.setAnalyst(node.xpath("//tr/td[6]/text(0)").get().trim());
                newGrade.setBlock(node.xpath("//tr/td[7]/text(0)").get().trim());
                newGrade.setGradeDate(node.xpath("//tr/td[8]/text(0)").get().trim());
//                newGrade.setStockPrice(strToDecimal(node.xpath("//tr/td[10]/span/text(0)").get().trim()));
//                newGrade.setStockChange(strToDecimal(node.xpath("//tr/td[11]/span/text(0)").get().trim()));
                newGrade.setGmtCreate(new Date());
                newGrade.setGmtModified(new Date());
                newGrade.setTs(System.currentTimeMillis());

                if(newGrade.getGradeDate().equals(params.get("today")[0])) {
                    gradeList.add(newGrade);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            try {
                page.putField("grades", mapper.writeValueAsString(gradeList));
            } catch (JsonProcessingException e) {
                logger.error("爬取股票机构评级信息序列化数据保存异常：", e);
            }
        }else
        {
            page.setSkip(true);
        }


    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:69.0) Gecko/20100101 Firefox/69.0");
    }
}
