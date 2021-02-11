package com.xiaoaiframework.spring.mongo.convert;

/**
 * @author edison
 */
public abstract class GenericTypeConvert<T> implements TypeConvert<T> {

    Class genericType;

    public Class getGenericType() {
        return genericType;
    }

    public void setGenericType(Class genericType) {
        this.genericType = genericType;
    }
}
