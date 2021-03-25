package com.xiaoaiframework.spring.rabbitmq.annotation;

import com.xiaoaiframework.spring.rabbitmq.configuration.RpcDeferredImportSelector;
import com.xiaoaiframework.spring.rabbitmq.constant.RpcMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author edison
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcDeferredImportSelector.class)
public @interface EnableSimpleRpc {

    RpcMode[] mode() default {RpcMode.RPC_CLIENT, RpcMode.RPC_SERVER};
}
