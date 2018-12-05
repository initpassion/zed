package com.initpassion.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class InitpassionCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(InitpassionCoreApplication.class, args);
    }
}
