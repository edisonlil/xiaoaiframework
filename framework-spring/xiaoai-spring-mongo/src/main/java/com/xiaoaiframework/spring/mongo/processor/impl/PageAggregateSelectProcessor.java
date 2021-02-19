package com.xiaoaiframework.spring.mongo.processor.impl;

import com.xiaoaiframework.spring.mongo.execute.MongoExecute;
import com.xiaoaiframework.spring.mongo.kit.MongoPageHelper;
import com.xiaoaiframework.spring.mongo.page.Page;
import com.xiaoaiframework.spring.mongo.processor.AggregateSelectProcessor;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 分页查询前置处理器
 * @author edison
 */
@Component
public class PageAggregateSelectProcessor implements AggregateSelectProcessor {

    @Autowired
    MongoExecute execute;


    @Override
    public void frontProcessor(List list, Class input, Class output) {

        List<AggregationOperation> operations = list;
        Page page = MongoPageHelper.get();

        if(page == null){return;}

        if(page != null && !page.getPageSizeZero()){
            list.add(new LimitOperation(page.getPageSize()));
            list.add(new SkipOperation(page.getStartRow()));
        }

        MatchOperation matchOperation = null;
        for (AggregationOperation operation : operations) {

            if(operation instanceof MatchOperation){
                matchOperation = (MatchOperation) operation;
                continue;
            }

        }

        CriteriaDefinition definition = ReflectUtil.getFieldValue(matchOperation,"criteriaDefinition");
        Query query = new Query(definition);
        page.setQuery(query);
        page.setEntityType(input);
    }

    @Override
    public Object postProcessor(Object result, Class input, Class output) {

        Page page = MongoPageHelper.get();

        if(page == null){return result;}

        if(CollUtil.isColl(result)){
            page.addAll((Collection) result);
        }else {
            page.add(result);
        }

        if(page.getCount()){
            page.setTotal(execute.count(page.getQuery(),page.getEntityType()));
        }
                                                              
        MongoPageHelper.clear();

        return page;
    }
}
