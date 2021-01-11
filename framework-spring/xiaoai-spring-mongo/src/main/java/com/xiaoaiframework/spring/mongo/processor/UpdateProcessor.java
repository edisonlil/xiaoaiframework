package com.xiaoaiframework.spring.mongo.processor;


import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * 更新操作前置处理器
 */
public interface UpdateProcessor {

    void frontProcessor(Update update, Query query,Class entity);
}
