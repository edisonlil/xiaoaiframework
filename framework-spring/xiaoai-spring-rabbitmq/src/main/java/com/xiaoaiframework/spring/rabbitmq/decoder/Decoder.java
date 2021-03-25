package com.xiaoaiframework.spring.rabbitmq.decoder;


import java.lang.reflect.Type;

/**
 * 解码器
 * @author edison
 */
public interface Decoder<T> {

    T decode(String message, Type type);

    

}
