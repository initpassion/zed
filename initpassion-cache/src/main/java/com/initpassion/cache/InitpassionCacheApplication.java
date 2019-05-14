package com.initpassion.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.initpassion.cache.dao")
@SpringBootApplication
public class InitpassionCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(InitpassionCacheApplication.class, args);
    }

}
