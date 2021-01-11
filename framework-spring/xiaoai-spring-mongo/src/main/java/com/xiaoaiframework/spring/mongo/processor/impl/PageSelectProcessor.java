package com.xiaoaiframework.spring.mongo.processor.impl;

import com.xiaoaiframework.spring.mongo.kit.MongoPageHelper;
import com.xiaoaiframework.spring.mongo.page.Page;
import com.xiaoaiframework.spring.mongo.processor.SelectProcessor;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;

/**
 * 分页查询前置处理器
 * @author edison
 */
public class PageSelectProcessor implements SelectProcessor {

    @Autowired
    MongoTemplate template;

    @Override
    public void frontProcessor(Query query, Class entity) {

        Page page = MongoPageHelper.get();
        if(page != null && !page.getPageSizeZero()){
            query.with(PageRequest.of(page.getPageNum(),page.getPageSize()));
        }

    }


    @Override
    public void postProcessor(Object result,Class rawType){

        Page page = MongoPageHelper.get();
        if(CollUtil.isColl(result)){
            page.addAll((Collection) result);
        }else {
            page.add(result);
        }
        
        if(page.getCount()){
            page.setTotal(template.count(page.getQuery(),page.getEntityType()));
        }
        result = page;
        
        MongoPageHelper.clear();
    }
}
