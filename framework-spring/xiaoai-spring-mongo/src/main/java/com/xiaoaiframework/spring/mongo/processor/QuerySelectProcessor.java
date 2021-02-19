package com.xiaoaiframework.spring.mongo.processor;

import org.springframework.data.mongodb.core.query.Query;


/**
 * 查询操作前置处理器
 */
public interface QuerySelectProcessor{

    void frontProcessor(Query query, Class entity);

    /**
     *
     * @param result  If the result is a collection
     *                then the primitive type is the element type in the collection,
     *                otherwise it should be the same as the result type
     * @param rawType
     * @return result
     */
    Object postProcessor(Object result, Class rawType);
}
