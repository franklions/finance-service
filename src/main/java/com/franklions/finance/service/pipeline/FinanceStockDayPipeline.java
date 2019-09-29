package com.franklions.finance.service.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockDay;
import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.service.FinanceStockDayService;
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
public class FinanceStockDayPipeline implements Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(FinanceStockDayPipeline.class);

    private ObjectMapper mapper;
    private FinanceStockDayService dayService;
    private FinanceStockService stockService;

    public FinanceStockDayPipeline(ObjectMapper mapper, FinanceStockDayService dayService,
                                   FinanceStockService stockService) {
        this.mapper = mapper;
        this.dayService = dayService;
        this.stockService = stockService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems != null || resultItems.getAll().size() > 0){

            String closed = resultItems.get("closed");

            if(closed.equals("false")) {

                String dayData = resultItems.get("dayInfo");
                System.out.println(dayData);
                if (dayData == null || dayData.length() < 1) {
                    return;
                }

                try {
                    FinanceStockDay dayInfo = mapper.readValue(dayData, FinanceStockDay.class);
                    dayService.save(dayInfo);
                } catch (IOException e) {
                    logger.error("保存股票数据到数据库时发生异常：" + dayData, e);
                }
            }else{
                String code = resultItems.get("stockCode");
                stockService.deleteClosedStock(code);
            }
        }
    }
}
