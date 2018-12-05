/*
 * @(#) DataCalculateEvent.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.learn.demo;

import lombok.Data;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Data
public class DataCalculateEvent {

    private String username;

    private double money;

    private int age;

}
