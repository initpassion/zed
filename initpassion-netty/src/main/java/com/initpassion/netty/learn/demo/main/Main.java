/*
 * @(#) Main.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.main;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.initpassion.netty.learn.demo.NettyProxy;
import com.initpassion.netty.learn.demo.netty.NettyServer;
import com.initpassion.netty.learn.demo.service.IDemoFacade;

import lombok.extern.log4j.Log4j2;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
@Log4j2
public class Main {

    // 这里集中测试服务端和客户端,顺便玩玩线程池

    private static final int CORE_SIZE = 4;

    private static final int MAX_SIZE = 4;

    private static final int QUEUE_SIZE = 1000;

    private static final int KEEP_ALIVE = 1;

    private static final int CLIENT_SIZE = 1000;

    ExecutorService service;
    volatile int totalPrice;

    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    public void run() throws Exception {
        service = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_SIZE));
        Thread thread = null;
        try {
            thread = new Thread(() -> {
                try {
                    new NettyServer(8090).run();
                } catch (Exception e) {
                    log.error("Netty server error , Program will be close right now", e);
                    return;
                }
            });
            thread.start();
            TimeUnit.SECONDS.sleep(10);
            clientRequest();
            log.info("total price : {}", totalPrice);
            log.info("Program has success handle all request");
        } finally {
            if (thread != null) {
                if (!thread.isInterrupted()) {
                    thread.interrupt();
                    log.info("Program closed successfully");
                }
            }
            service.shutdown();
        }

    }

    public void clientRequest() throws InterruptedException, ExecutionException {
        log.info(">>>> client request starting <<<<");
        List<Future<Double>> futures = Lists.newArrayListWithCapacity(CLIENT_SIZE);
        /**
         * 模拟100个客户端,并且同时请求
         */
        CountDownLatch countDownLatch = new CountDownLatch(CLIENT_SIZE);
        for (int i = 0; i < CLIENT_SIZE; i++) {
            Callable callable = () -> {
                try {
                    IDemoFacade facade = (IDemoFacade) NettyProxy.getProxyInstance(IDemoFacade.class);
                    return facade.doubleBtc(Math.random() * 1000);
                } finally {
                    countDownLatch.countDown();
                }

            };
            Future<Double> price = service.submit(callable);
            futures.add(price);
            futures.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
        countDownLatch.await();
    }
}
