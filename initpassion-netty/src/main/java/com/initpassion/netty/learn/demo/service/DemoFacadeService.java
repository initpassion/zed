/*
 * @(#) DemoFacadeService.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.service;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
public class DemoFacadeService implements IDemoFacade {

    /**
     * 翻滚吧
     * 
     * @param btcPrice
     * @return
     */
    @Override
    public Double doubleBtc(Double btcPrice) {
        return 2 * btcPrice;
    }
}
