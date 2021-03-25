package com.xiaoaiframework.spring.rabbitmq.annotation;


import com.xiaoaiframework.spring.rabbitmq.constant.RpcType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author edison
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcClientMethod {

    /**
     * You can specify an names,
     * if the client and server method names do not match.
     * @return
     */
    String name() default "";

    RpcType type() default RpcType.REPLY;

    /**
     * 消息延时发送时间
     */
    long delayTime() default 0L;

    /**
     * 消息延时发送时间单位
     * @return
     */
    TimeUnit delayTimeUnit() default TimeUnit.MILLISECONDS;

}
