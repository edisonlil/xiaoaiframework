package com.xiaoaiframework.spring.mongo.context;

import java.lang.reflect.Method;

public abstract class MongoContext {

    Method method;

    Object[] objects;

    Class entityType;


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }


    public Class getEntityType() {
        return entityType;
    }

    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }
}
