/*
 * @(#) TaskThread.java 2018-12-25
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.threapool;

import static com.initpassion.thread.threapool.TaskStatusConstent.CANCELING;

import java.util.concurrent.Callable;

import lombok.Data;

/**
 * @author tushenghong01
 * @version 2018-12-25
 */
@Data
public class TaskThread implements Callable<Boolean> {

    private Task task;

    public TaskThread(Task task) {
        this.task = task;
    }

    @Override
    public Boolean call() throws InterruptedException {
        long start = System.currentTimeMillis();
        boolean run = true;
        boolean flag = true;
        while (run) {
            System.out.println("this uuid has running" + task.getUuid() + "-threadName-" + task.getTaskName());
            long end = System.currentTimeMillis();
            // 程序默认执行六分钟
            if (end - start >= 10 * 1000 && flag) {
                task.setStatus(CANCELING);
                flag = false;
            }
            // 程序默认执行六分钟
            if (end - start >= 10 * 60 * 1000) {
                run = false;
            }

            /**
             * 任务被取消
             */
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("PrimerTask.call： cancel me ? why?");
                return false;
            }

        }
        System.out.println("this uuid has finished");
        return true;
    }

}
