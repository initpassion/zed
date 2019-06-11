/*
 * @(#) CaffineCacheManager.java 2019-06-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy.caffeine;

import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.policy.Cache;
import org.springframework.stereotype.Component;

/**
 * @author initpassion
 * @version 2019-06-11
 */
@Component
public class CaffeineCacheManager implements Cache {
    @Override public void init() {
        
    }

    @Override public GoodsInfo get(String code) {
        return null;
    }
}
