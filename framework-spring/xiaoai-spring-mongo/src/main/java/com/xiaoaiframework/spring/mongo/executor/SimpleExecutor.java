package com.xiaoaiframework.spring.mongo.executor;

import com.xiaoaiframework.spring.mongo.parsing.QueryParsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class SimpleExecutor implements Executor {

    @Autowired
    MongoTemplate template;

    @Autowired
    QueryParsing queryParsing;


    @Override
    public <E> List<E> find(Method method, Object[] objects, Class<E> entity) {
        Query query = getQuery(method, objects);
        return template.find(query,entity);
    }

    @Override
    public <E> E findOne(Method method, Object[] objects, Class<E> entity) {
        Query query = getQuery(method, objects);
        return template.findOne(query,entity);
    }

    @Override
    public <E> List<E> findAll(Class<E> entity) {
        return template.findAll(entity);
    }

    @Override
    public boolean save(Object entity){
        template.save(entity);
        return true;
    }


    Query getQuery(Method method, Object[] objects){

        Annotation[][] annotations = method.getParameterAnnotations();

        Query query = new Query();
        for (int i = 0; i < objects.length; i++) {
            Annotation annotation = annotations[i][0];
            query = queryParsing.parsing(query,annotation,objects[i]);
        }

        return query;
    }
}
