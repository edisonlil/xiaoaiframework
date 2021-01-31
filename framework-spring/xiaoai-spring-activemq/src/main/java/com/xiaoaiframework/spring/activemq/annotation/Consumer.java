package com.xiaoaiframework.spring.activemq.annotation;

import java.lang.annotation.*;

/**
 * @author edison
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Consumer {

    String id() default "";

}
