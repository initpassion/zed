/*
 * @(#) GoodsInfo.java 2019-05-14
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.cache.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tushenghong01
 * @version 2019-05-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo implements Serializable {
    private Long id;
    private String goodsName;
    private String goodsCode;
    private Double goodsPrice;
    private String goodsDesc;
    private Integer goodsStatus;
    private Long createTime;
    private Long updateTime;
    private String creator;
    private String operator;
}
