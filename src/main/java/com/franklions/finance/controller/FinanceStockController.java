package com.franklions.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.service.*;
import com.franklions.finance.service.downloader.HtmlUnitDownloader;
import com.franklions.finance.service.downloader.MarketHtmlUnitDownloader;
import com.franklions.finance.service.pipeline.FinanceStockDayPipeline;
import com.franklions.finance.service.pipeline.FinanceStockGradePipeline;
import com.franklions.finance.service.pipeline.FinanceStockMarketPipeline;
import com.franklions.finance.service.processor.EastMoneyStockPageProcessor;
import com.franklions.finance.service.pipeline.FinanceStockPipeline;
import com.franklions.finance.service.processor.SinaFinanceStockMarketPageProcessor;
import com.franklions.finance.service.processor.SinaFinanceStockPageProcessor;
import com.franklions.finance.service.processor.SinaFinanceStockVipPageProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
@Api(tags = "webmagic",description = "des")
@RestController
public class FinanceStockController {

    @Autowired
    FinanceStockService stockService;

    @Autowired
    FinanceFundService fundService;

    @Autowired
    FinanceStockDayService stockDayService;

    @Autowired
    FinanceStockGradeService gradeService;

    @Autowired
    FinanceStockMarketService marketService;


    @Autowired
    ObjectMapper mapper;

    /**
     * 刷新股票代码
     * @return
     */
    @ApiOperation(value = "aaaaa", notes="aaaa")
    @PutMapping("/stock/refresh")
    public ResponseEntity<?> refreshStock(){
        CompletableFuture.runAsync(()->{
            Spider.create(new EastMoneyStockPageProcessor())
                    .addPipeline(new FinanceStockPipeline(stockService,fundService,mapper))
                    .addUrl("http://quote.eastmoney.com/stock_list.html")
                    .thread(1)
                    .run();
        });
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "bbb", notes="bbbb")
    @PutMapping("/stock/day")
    public ResponseEntity<?> getDayData(){
        CompletableFuture.runAsync(()->{
           Spider spider =  Spider.create(new SinaFinanceStockPageProcessor())
                    .setDownloader(new HtmlUnitDownloader())
                    .addPipeline(new FinanceStockDayPipeline(mapper,stockDayService,stockService))
                    .setScheduler(new RedisScheduler("localhost"))
                    .thread(10);

             List<FinanceStockInfo> stocks = stockService.selectAll();

            if(stocks != null && stocks.size() > 0) {
                for(FinanceStockInfo info : stocks){
                    spider.addUrl(String.format("http://finance.sina.com.cn/realstock/company/%s%s/nc.shtml",info.getStockType(),info.getStockCode()));
                }
                spider.run();
            }
        });

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "cccc", notes="cccc")
    @GetMapping("/stock/grade/{code}")
    public ResponseEntity<?> getGradeData(@PathVariable("code") String code){
        CompletableFuture.runAsync(()->{
            Spider spider =  Spider.create(new SinaFinanceStockVipPageProcessor())
                    .addPipeline(new FinanceStockGradePipeline(mapper,gradeService))
                    .setScheduler(new RedisScheduler("localhost"))
                    .thread(10);

            String today = "2019-11-28";
            Integer pages = 7;
            for(int i =1;i<=pages;i++) {
                spider.addUrl("http://stock.finance.sina.com.cn/stock/go.php/vIR_RatingNewest/index.phtml?num=60&p="+i+"&today="+today+"&numtime="+ Math.random());
            }
            spider.run();

        });

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "ddddd", notes="dddddd")
    @GetMapping("/stock/market")
    public ResponseEntity<?> getMarketData(){
        CompletableFuture.runAsync(()->{
            Spider.create(new SinaFinanceStockMarketPageProcessor())
                    .setDownloader(new MarketHtmlUnitDownloader())
                    .addPipeline(new FinanceStockMarketPipeline(mapper,marketService))
                    .addUrl("https://finance.sina.com.cn/realstock/company/sh000001/nc.shtml?numtime="+ Math.random())
                    .addUrl("http://finance.sina.com.cn/realstock/company/sz399001/nc.shtml?numtime="+ Math.random())
                    .addUrl("http://finance.sina.com.cn/realstock/company/sz399006/nc.shtml?numtime="+ Math.random())
                    .thread(1)
                    .run();

        });

        return ResponseEntity.ok().build();
    }
}
