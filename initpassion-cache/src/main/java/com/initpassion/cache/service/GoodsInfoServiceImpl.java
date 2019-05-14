/*
 * @(#) GoodsInfoServiceImpl.java 2019-05-14
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.dao.GoodsInfoDAO;

/**
 * @author tushenghong01
 * @version 2019-05-14
 */

@Service
public class GoodsInfoServiceImpl implements GoodsInfoService {

    @Resource
    private GoodsInfoDAO goodsInfoDAO;

    @Override
    public boolean insert(GoodsInfo goodsInfo) {
        return goodsInfoDAO.insert(goodsInfo) > 0;
    }
}
