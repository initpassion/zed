/*
 * @(#) LongEventMain8.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo.java8;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.initpassion.disruptor.office.demo.LongEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventMain8 {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        /**
         * 省略工厂类
         */
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, executor);
        /**
         * 省略消费者类
         */
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event: " + event.getValue()));
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            /**
             * 省略生产者类
             */
            ringBuffer.publishEvent((event, sequence, buffer) -> event.setValue(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }
}
