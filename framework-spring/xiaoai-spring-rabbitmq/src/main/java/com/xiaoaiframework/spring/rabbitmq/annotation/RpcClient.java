package com.xiaoaiframework.spring.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * @author edison
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcClient {


    String value();

    int replyTimeout() default 2000;

    int maxAttempts() default 3;

}
