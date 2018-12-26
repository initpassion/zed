/*
 * @(#) WorkThread.java 2018-12-26
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.futurecancel;

import java.util.concurrent.Callable;

/**
 * @author tushenghong01
 * @version 2018-12-26
 */
public class WorkThread implements Callable<Boolean> {

    @Override
    public Boolean call() throws Exception {
        for (;;) {
            System.out.println("this thread is running -" + Thread.currentThread().getName());
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("this thread is interrupted -" + Thread.currentThread().getName());
                return false;
            }
        }

    }
}
