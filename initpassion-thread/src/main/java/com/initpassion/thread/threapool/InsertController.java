/*
 * @(#) RequestController.java 2018-12-25
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.threapool;

import static com.initpassion.thread.threapool.TaskStatusConstent.CREATED;

import java.util.UUID;

/**
 *
 * 模拟后台管理系统操作
 * 
 * @author tushenghong01
 * @version 2018-12-25
 */
public class InsertController {

    public static void main(String[] args) {
        Task task = Task.builder().uuid(UUID.randomUUID().toString().replace("-", "")).taskName("").status(CREATED)
                .build();
    }
}
