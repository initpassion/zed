/*
 * @(#) Task.java 2018-12-25
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.thread.threapool;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tushenghong01
 * @version 2018-12-25
 */
@Getter
@Setter
@Builder
public class Task {

    private String uuid;

    private String taskName;

    private int status;
}
