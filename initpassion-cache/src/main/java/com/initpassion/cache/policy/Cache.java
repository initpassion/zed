/*
 * @(#) Cache.java 2019-06-11
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.policy;

import com.initpassion.cache.bo.GoodsInfo;

/**
 * @author initpassion
 * @version 2019-06-11
 */
public interface Cache {
    /**
     * 初始化, 同步或异步加载全量数据
     */
    void init();

    /**
     * code查询
     * @param code
     * @return
     */
    GoodsInfo get(String code);
}
