package com.xiaoaiframework.core.type;

import java.lang.reflect.Type;

/**
 * @author edison
 */
public abstract class JavaType<T> implements Type{

    Class<T> type;

    public JavaType(Class<T> type){
        this.type = type;
    }


    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public String getTypeName() {
        return type.getTypeName();
    }



}
