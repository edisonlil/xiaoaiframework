package com.xiaoaiframework.spring.mongo.proxy;

import com.alibaba.fastjson.JSON;
import com.xiaoaiframework.spring.mongo.annotation.Delete;
import com.xiaoaiframework.spring.mongo.annotation.Save;
import com.xiaoaiframework.spring.mongo.annotation.Select;
import com.xiaoaiframework.spring.mongo.annotation.Update;
import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    Executor executor;


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        
        Annotation annotation = AnnotationUtils.findAnnotation(method,Select.class);
        if(annotation instanceof Select){
            select((Select) annotation,method,objects);
        }else if(annotation instanceof Save){
            
            assert objects.length == 1;

            Object val = objects[0];
            if (entityType != val.getClass()) {
                Object entity = ReflectUtil.newInstance(entityType);
                ObjectUtil.copyProperties(val, entity);
            }
            return executor.save(val);
        }else if(annotation instanceof Update){
            throw new RuntimeException("Not implemented update");
        }else if(annotation instanceof Delete){
            throw new RuntimeException("Not implemented del");
        }
        throw new RuntimeException("The method prefix definition cannot be found");

    }


    private Object select(Select select,Method method,Object[] objects){

        List<?> list = null;
        if(objects == null || objects.length == 0){
            list = executor.findAll(entityType);
        }else if(CollUtil.isColl(method.getReturnType())){
            list = executor.find(method,objects,entityType);
        }

        
        if(list == null || list.isEmpty()) {
            return null;
        }
        
        if(method.getReturnType() == entityType){
            return list.get(0);
        }

        Class rawClass = select.rawType() != null? select.rawType() : entityType;

        if(CollUtil.isColl(method.getReturnType()) && rawClass == entityType){
            return list;
        }else if(method.getReturnType().isArray() && rawClass == entityType){
            return list.toArray();
        }

        if(method.getReturnType().isArray()){
            return JSON.parseArray(JSON.toJSONString(list),rawClass).toArray();
        }else if(CollUtil.isColl(method.getReturnType())){
            return JSON.parseArray(JSON.toJSONString(list),rawClass);
        }else{
            Object data = ReflectUtil.newInstance(rawClass);
            ObjectUtil.copyProperties(list.get(0),data);
            return data;
        }

    }



    public Class<?> getKeyType() {
        return keyType;
    }

    public void setKeyType(Class<?> keyType) {
        this.keyType = keyType;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
