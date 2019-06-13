/*
 * @(#) CaffineCacheManager.java 2019-06-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy.caffeine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.pagehelper.Page;
import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.policy.Cache;
import com.initpassion.cache.service.GoodsInfoService;

/**
 * @author initpassion
 * @version 2019-06-11
 */
@Component
public class CaffeineCacheManager implements Cache {

    private LoadingCache<String, GoodsInfo> cache;

    @Resource
    private GoodsInfoService goodsInfoService;

    @PostConstruct
    @Override
    public void init() {
        cache = Caffeine.newBuilder()
                // 最大缓存10000个对象
                .maximumSize(10000)
                // 1分钟后缓存失效
                .expireAfterWrite(1, TimeUnit.MINUTES)
                // 使用SoftReference对象封装value, 使得内存不足时，自动回收
                .softValues()
                // 定义缓存对象失效的时间精度为纳秒级
                .ticker(Ticker.systemTicker())
                // 数据的加载
                .build(goodCode -> goodsInfoService.getByGoodCode(goodCode));
        // 第一次启动全部加载
        Page<GoodsInfo> page = goodsInfoService.pageQuery(1, 0);
        if (Objects.nonNull(page)) {
            page.getResult().stream().map(GoodsInfo::getGoodsCode).forEach(this::get);
        }
    }

    @Override
    public GoodsInfo get(String code) {
        return cache.get(code);
    }
}
