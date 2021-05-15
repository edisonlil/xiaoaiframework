package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * mongo接口的映射注解
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Mapping {

    /**
     * 主鍵類型
     * @return
     */
    Class<?> keyType() default String.class;


    /**
     * 實體類型
     * @return
     */
    Class<?> entityType();

}
