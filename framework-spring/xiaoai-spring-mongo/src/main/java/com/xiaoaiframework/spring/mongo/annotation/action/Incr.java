package com.xiaoaiframework.spring.mongo.annotation.action;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface Incr {

    String name();


    int incrCount() default 1;
}
