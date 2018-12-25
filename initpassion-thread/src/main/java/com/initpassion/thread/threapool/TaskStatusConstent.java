/*
 * @(#) TaskStatusConstent.java 2018-12-25
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.threapool;

/**
 * @author tushenghong01
 * @version 2018-12-25
 */
public interface TaskStatusConstent {

    /**
     * 新创建
     */
    int CREATED = 0;

    /**
     * 取消中
     */
    int CANCELING = 1;

    /**
     * 取消成功
     */
    int CANCELED = 2;

    /**
     * 已完成
     */
    int FINISHED = 3;

}
