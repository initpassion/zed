/*
 * @(#) ApiBaseResponse.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.api.base;

import java.io.Serializable;

import lombok.Data;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Data
public class ApiBaseResponse<T> implements Serializable {

    private int code;

    private String message;

    private T data;

}
