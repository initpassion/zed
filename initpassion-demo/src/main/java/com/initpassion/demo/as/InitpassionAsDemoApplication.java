/*
 * @(#) InitpassionAsDemoApplication.java 2018-12-05
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.initpassion.demo.as;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.log4j.Log4j2;

/**
 * @author tushenghong01
 * @version 2018-12-05
 */
@Log4j2
public class InitpassionAsDemoApplication {
    private static Random random = new Random();

    public int illegalArgumentCount = 0;

    private static final Map<String, String> cache = Maps.newHashMap();
    static {
        cache.put("age", "age");
        cache.put("user", "user");
        cache.put("pass", "pass");
    }

    public static void main(String[] args) throws InterruptedException {
        InitpassionAsDemoApplication application = new InitpassionAsDemoApplication();
        boolean run = true;
        while (run) {
            application.run();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void run() throws InterruptedException {
        try {
            int number = random.nextInt();
            List<Integer> primeFactors = primeFactors(number);
            print(number, primeFactors);

        } catch (Exception e) {
            System.out.println(String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage());
        }
    }

    public static void print(int number, List<Integer> primeFactors) {
        StringBuffer sb = new StringBuffer(number + "=");
        for (int factor : primeFactors) {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }

    public List<Integer> primeFactors(int number) {
        if (number < 2) {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }
        List<Integer> result = Lists.newArrayList();
        int i = 2;
        while (i <= number) {
            if (number % i == 0) {
                result.add(i);
                number = number / i;
                i = 2;
            } else {
                i++;
            }
        }
        return result;
    }
}
