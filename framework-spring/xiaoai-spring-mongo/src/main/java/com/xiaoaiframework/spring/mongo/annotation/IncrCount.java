package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * 设置自增数
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface IncrCount {

}
