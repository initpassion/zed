/*
 * @(#) LongEventMethodMain8.java 2018-12-05
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
public class LongEventMethodMain8 {

    public static void consumer(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event:" + event.getValue());
    }

    public static void produce(LongEvent event, long sequence, ByteBuffer buffer) {
        event.setValue(buffer.getLong(0));
    }

    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, executor);
        disruptor.handleEventsWith(LongEventMethodMain8::consumer);
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent(LongEventMethodMain8::produce, bb);
            Thread.sleep(1000);
        }
    }
}
