/*
 * @(#) HttpMethodEnum.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.common.enums;

import lombok.Getter;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Getter
public enum HttpMethodEnum {
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),
    UPDATE("UPDATE");
    private String method;

    HttpMethodEnum(String method) {
        this.method = method;
    }
}
