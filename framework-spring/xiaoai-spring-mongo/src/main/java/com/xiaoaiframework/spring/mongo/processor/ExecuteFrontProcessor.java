package com.xiaoaiframework.spring.mongo.processor;

import java.lang.reflect.Method;

/**
 * 执行前置处理器
 * @author edison
 */
public interface ExecuteFrontProcessor {

    void frontProcessor(Object o, Method method, Object[] objects);

}
