package com.franklions.finance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        System.out.println(">>>>>>>>>>>>>running>>>>>>>>>>>>");
    }
}
