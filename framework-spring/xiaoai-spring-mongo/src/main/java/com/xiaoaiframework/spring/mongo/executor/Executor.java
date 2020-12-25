package com.xiaoaiframework.spring.mongo.executor;


import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.List;

/**
 * mongo的執行器
 * @author edison
 */
public interface Executor {


    <E> List<E> find(Method method, Object[] objects, Class<E> entity);

    <E> E findOne(Method method,Object[] objects,Class<E> entity);

    <E> List<E> findAll(Class<E> entity);

    boolean save(Object entity);
}
