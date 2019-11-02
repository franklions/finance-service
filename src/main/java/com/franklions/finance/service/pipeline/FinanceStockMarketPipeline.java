package com.franklions.finance.service.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockDay;
import com.franklions.finance.domain.FinanceStockMarket;
import com.franklions.finance.service.FinanceStockDayService;
import com.franklions.finance.service.FinanceStockMarketService;
import com.franklions.finance.service.FinanceStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-25
 * @since Jdk 1.8
 */
public class FinanceStockMarketPipeline implements Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(FinanceStockMarketPipeline.class);

    private ObjectMapper mapper;
    private FinanceStockMarketService marketervice;


    public FinanceStockMarketPipeline(ObjectMapper mapper, FinanceStockMarketService marketService) {
        this.mapper = mapper;
        this.marketervice = marketService;

    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems != null || resultItems.getAll().size() > 0){

            String dayData = resultItems.get("marketInfo");
            System.out.println(dayData);
            if (dayData == null || dayData.length() < 1) {
                return;
            }

            try {
                FinanceStockMarket market = mapper.readValue(dayData, FinanceStockMarket.class);
                marketervice.save(market);
            } catch (IOException e) {
                logger.error("保存大盘数据到数据库时发生异常：" + dayData, e);
            }

        }
    }
}
