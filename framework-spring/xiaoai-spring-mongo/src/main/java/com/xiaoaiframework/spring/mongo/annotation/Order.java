package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD,ElementType.PARAMETER})
public @interface Order  {
}
