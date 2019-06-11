/*
 * @(#) GuavaCacheManager.java 2019-06-04
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy.guava;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Component;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.service.GoodsInfoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tushenghong01
 * @version 2019-06-04
 */
@Component
@Slf4j
public class GuavaCacheManager {

    @Resource
    private GoodsInfoService goodsInfoService;

    private LoadingCache<String, GoodsInfo> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                // 最大缓存10000个对象
                .maximumSize(10000)
                // 1分钟后缓存失效
                .expireAfterWrite(1, TimeUnit.MINUTES)
                // 使用SoftReference对象封装value, 使得内存不足时，自动回收
                .softValues()
                // 定义缓存对象失效的时间精度为纳秒级
                .ticker(Ticker.systemTicker())
                // 数据的加载
                .build(new CacheLoader<String, GoodsInfo>() {
                    @Override
                    public GoodsInfo load(String goodCode) {
                        return goodsInfoService.getByGoodCode(goodCode);
                    }
                });
        //第一次启动全部加载

        Page<GoodsInfo> page = goodsInfoService.pageQuery(1, 0);
        if (Objects.nonNull(page)){
            page.getResult().stream().map(GoodsInfo::getGoodsCode).forEach(this::getByCode);
        }
    }

    /**
     * code查询
     * 
     * @param code
     * @return
     */
    public GoodsInfo getByCode(String code) {
        return cache.getUnchecked(code);
    }
}
