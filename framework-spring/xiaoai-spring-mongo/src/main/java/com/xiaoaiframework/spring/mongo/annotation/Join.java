package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * 待实现...  
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Join {

    /**
     * 从表实体类
     * @return
     */
    Class slave();


    //where {id = id,};
    String where();








}
