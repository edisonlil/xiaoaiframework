package com.xiaoaiframework.spring.rabbitmq.decoder.impl;


import com.alibaba.fastjson.JSON;
import com.xiaoaiframework.spring.rabbitmq.decoder.Decoder;

import java.lang.reflect.Type;

public class SimpleDecoder implements Decoder {



    @Override
    public Object decode(String message, Type type) {


        assertType(type);

        Class c = (Class) type;

        if(isBasicType(c)){
            return message;
        }else if(!JSON.isValid(message)){
            return message;
        }


        return JSON.parseObject(message);
    }

    public void assertType(Type type){
        assert type instanceof Class;
    }

    public boolean isBasicType(Class c){
        return c.isPrimitive();
    }
}
