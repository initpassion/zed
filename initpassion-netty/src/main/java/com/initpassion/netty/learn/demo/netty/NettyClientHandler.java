/*
 * @(#) NettyClientHandler.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.netty;

import java.lang.reflect.Method;

import com.initpassion.netty.learn.demo.service.NettyBean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
@Log4j2
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private Object proxy;
    private Method method;
    private Object[] args;
    private Object response;

    public NettyClientHandler(Object proxy, Method method, Object[] args, Object response) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.response = response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyBean req = new NettyBean(proxy.getClass().getInterfaces()[0], method.getParameterTypes(), args,
                method.getName());
        ctx.write(req);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Netty client request success");
        // return server massage
        response = msg;
        log.info("Netty client receive the response : {}", response);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Netty client request has error", cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
