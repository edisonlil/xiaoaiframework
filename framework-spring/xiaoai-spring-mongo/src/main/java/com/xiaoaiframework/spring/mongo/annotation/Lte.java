package com.xiaoaiframework.spring.mongo.annotation;

import com.xiaoaiframework.spring.mongo.constant.ActionType;

import java.lang.annotation.*;

import static com.xiaoaiframework.spring.mongo.constant.ActionType.AND;

/**
 * 小于等于
 * @author edison
 */
@Documented
@ICriteria
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Lte {

    /**
     * 是否忽略空值判斷
     */
    boolean ignoreNull() default true;

    /**
     * 字段名
     * @return
     */
    String name() default "";

    /**
     * 該條件為 and 還是 or
     * @return
     */
    ActionType action() default AND;
}
