/*
 * @(#) HelloWordController.java 2019-06-04
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tushenghong01
 * @version 2019-06-04
 */
@RestController
public class HelloWordController {

    @Value("${key:100}")
    private String key;

    @GetMapping(value = "hello")
    public String hello() {
        return key;
    }
}
