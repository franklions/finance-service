package com.franklions.finance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.service.downloader.HtmlUnitDownloader;
import com.franklions.finance.service.pipeline.FinanceStockDayPipeline;
import com.franklions.finance.service.processor.SinaFinanceStockPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-18
 * @since Jdk 1.8
 */
@SpringBootApplication
public class RunApplication implements CommandLineRunner {

    @Autowired
    ObjectMapper mapper;

    public static void main(String[] args)  {
        SpringApplication.run(RunApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.setProperty("http.keepAlive", "false");
        Spider spider = Spider.create(new SinaFinanceStockPageProcessor())
                .setDownloader(new HtmlUnitDownloader())
                .setScheduler(new RedisScheduler("localhost"))
                .thread(1);

                spider.addUrl("http://finance.sina.com.cn/realstock/company/sh601313/nc.shtml?numtime="+ Math.random());
                spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600835/nc.shtml?numtime="+ Math.random());
//                spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600152/nc.shtml?numtime="+ Math.random());
//                spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600159/nc.shtml?numtime="+ Math.random());
//        spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600232/nc.shtml?numtime="+ Math.random());
//        spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600239/nc.shtml?numtime="+ Math.random());
//        spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600278/nc.shtml?numtime="+ Math.random());
//        spider.addUrl("http://finance.sina.com.cn/realstock/company/sh600310/nc.shtml?numtime="+ Math.random());
            spider.run();

        System.out.println(">>>>>>>>>>>>>running>>>>>>>>>>>>");

    }
}
