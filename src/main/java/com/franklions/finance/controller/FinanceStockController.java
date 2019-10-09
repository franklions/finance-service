package com.franklions.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.service.FinanceStockDayService;
import com.franklions.finance.service.FinanceStockGradeService;
import com.franklions.finance.service.downloader.HtmlUnitDownloader;
import com.franklions.finance.service.downloader.VipHtmlUnitDownloader;
import com.franklions.finance.service.pipeline.FinanceStockDayPipeline;
import com.franklions.finance.service.pipeline.FinanceStockGradePipeline;
import com.franklions.finance.service.processor.EastMoneyStockPageProcessor;
import com.franklions.finance.service.FinanceFundService;
import com.franklions.finance.service.pipeline.FinanceStockPipeline;
import com.franklions.finance.service.FinanceStockService;
import com.franklions.finance.service.processor.SinaFinanceStockPageProcessor;
import com.franklions.finance.service.processor.SinaFinanceStockVipPageProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PutMapping("/stock/grade/{code}")
    public ResponseEntity<?> getGradeData(@PathVariable("code") String code){
        CompletableFuture.runAsync(()->{
            Spider spider =  Spider.create(new SinaFinanceStockVipPageProcessor())
                    .setDownloader(new VipHtmlUnitDownloader())
                    .addPipeline(new FinanceStockGradePipeline(mapper,gradeService))
//                    .setScheduler(new RedisScheduler("localhost"))
                    .thread(1);
            spider.addUrl("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_StockSearch/key/"+code+".phtml?num=60&p=1");
            spider.run();

        });

        return ResponseEntity.ok().build();
    }
}
