/*
 * @(#) LongEvent.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import lombok.Data;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Data
public class LongEvent {
    /**
     * 数据载体
     */
    private Long value;
}
