package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * 更新接口专用,设置可更新字段
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Set {
}
