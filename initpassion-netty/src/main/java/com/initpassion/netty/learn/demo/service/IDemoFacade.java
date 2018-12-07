/*
 * @(#) IDemoFacade.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.service;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
public interface IDemoFacade {

    /**
     * BTC翻倍
     *
     * @param btcPrice
     * @return
     */
    Double doubleBtc(Double btcPrice);
}
