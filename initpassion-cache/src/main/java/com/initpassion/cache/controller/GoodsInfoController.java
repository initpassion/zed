/*
 * @(#) GoodsInfoController.java 2019-05-14
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.policy.guava.GuavaCacheManager;
import com.initpassion.cache.service.GoodsInfoService;

import java.util.Objects;

/**
 * @author tushenghong01
 * @version 2019-05-14
 */
@RestController
@RequestMapping(value = "/api/goods")
public class GoodsInfoController {

    @Resource
    private GoodsInfoService goodsInfoService;

    @Resource
    private GuavaCacheManager cacheManager;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insert(@RequestBody GoodsInfo goodsInfo) {
        goodsInfoService.insert(goodsInfo);
    }

    @RequestMapping(value = "pageQuery")
    public void pageQuery(Integer pageNum, Integer pageSize) {
        if (Objects.isNull(pageSize)){
            //查全部
            pageSize = 0;
        }
        Page<GoodsInfo> page = goodsInfoService.pageQuery(pageNum, pageSize);
        page.getResult().stream().map(GoodsInfo::getGoodsCode).forEach(cacheManager::get);
    }
}
