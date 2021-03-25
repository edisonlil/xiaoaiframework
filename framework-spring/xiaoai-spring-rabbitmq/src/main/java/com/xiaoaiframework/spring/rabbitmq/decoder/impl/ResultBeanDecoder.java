package com.xiaoaiframework.spring.rabbitmq.decoder.impl;

import com.alibaba.fastjson.JSON;
import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.spring.rabbitmq.decoder.Decoder;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ResultBeanDecoder implements Decoder {
    @Override
    public Object decode(String message, Type type) {

        if(type != ResultBean.class) {
            return null;
        }
        Class c = (Class) type;
        ResultBean resultBean = JSON.parseObject(message, ResultBean.class);

        Type genericType = c.getGenericInterfaces()[0];
        if(genericType instanceof ParameterizedType){
            Type[] resultGenericType = ((ParameterizedType) genericType).getActualTypeArguments();
            Object resultData = resultBean.getData();
            if (resultData != null) {
                if(resultGenericType[0].getTypeName().equals("java.lang.String")){
                    resultBean.setData(resultData.toString());
                }else{
                    resultBean.setData(JSON.parseObject(resultData.toString(), resultGenericType[0]));
                }

            }
        }
        return resultBean;


    }
}
