/*
 * @(#) NettyDubboProxy.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo;

import java.lang.reflect.Proxy;

import com.initpassion.netty.learn.demo.netty.NettyConsumer;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
public class NettyProxy {

    public static Object getProxyInstance(Class<?> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new NettyConsumer());
    }
}
