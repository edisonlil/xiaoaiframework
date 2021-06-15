package com.xiaoaiframework.spring.mongo.annotation.action;

import java.lang.annotation.*;

/**
 * 连表
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
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
