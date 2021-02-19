package com.xiaoaiframework.spring.mongo.processor.impl;

import com.xiaoaiframework.spring.mongo.execute.MongoExecute;
import com.xiaoaiframework.spring.mongo.kit.MongoPageHelper;
import com.xiaoaiframework.spring.mongo.page.Page;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 分页查询前置处理器
 * @author edison
 */
@Component
public class PageQuerySelectProcessor implements QuerySelectProcessor {

    @Autowired
    MongoExecute execute;

    @Override
    public void frontProcessor(Query query, Class entity) {

        Page page = MongoPageHelper.get();
        if(page == null){return;}

        if(page != null && !page.getPageSizeZero()){
            query.with(PageRequest.of(page.getPageNum(),page.getPageSize()));
        }
        page.setQuery(query);
        page.setEntityType(entity);

    }


    @Override
    public Object postProcessor(Object result,Class rawType){

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
