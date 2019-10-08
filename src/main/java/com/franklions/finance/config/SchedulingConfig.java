package com.franklions.finance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.service.FinanceStockDayService;
import com.franklions.finance.service.FinanceStockService;
import com.franklions.finance.service.downloader.HtmlUnitDownloader;
import com.franklions.finance.service.pipeline.FinanceStockDayPipeline;
import com.franklions.finance.service.processor.SinaFinanceStockPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    FinanceStockService stockService;

    @Autowired
    FinanceStockDayService stockDayService;

    @Autowired
    ObjectMapper mapper;


    @Scheduled(cron = "${cron.expression}") // 每天下午3点半执行
    public void scheduler() {
        Spider spider = Spider.create(new SinaFinanceStockPageProcessor())
                .setDownloader(new HtmlUnitDownloader())
                .addPipeline(new FinanceStockDayPipeline(mapper, stockDayService, stockService))
                .setScheduler(new RedisScheduler("localhost"))
                .thread(10);

        List<FinanceStockInfo> stocks = stockService.selectAll();

        if (stocks != null && stocks.size() > 0) {
            for (FinanceStockInfo info : stocks) {
                spider.addUrl(String.format("http://finance.sina.com.cn/realstock/company/%s%s/nc.shtml?numtime="+ Math.random(), info.getStockType(), info.getStockCode()));
            }
            spider.run();
        }
    }
}
