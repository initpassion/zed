/*
 * @(#) LongEventProducer.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.disruptor.office.demo;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * onData用来发布事件，每调用一次就发布一次事件事件
     *
     * 它的参数会通过事件传递给消费者
     *
     * @param bb
     */
    public void onData(ByteBuffer bb) {
        /**
         * 获取下一个数据槽的序号
         */
        long sequence = ringBuffer.next();
        try {
            /**
             * 根据序号拿到对应的槽
             */
            LongEvent event = ringBuffer.get(sequence);
            /**
             * 将值放到槽上
             */
            event.setValue(bb.getLong(0));
        } finally {
            /**
             * 发布数据
             */
            ringBuffer.publish(sequence);
        }
    }

}
