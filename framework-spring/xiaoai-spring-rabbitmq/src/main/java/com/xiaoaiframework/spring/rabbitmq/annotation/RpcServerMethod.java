package com.xiaoaiframework.spring.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * @author edsion
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcServerMethod {

    String value() default "";

}
