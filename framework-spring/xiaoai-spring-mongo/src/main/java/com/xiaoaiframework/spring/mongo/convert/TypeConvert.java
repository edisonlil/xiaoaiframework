package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.spring.mongo.type.JavaType;


/**
 * 类型转换器
 * @author edison
 */
public interface TypeConvert<T> {

    /**
     * 类型转换
     * @param data
     * @param type
     * @return
     */
    T convert(Object data, JavaType<T> type);


    /**
     * 是否能够转换
     * @param type
     * @return
     */
    boolean match(JavaType type);

    /*
           数字类型
           字符串类型
           对象类型
           数组类型
     */



}
