package com.xiaoaiframework.spring.mongo.convert;

/**
 * 类型转换器
 * @author edison
 */
public interface TypeConvert<I,O> {

    /**
     * 类型转换
     * @param data
     * @param target
     * @return
     */
    O convert(I data,Class target);


    boolean canConvert(I data,Class target);

    /*
           数字类型
           字符串类型
           对象类型
           数组类型
     */



}
