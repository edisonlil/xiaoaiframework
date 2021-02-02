package com.xiaoaiframework.spring.mongo;

import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.bean.BeanUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


/**
 * Mongo执行器
 * @author edison
 */
public class MongoExecute {

    MongoTemplate template;

    public MongoExecute(MongoTemplate template){
        this.template = template;
    }

    public <T>T findOne(Query query, Class<T> type){
       return template.findOne(query,type);
    }

    <T> List<T> findAll(Class<T> type){
        return template.findAll(type);
    }

    public <T> List<T> find(Query query, Class<T> type){
        return template.find(query, type);
    }

    public Long count(Query query, Class type){
        return template.count(query, type);
    }


    public boolean save(Object data,Class type){

        if(!type.equals(data.getClass())){
            Object raw = ReflectUtil.newInstance(type);
            ObjectUtil.copyProperties(data,raw);
            data = raw;
        }
        template.save(data);

        return true;
    }


    public boolean updateFirst(Query query,Update update,Class type){
        return template.updateFirst(query,update,type).wasAcknowledged();
    }

    public boolean remove(Query query, Class type){
        return template.remove(query,type).wasAcknowledged();
    }


}
