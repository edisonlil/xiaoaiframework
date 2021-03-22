package com.xiaoaiframework.core;

/**
 * 备忘录接口
 * @author edc
 */
public interface Memorandum<T> {

    /**
     * 提供一个备份接口
     */
    void backup();


    /**
     * 提供一个回退接口
     */
    void rollback();
}
