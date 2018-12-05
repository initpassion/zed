/*
 * @(#) LongEventProducerWithTranslator.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventProducerWithTranslator {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = (event, sequence, arg0) -> event
            .setValue(arg0.getLong(0));

    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }

}
