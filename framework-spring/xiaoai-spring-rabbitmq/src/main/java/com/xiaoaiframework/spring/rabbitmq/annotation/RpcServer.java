package com.xiaoaiframework.spring.rabbitmq.annotation;


import com.xiaoaiframework.spring.rabbitmq.constant.RpcType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author edison
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcServer {

    String value() default "";

    /**
     * Time To Live 消息存活时间
     * @return
     */
    int xMessageTTL() default 1000;

    int threadNum() default 1;

    /**
     * 支持的Rpc类型
     * @return
     */
    RpcType[] types() default {RpcType.REPLY, RpcType.DIRECT,RpcType.FANOUT};


}
