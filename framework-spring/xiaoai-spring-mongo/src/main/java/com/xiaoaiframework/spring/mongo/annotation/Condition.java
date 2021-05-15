package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * 條件查詢注解
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Condition {
}
