package com.franklions.finance.service;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-11-29
 * @since Jdk 1.8
 */
public class RequestUseTime {
    public static final ThreadLocal<Long> threadStartTime = new ThreadLocal<>();
}
