/*
 * @(#) NeetyDubbo.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.service;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
@Data
@AllArgsConstructor
public class NettyBean implements Serializable {
    private Class<?> interfaceClass;
    private Class<?>[] paramTypes;
    private Object[] args;
    private String methodName;
}
