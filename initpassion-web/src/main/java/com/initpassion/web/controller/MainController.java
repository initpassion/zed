/*
 * @(#) MainController.java 2019-04-29
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author initpassion
 * @version 2019-04-29
 */
@RestController
public class MainController {

    @RequestMapping(value = "/index")
    public String sayHello(){
        return "hello world 0033";
    }
}

