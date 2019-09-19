package com.franklions.finance;

import com.franklions.finance.service.EasyMoneyStockPageProcessor;
import com.franklions.finance.service.FinanceStockPipeline;
import com.franklions.finance.service.FinanceStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-18
 * @since Jdk 1.8
 */
@SpringBootApplication
public class RunApplication implements CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication.run(RunApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>running>>>>>>>>>>>>");
    }
}
