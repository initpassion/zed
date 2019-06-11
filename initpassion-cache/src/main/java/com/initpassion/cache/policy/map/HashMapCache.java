/*
 * @(#) HashMapCache.java 2019-06-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy.map;

import com.google.common.collect.Maps;
import com.initpassion.cache.bo.GoodsInfo;

import java.util.Map;

/**
 * @author initpassion
 * @version 2019-06-11
 */
public class HashMapCache {


    private static final Map<String, GoodsInfo> cache = Maps.newHashMap();


    public static void add(GoodsInfo info){
        cache.put(info.getGoodsCode(), info);
    }

    public static void remove(String code){
        cache.remove(code);
    }

    public static GoodsInfo get(String code){
        return cache.get(code);
    }
}

