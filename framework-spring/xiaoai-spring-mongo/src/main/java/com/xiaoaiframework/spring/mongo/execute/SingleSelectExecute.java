package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * 单个查询
 * @author edison
 */
@Component
public class SingleSelectExecute extends AbstractQuerySelectExecute {
    @Override
    public Object select(Query query,Class entityType) {
        return execute.findOne(query,entityType);
    }
}
