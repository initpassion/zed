/*
 * @(#) HashMapCacheManager.java 2019-06-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy.map;

import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.policy.Cache;
import com.initpassion.cache.service.GoodsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author initpassion
 * @version 2019-06-11
 */
@Component
@Slf4j
public class HashMapCacheManager implements Cache {

    /**
     * 默认更新间隔为1分钟
     */
    private static int DEFAULT_SCHEDULE_INTERVAL = 1;
    /**
     * 默认的线程数
     */
    private static int DEFAULT_SCHEDULE_THREAD_NUM = 1;

    private ScheduledExecutorService exec;

    private int scheduleInterval;

    @Resource
    private GoodsInfoService goodsInfoService;


    public HashMapCacheManager() {
        this(DEFAULT_SCHEDULE_INTERVAL);
    }

    public HashMapCacheManager(int scheduleInterval) {
        this.scheduleInterval = scheduleInterval > 0 ? scheduleInterval : DEFAULT_SCHEDULE_INTERVAL;
    }

    @Override
    @PostConstruct
    public void init(){
        log.info("HashMapCache Task refresh begin...");
        refresh(); // 第一次同步加载
        this.exec = Executors.newScheduledThreadPool(DEFAULT_SCHEDULE_THREAD_NUM, new ThreadFactory() {
            private AtomicInteger num = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("HashMapCacheRefresh-" + num.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
        // 设置当任务取消的时候移除
        if (this.exec instanceof ScheduledThreadPoolExecutor) {
            ((ScheduledThreadPoolExecutor) this.exec).setRemoveOnCancelPolicy(true);
        }
        schedule();
    }

    /**
     * 固定周期进行任务调度，注意task中不能抛出异常，并且当执行时间超过周期时，执行时间会延时不会多线程同时调用
     */
    public ScheduledFuture<?> schedule() {
        Runnable task = () -> {
            log.info("starting refresh goodInfo");
            try {
                refresh();
            } catch (Exception e) {
                // 捕捉异常，防止定时任务由于异常退出
                log.error("scheduled work run exception", e);
            }
        };
        ScheduledFuture<?> future = exec.scheduleWithFixedDelay(task, scheduleInterval, scheduleInterval,
                TimeUnit.MINUTES);
        return future;
    }

    public void refresh(){
        Page<GoodsInfo> page = goodsInfoService.pageQuery(1, 0);
        if (Objects.nonNull(page)) {
            page.getResult().stream().forEach(HashMapCache::add);
        }

    }

    @Override
    public GoodsInfo get(String code) {
        return HashMapCache.get(code);
    }
}



