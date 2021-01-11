package com.xiaoaiframework.spring.mongo.processor;

import org.springframework.data.mongodb.core.query.Query;


/**
 * 查询操作前置处理器
 */
public interface SelectProcessor{

    void frontProcessor(Query query, Class entity);

    /**
     *
     * @param query
     * @param entity
     * @param result  If the result is a collection
     *                then the primitive type is the element type in the collection,
     *                otherwise it should be the same as the result type
     * @param rawType
     */
    void postProcessor(Query query, Class entity,
                       Object result, Class rawType);
}
