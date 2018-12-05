/*
 * @(#) DataCalculateEventMain.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.learn.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import lombok.extern.log4j.Log4j2;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Log4j2
public class DataCalculateEventMain {
    static Random rand = new Random();
    final static int coreSize = 4;
    final static int maxSize = 4;
    volatile static double totalMoney = 0;

    public static void produce(DataCalculateEvent event, long sequence, List<DataCalculateEvent> data) {
        event.setAge(rand.nextInt(100));
        event.setMoney(Math.random() * 1000);
        event.setUsername(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    }

    public static void consumer(DataCalculateEvent event, long sequence, boolean endOfBatch) {
        log.info(" username={}, age={}, money={}", event.getUsername(), event.getAge(), event.getMoney());
        if (event.getAge() > 40) {
            totalMoney += event.getMoney();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int ringBufferSize = 1024;
        ExecutorService executor = new ThreadPoolExecutor(coreSize, maxSize, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue(100));
        Disruptor<DataCalculateEvent> disruptor = new Disruptor<>(DataCalculateEvent::new, ringBufferSize, executor);
        disruptor.handleEventsWith(DataCalculateEventMain::consumer);
        disruptor.start();
        RingBuffer<DataCalculateEvent> ringBuffer = disruptor.getRingBuffer();
        ArrayList<DataCalculateEvent> data = Lists.newArrayList();
        while (true) {
            ringBuffer.publishEvent(DataCalculateEventMain::produce, data);
            Thread.sleep(1000);
        }
    }
}
