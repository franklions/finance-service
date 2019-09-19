package com.franklions.finance.controller;

import com.franklions.finance.service.EasyMoneyStockPageProcessor;
import com.franklions.finance.service.FinanceFundService;
import com.franklions.finance.service.FinanceStockPipeline;
import com.franklions.finance.service.FinanceStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import java.util.concurrent.CompletableFuture;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
@RestController
public class FinanceStockController {

    @Autowired
    FinanceStockService stockService;

    @Autowired
    FinanceFundService fundService;

    /**
     * 刷新股票代码
     * @return
     */
    @PutMapping("/stock/refresh")
    public ResponseEntity<?> refreshStock(){
        CompletableFuture.runAsync(()->{
            Spider.create(new EasyMoneyStockPageProcessor())
                    .addPipeline(new FinanceStockPipeline(stockService,fundService))
                    .addUrl("http://quote.eastmoney.com/stock_list.html")
                    .thread(1)
                    .run();
        });
        return ResponseEntity.ok().build();
    }
}
