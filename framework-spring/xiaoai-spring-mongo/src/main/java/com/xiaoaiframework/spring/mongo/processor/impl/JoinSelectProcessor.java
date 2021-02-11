package com.xiaoaiframework.spring.mongo.processor.impl;

import com.xiaoaiframework.spring.mongo.processor.SelectProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 分页查询前置处理器
 * @author edison
 */
@Component
public class JoinSelectProcessor implements SelectProcessor {

    @Autowired
    MongoTemplate template;

    @Override
    public void frontProcessor(Query query, Class entity) {

        List<AggregationOperation> list = new ArrayList<>();
        list.add(new MatchOperation(Criteria.where("")));

        Aggregation aggregation = Aggregation.newAggregation(list);

        template.aggregate(aggregation,entity,entity);
    }


    @Override
    public Object postProcessor(Object result,Class rawType){
        return result;
    }
}
