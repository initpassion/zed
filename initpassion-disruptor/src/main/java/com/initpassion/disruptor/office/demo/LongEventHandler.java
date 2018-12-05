/*
 * @(#) LongEventHandler.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import com.lmax.disruptor.EventHandler;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(event.getValue());
    }
}
