/*
 * @(#) HelloController.java 2019-05-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassiom.caffeine.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author initpassion
 * @version 2019-05-11
 */
@RestController
public class HelloController {

    @RequestMapping(value = "/initpassion")
    public String say(){
        return "initpassion";
    }
}
