/*
 * @(#) GoodsInfoService.java 2019-05-14
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.service;

import com.github.pagehelper.Page;
import com.initpassion.cache.bo.GoodsInfo;

/**
 * @author tushenghong01
 * @version 2019-05-14
 */
public interface GoodsInfoService {

    boolean insert(GoodsInfo goodsInfo);

    /**
     * code查询
     * 
     * @param goodCode
     * @return
     */
    GoodsInfo getByGoodCode(String goodCode);

    Page<GoodsInfo> pageQuery(Integer pageNum, Integer pageSize);
}
