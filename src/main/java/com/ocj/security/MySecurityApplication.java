package com.ocj.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.ocj.security.mapper")
@EnableCaching
@EnableTransactionManagement
public class MySecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySecurityApplication.class, args);
    }

}
