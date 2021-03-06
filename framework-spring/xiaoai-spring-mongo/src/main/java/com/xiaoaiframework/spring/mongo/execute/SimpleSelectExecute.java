package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * 简单的查询执行器
 * @author edison
 */
@Component
public class SimpleSelectExecute extends AbstractQuerySelectExecute {


    @Override
    public Object select(Query query,Class entityType) {
        return execute.find(query,entityType);
    }

}
