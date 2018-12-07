/*
 * @(#) DubboConsumerHandler.java 2018-12-07
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.netty.learn.demo.netty;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author tushenghong01
 * @version 2018-12-07
 */
public class NettyConsumer implements InvocationHandler {

    /**
     * 服务器返回
     */
    private Object response;

    private static final String LOCALHOST = "127.0.0.1";

    private static final int PORT = 8090;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // WorkThreadPool ;这里当做线程池理解, 线程数 = 2*CPU数
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 入口
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * alibaba dubbo #adapter.getDecoder(); #adapter.getEncoder(); #nettyClientHandler
                             *
                             */
                            pipeline.addLast(new ObjectDecoder(1024,
                                    ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new NettyClientHandler(proxy, method, args, response));
                        }
                    });
            ChannelFuture future = bootstrap.connect(LOCALHOST, PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        return response;
    }
}
