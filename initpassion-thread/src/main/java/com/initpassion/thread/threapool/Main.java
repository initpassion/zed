/*
 * @(#) Main.java 2018-12-25
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.threapool;

import static com.initpassion.thread.threapool.TaskStatusConstent.CANCELING;
import static com.initpassion.thread.threapool.TaskStatusConstent.CREATED;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Maps;

/**
 * @author tushenghong01
 * @version 2018-12-25
 */
public class Main {

    /**
     * 模拟数据库的存储
     */
    public static Map<String, Task> tasks = Maps.newConcurrentMap();

    static {
        for (int i = 0; i < 4; i++) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            tasks.put(uuid, Task.builder().status(CREATED).uuid(uuid).taskName(String.valueOf(i)).build());
        }
    }

    /**
     * 存放每个线程对应的UUID
     */
    private volatile static Map<String, Future> futureMap = Maps.newConcurrentMap();

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // 这里简单实现用 timer功能足够了
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<String, Task> task : tasks.entrySet()) {
                    Task t = task.getValue();
                    if (t.getStatus() == CREATED) {
                        Future<?> future = exec.submit(new TaskThread(t));
                        futureMap.put(t.getUuid(), future);
                    } else if (t.getStatus() == CANCELING) {
                        Future future = null;
                        try {
                            future = futureMap.get(t.getUuid());
                            if (future != null || future.isCancelled()) {
                                tasks.remove(t.getUuid());
                                futureMap.remove(t.getUuid());
                            }
                        } finally {
                            future.cancel(true);
                        }
                    }
                }

            }
        };
        // 从现在开始每间隔 1s 计划执行一个任务
        timer.schedule(timerTask, 0, 1000);
        System.out.println("finished");
    }

}
