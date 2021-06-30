package com.xiaoaiframework.spring.mongo.wrapper;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.HashMap;
import java.util.Map;

public class CriteriaWrapper {

    Map<String,Criteria> andCriteriaMap = new HashMap<>();
    Map<String,Criteria> orCriteriaMap = new HashMap<>();

    public Criteria getAndCriteria(String key){
        if(!andCriteriaMap.containsKey(key)){
            andCriteriaMap.put(key,Criteria.where(key));
        }
        return andCriteriaMap.get(key);
    }

    public Criteria getOrCriteria(String key){
        if(!orCriteriaMap.containsKey(key)){
            orCriteriaMap.put(key,Criteria.where(key));
        }
        return orCriteriaMap.get(key);
    }

    public Criteria build(){

        Criteria criteria = new Criteria();
        andCriteriaMap.forEach((k,v)->{
            if(v.getCriteriaObject().size() == 0){
                return;
            }
            criteria.andOperator(v);
        });

        orCriteriaMap.forEach((k,v)->{
            if(v.getCriteriaObject().size() == 0){
                return;
            }
            criteria.orOperator(v);
        });


        return criteria;
    }
}
