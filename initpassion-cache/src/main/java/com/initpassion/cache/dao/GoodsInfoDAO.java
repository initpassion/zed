/*
 * @(#) GoodsDAO.java 2019-05-14
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.initpassion.cache.bo.GoodsInfo;

/**
 * @author tushenghong01
 * @version 2019-05-14
 */

@Repository
public interface GoodsInfoDAO {

    int insert(GoodsInfo goodsInfo);

    int batchInsert(List<GoodsInfo> goodsInfoList);
}
