package com.xiaoaiframework.spring.mongo.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class SimpleExecutor implements Executor {

    @Autowired
    MongoTemplate template;



    @Override
    public <E> List<E> find(Query query,Class<E> entity) {
        return template.find(query,entity);
    }


    @Override
    public boolean save(Object entity){
        template.save(entity);
        return true;
    }

}
