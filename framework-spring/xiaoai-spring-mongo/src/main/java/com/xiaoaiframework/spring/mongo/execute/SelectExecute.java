package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;

import java.lang.reflect.Method;

/**
 * 查询执行器
 * @author edison
 */
public interface SelectExecute {


    Object doSelect(Select select, Method method, Object[] objects);



}
