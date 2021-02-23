package com.xiaoaiframework.spring.mongo.proxy;

import com.xiaoaiframework.spring.mongo.chain.ConditionParserChain;
import com.xiaoaiframework.spring.mongo.context.QuerySelectContext;
import com.xiaoaiframework.spring.mongo.context.UpdateContext;
import com.xiaoaiframework.spring.mongo.execute.MongoExecute;
import com.xiaoaiframework.spring.mongo.annotation.action.Delete;
import com.xiaoaiframework.spring.mongo.annotation.action.Save;
import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.annotation.action.Update;
import com.xiaoaiframework.spring.mongo.processor.*;
import com.xiaoaiframework.spring.mongo.service.SelectService;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.query.Query;
import java.util.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author edison
 */
public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    MongoExecute execute;

    List<ExecuteProcessor> executeFrontProcessors;

    List<UpdateProcessor> updateFrontProcessors;

    List<SaveProcessor> saveFrontProcessor;

    SelectService selectService;

    ConditionParserChain chain;


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
        return selectService.doSelect(select,method,objects,entityType);
    }


    public Object delete(Method method, Object[] objects) {

        if (objects == null) {
            return false;
        }

        QuerySelectContext context = new QuerySelectContext();
        context.setMethod(method);
        context.setObjects(objects);
        chain.doParsing(context);

        return execute.remove(context.getQuery(), entityType);

    }


    public Object update(Method method, Object[] objects) {

        if (objects == null) {
            return false;
        }

        UpdateContext context = new UpdateContext();
        context.setMethod(method);
        context.setObjects(objects);
        chain.doParsing(context);

        if(context.getUpdate() == null){
            return false;
        }

        org.springframework.data.mongodb.core.query.Update u =
                context.getUpdate();

        Query query = context.getQuery();
        updateFrontProcessors(u,query,entityType);

        return execute.updateFirst(query
                ,u, entityType);
    }


    public void setChain(ConditionParserChain chain) {
        this.chain = chain;
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

    public void setSelectService(SelectService selectService) {
        this.selectService = selectService;
    }



    public void setExecuteFrontProcessors(List<ExecuteProcessor> executeFrontProcessors) {
        this.executeFrontProcessors = executeFrontProcessors;
    }

    public void setSaveFrontProcessor(List<SaveProcessor> saveFrontProcessor) {
        this.saveFrontProcessor = saveFrontProcessor;
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



}
