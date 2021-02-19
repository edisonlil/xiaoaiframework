package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.query.Query;

/**
 * 简单的查询执行器
 * @author edison
 */
public class SimpleSelectExecute extends AbstractQuerySelectExecute {


    @Override
    public Object select(Query query) {
        return execute.find(query,entityType);
    }

}
