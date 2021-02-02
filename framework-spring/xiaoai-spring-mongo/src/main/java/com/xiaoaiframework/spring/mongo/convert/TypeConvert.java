package com.xiaoaiframework.spring.mongo.convert;

/**
 * 类型转换器
 * @author edison
 */
public interface TypeConvert<T> {

    /**
     * 类型转换
     * @param data
     * @param target
     * @return
     */
    T convert(Object data,Class target);


    /**
     * 是否能够转换
     * @param data
     * @param target
     * @return
     */
    boolean canConvert(Object data,Class target);

    /*
           数字类型
           字符串类型
           对象类型
           数组类型
     */



}
