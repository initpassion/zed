/*
 * @(#) ThreadCancel.java 2018-12-28
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.futurecancel;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;

/**
 * @author tushenghong01
 * @version 2018-12-28
 */
public class ThreadCancel {

    private static volatile Map<String, Future> futureCache = Maps.newHashMap();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final Future<Boolean> future = exec.submit(new WorkerThread());
            futureCache.put(String.valueOf(i), future);
        }
        /**
         * 两分钟后取消前9个线程,保留最后一个线程
         */
        TimeUnit.MINUTES.sleep(2);
        for (int i = 0; i < 10; i++) {
            if (i < 9) {
                final Future future = futureCache.get(String.valueOf(i));
                if (future != null) {
                    future.cancel(true);
                }
            }

        }

    }

}

class WorkerThread implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {
        try {
            boolean run = true;
            while (run) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("[" + Thread.currentThread().getName() + "] this thread has been interrupted");
                    return false;
                }
                // 每隔5秒执行一次
                System.out.println("[" + Thread.currentThread().getName() + "] will sleep 2 seconds");
                // sleep模拟线程阻塞
                TimeUnit.SECONDS.sleep(5);
                System.out.println("[" + Thread.currentThread().getName() + "] this thread is alive");
            }

        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                System.out
                        .println("[" + Thread.currentThread().getName() + "] the blocking thread has been interrupted");
                return false;
            }
        }
        return true;
    }
}
