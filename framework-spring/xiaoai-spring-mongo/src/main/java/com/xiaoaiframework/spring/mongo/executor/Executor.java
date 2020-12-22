package com.xiaoaiframework.spring.mongo.executor;


import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * mongo的執行器
 * @author edison
 */
public interface Executor {

    <E> List<E> find(Query query, Class<E> entity);


    boolean save(Object entity);
}
