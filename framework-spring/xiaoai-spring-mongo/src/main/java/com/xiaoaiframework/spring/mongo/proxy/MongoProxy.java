package com.xiaoaiframework.spring.mongo.proxy;

import com.alibaba.fastjson.JSON;
import com.xiaoaiframework.spring.mongo.annotation.*;
import com.xiaoaiframework.spring.mongo.annotation.Set;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.*;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.annotation.Annotation;
import java.util.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    ConditionParsing parsing;

    MongoTemplate template;


    List<ExecuteProcessor> executeFrontProcessors;

    List<UpdateProcessor> updateFrontProcessors;

    List<SaveProcessor> saveFrontProcessor;

    List<SelectProcessor> selectFrontProcessor;

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
            template.save(val);
            return true;
        }

        List list = new ArrayList();
        list.addAll((Collection) val);

        if (list.isEmpty()) {
            return true;
        }

        saveFrontProcessors(list.toArray());
        if (list.size() == 1) {
            template.save(list.get(0));
            return true;
        }

        list.forEach(item -> {
            template.save(item);
        });

        return true;
    }


    private Object select(Select select, Method method, Object[] objects) {

        Object data = null;
        List<?> list = null;
        Query query = parsing.getQuery(method, objects);
        selectFrontProcessors(query,entityType);
        if (CollUtil.isColl(method.getReturnType())) {
            list = template.find(query, entityType);
        }else if(ObjectUtil.isNumber(method.getReturnType())){
            data = template.count(query,entityType);
        }

        if (list == null || list.isEmpty()) {
            return null;
        }

        Class rawClass = select.rawType() != Void.class ? select.rawType() : entityType;
        if (method.getReturnType() == entityType) {
            data = list.get(0);
        }else{

            if (CollUtil.isColl(method.getReturnType()) && rawClass == entityType) {
                data = list;
            } else if (method.getReturnType().isArray() && rawClass == entityType) {
                data = list.toArray();
            }else if (CollUtil.isMap(rawClass) && CollUtil.isMap(rawClass)) {
                data = BeanUtil.beanToMap(list.get(0));
            }else if (method.getReturnType().isArray()) {
                data = JSON.parseArray(JSON.toJSONString(list), rawClass).toArray();
            } else if (CollUtil.isColl(method.getReturnType())) {
                data = JSON.parseArray(JSON.toJSONString(list), rawClass);
            } else {
                data = ReflectUtil.newInstance(rawClass);
                ObjectUtil.copyProperties(list.get(0), data);
            }

        }

        selectPostProcessors(data,rawClass);
        return data;
    }


    public Object delete(Method method, Object[] objects) {

        if (objects == null) {
            return false;
        }
        return template.remove(parsing.getQuery(method, objects), entityType).wasAcknowledged();

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

        org.springframework.data.mongodb.core.query.Update u = parsing.convertUpdate(update);
        Query query = parsing.getQuery(method, objects);
        updateFrontProcessors(u,query,entityType);

        return template.updateFirst(query
                ,u, entityType).wasAcknowledged();
    }


    public void setKeyType(Class<?> keyType) {
        this.keyType = keyType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }


    public void setTemplate(MongoTemplate template) {
        this.template = template;
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


    void selectPostProcessors(Object result,Class rawType) {
        List<SelectProcessor> processors = selectFrontProcessor;

        if (processors == null || processors.isEmpty()) {
            return;
        }

        for (SelectProcessor processor : processors) {
            processor.postProcessor(result,rawType);
        }
    }

}
