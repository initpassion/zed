/*
 * @(#) DubboServerHandler.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.netty;

import java.lang.reflect.Method;

import com.initpassion.netty.learn.demo.service.DemoFacadeService;
import com.initpassion.netty.learn.demo.service.IDemoFacade;
import com.initpassion.netty.learn.demo.service.NettyBean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
@Log4j2
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Netty server has receive a message : {}", msg);
        NettyBean req = (NettyBean) msg;
        Object target = this.getInstenceByInterfaceClass(req.getInterfaceClass());
        String methodName = req.getMethodName();
        Method method = target.getClass().getMethod(methodName, req.getParamTypes());
        Object res = method.invoke(target, req.getArgs());
        ctx.writeAndFlush(res);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * @param clazz
     * @return
     */
    private Object getInstenceByInterfaceClass(Class<?> clazz) {
        if (IDemoFacade.class.equals(clazz)) {
            return new DemoFacadeService();
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
