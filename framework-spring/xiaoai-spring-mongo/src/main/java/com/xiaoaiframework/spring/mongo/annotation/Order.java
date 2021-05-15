package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * 排序
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD,ElementType.PARAMETER})
public @interface Order  {
}
