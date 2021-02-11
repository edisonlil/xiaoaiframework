package com.xiaoaiframework.spring.mongo.proxy;

import com.xiaoaiframework.spring.mongo.MongoExecute;
import com.xiaoaiframework.spring.mongo.annotation.Set;
import com.xiaoaiframework.spring.mongo.annotation.action.Delete;
import com.xiaoaiframework.spring.mongo.annotation.action.Save;
import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.annotation.action.Update;
import com.xiaoaiframework.spring.mongo.convert.ConvertRegistrar;
import com.xiaoaiframework.spring.mongo.convert.GenericTypeConvert;
import com.xiaoaiframework.spring.mongo.convert.OtherConvert;
import com.xiaoaiframework.spring.mongo.convert.TypeConvert;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.*;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import com.xiaoaiframework.util.type.TypeUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.annotation.Annotation;
import java.util.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author edison
 */
public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    ConditionParsing parsing;

    MongoExecute execute;


    List<ExecuteProcessor> executeFrontProcessors;

    List<UpdateProcessor> updateFrontProcessors;

    List<SaveProcessor> saveFrontProcessor;

    List<SelectProcessor> selectFrontProcessor;

    ConvertRegistrar convertRegistrar;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        //方法操作前(扩展点)
        executeFrontProcessors(o, method, objects);

        Select select = AnnotationUtils.getAnnotation(method, Select.class);
        if (select != null) {
            return select(select, method, objects);
        }

        Save save = AnnotationUtils.getAnnotation(method, Save.class);
        if (save != null) {
            assert objects.length == 1;
            Object val = objects[0];
            return save(val);
        }

        Update update = AnnotationUtils.getAnnotation(method, Update.class);
        if (update != null) {
           update(method, objects);
        }


        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        if (delete != null) {
            return delete(method, objects);
        }
        throw new RuntimeException("The method prefix definition cannot be found");

    }


    private Object save(Object val) {

        if (!CollUtil.isColl(val)) {
            saveFrontProcessors(new Object[]{val});
            execute.save(val,entityType);
            return true;
        }else{
            List list = CollUtil.newArrayList((Collection) val);
            saveFrontProcessors(list.toArray());
            execute.saveBatch(list,entityType);
        }

        return true;
    }


    private Object select(Select select, Method method, Object[] objects) {

        Object data;

        Query query = parsing.getQuery(method, objects);
        selectFrontProcessors(query,entityType);

        if (CollUtil.isColl(method.getReturnType()) && objects.length == 0){
            data = execute.findAll(entityType);
        }else if (CollUtil.isColl(method.getReturnType())) {
            data = execute.find(query, entityType);
        }else if(ObjectUtil.isNumber(method.getReturnType())){
            data = execute.count(query,entityType);
        }else{
            data = execute.findOne(query,entityType);
        }

        if (ObjectUtil.isEmpty(data)) {
            return data;
        }


        Class rawClass = select.rawType() != Void.class ? select.rawType() : entityType;
        data = returnTypeConvert(data,method,rawClass);

        return selectPostProcessors(data,rawClass);

    }


    public Object delete(Method method, Object[] objects) {

        if (objects == null) {
            return false;
        }
        return execute.remove(parsing.getQuery(method, objects), entityType);

    }


    public Object update(Method method, Object[] objects) {

        if (objects == null) {
            return false;
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        Object update = null;
        for (int i = 0; i < objects.length; i++) {
            Annotation annotation = annotations[i][0];
            if (annotation instanceof Set) {
                update = objects[i];
                break;
            }
        }

        if (update == null) {
            return true;
        }

        org.springframework.data.mongodb.core.query.Update u =
                parsing.convertUpdate(update);
        Query query = parsing.getQuery(method, objects);
        updateFrontProcessors(u,query,entityType);

        return execute.updateFirst(query
                ,u, entityType);
    }


    public void setExecute(MongoExecute execute) {
        this.execute = execute;
    }

    public void setKeyType(Class<?> keyType) {
        this.keyType = keyType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }


    public void setParsing(ConditionParsing parsing) {
        this.parsing = parsing;
    }

    public void setExecuteFrontProcessors(List<ExecuteProcessor> executeFrontProcessors) {
        this.executeFrontProcessors = executeFrontProcessors;
    }

    public void setSaveFrontProcessor(List<SaveProcessor> saveFrontProcessor) {
        this.saveFrontProcessor = saveFrontProcessor;
    }

    public void setSelectFrontProcessor(List<SelectProcessor> selectFrontProcessor) {
        this.selectFrontProcessor = selectFrontProcessor;
    }

    public void setUpdateFrontProcessors(List<UpdateProcessor> updateFrontProcessors) {
        this.updateFrontProcessors = updateFrontProcessors;
    }

    void executeFrontProcessors(Object o, Method method, Object[] objects) {
        List<ExecuteProcessor> processors = executeFrontProcessors;

        if (processors == null || processors.isEmpty()) {
            return;
        }

        for (ExecuteProcessor processor : processors) {
            processor.frontProcessor(o, method, objects);
        }
    }

    void updateFrontProcessors(org.springframework.data.mongodb.core.query.Update update, Query query,Class entity) {
        List<UpdateProcessor> processors = updateFrontProcessors;

        if (processors == null || processors.isEmpty()) {
            return;
        }

        for (UpdateProcessor processor : processors) {
            processor.frontProcessor(update, query, entity);
        }
    }

    void saveFrontProcessors(Object[] data) {
        List<SaveProcessor> processors = saveFrontProcessor;

        if (processors == null || processors.isEmpty()) {
            return;
        }

        for (SaveProcessor processor : processors) {
            processor.frontProcessor(data);
        }
    }

    void selectFrontProcessors(Query query,Class entityType) {
        List<SelectProcessor> processors = selectFrontProcessor;

        if (processors == null || processors.isEmpty()) {
            return;
        }

        for (SelectProcessor processor : processors) {
            processor.frontProcessor(query, entityType);
        }
    }


    Object selectPostProcessors(Object result,Class rawType) {
        List<SelectProcessor> processors = selectFrontProcessor;

        if (processors == null || processors.isEmpty()) {
            return result;
        }

        for (SelectProcessor processor : processors) {
           result = processor.postProcessor(result,rawType);
        }

        return result;
    }

    public void setConvertRegistrar(ConvertRegistrar convertRegistrar) {
        this.convertRegistrar = convertRegistrar;
    }

    Object returnTypeConvert(Object data, Method method, Class rawType){

        TypeConvert convert = convertRegistrar.getConvert(method.getReturnType());


        if(convert != null){

            if(convert instanceof GenericTypeConvert){
                ((GenericTypeConvert)convert).setGenericType(rawType);
            }

            return convert.convert(data, method.getReturnType());
        }
        return new OtherConvert().convert(data,method.getReturnType());
    }
}
