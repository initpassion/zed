/*
 * @(#) LongEventFactory.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import com.lmax.disruptor.EventFactory;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
