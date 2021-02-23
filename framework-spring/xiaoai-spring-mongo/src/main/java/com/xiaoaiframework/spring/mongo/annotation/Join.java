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


    /**
     * 主表关键字段
     * @return
     */
    String localField();

    /**
     * 从表关键字段
     * @return
     */
    String foreignField();


    String as();







}
