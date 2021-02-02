package com.xiaoaiframework.spring.mongo.base;

import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.spring.log4j2.annotation.LogAspect;

/**
 * @author edison
 * @param <ID>
 * @param <E>
 */
public interface BaseService<ID,E> {


    E getById(ID id);


    ResultBean add(E t);


    ResultBean update(ID id, E t);


    ResultBean delete(ID id);
}
