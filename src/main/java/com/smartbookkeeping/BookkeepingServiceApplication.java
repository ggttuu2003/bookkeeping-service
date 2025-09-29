package com.smartbookkeeping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 智能记账服务主应用类
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.smartbookkeeping.mapper")
public class BookkeepingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeepingServiceApplication.class, args);
    }
}