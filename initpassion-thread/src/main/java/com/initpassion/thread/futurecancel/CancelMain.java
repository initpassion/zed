/*
 * @(#) CancelMain.java 2018-12-26
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.futurecancel;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Maps;

/**
 * @author tushenghong01
 * @version 2018-12-26
 */
public class CancelMain {

    /**
     * 存放每个线程对应的UUID
     */
    private volatile static Map<String, Future> futureMap = Maps.newConcurrentMap();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 4; i++) {
            Future<Boolean> future = exec.submit(new WorkThread());
            futureMap.put(String.valueOf(i), future);
        }
        Thread.sleep(10000);

        for (Map.Entry<String, Future> map : futureMap.entrySet()) {
            if (Integer.valueOf(map.getKey()) < 3) {
                Future threadFuture = map.getValue();
                Thread.sleep(1000);
                threadFuture.cancel(true);
            }
        }
    }

}
