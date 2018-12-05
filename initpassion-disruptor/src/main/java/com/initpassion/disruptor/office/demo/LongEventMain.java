/*
 * @(#) LongEventMain.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventMain {

    public static void main(String[] args) {
        Executor executor = Executors.newCachedThreadPool();
        EventFactory eventFactory = new LongEventFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor);
        // 消费者
        EventHandler<LongEvent> eventHandler = new LongEventHandler();
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
