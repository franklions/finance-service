package com.franklions.finance.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceFundInfo;
import com.franklions.finance.domain.FinanceStockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-18
 * @since Jdk 1.8
 */
public class FinanceStockPipeline implements Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(FinanceStockPipeline.class);

    private FinanceStockService stockService;
    private FinanceFundService fundService;

    public FinanceStockPipeline(FinanceStockService stockService, FinanceFundService fundService) {
        this.stockService = stockService;
        this.fundService = fundService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems.getAll().size() > 0){
            String jsonStr = resultItems.get("stocks");
            if(jsonStr != null && jsonStr != "")
            {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<FinanceStockInfo> dataList = mapper.readValue(jsonStr, new TypeReference<List<FinanceStockInfo>>() {});

                    //分批次进行存储
                    int stepLen = 100;
                    int batch = Double.valueOf(Math.ceil( dataList.size() / stepLen)).intValue();
                    for(int i=0;i<=batch;i++) {
                        int formIndex =i * stepLen;
                        int toIndex = (i+1) *stepLen;

                        if(toIndex > dataList.size()){
                            toIndex = dataList.size() ;
                        }

                        List<FinanceStockInfo> tmp = dataList.subList(formIndex,toIndex);
                        stockService.batchSave(tmp);
                    }
                    logger.info("爬取股票代码成功,总共:{}",dataList.size());
                } catch (Exception e) {
                    logger.error("保存股票代码到数据库时发生异常：",e);
                }
            }

            //保存基金
            jsonStr = resultItems.get("funds");
            if(jsonStr != null && jsonStr != "")
            {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<FinanceFundInfo> dataList = mapper.readValue(jsonStr, new TypeReference<List<FinanceFundInfo>>() {});

                    //分批次进行存储
                    int stepLen = 100;
                    int batch = Double.valueOf(Math.ceil( dataList.size() / stepLen)).intValue();
                    for(int i=0;i<=batch;i++) {
                        int formIndex =i * stepLen;
                        int toIndex = (i+1) *stepLen;

                        if(toIndex > dataList.size()){
                            toIndex = dataList.size() ;
                        }

                        List<FinanceFundInfo> tmp = dataList.subList(formIndex,toIndex);
                        fundService.batchSave(tmp);
                    }
                    logger.info("爬取基金代码成功,总共:{}",dataList.size());
                } catch (Exception e) {
                    logger.error("保存基金代码到数据库时发生异常：",e);
                }
            }
        }
    }
}
