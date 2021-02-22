package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CountSelectExecute extends AbstractQuerySelectExecute  {

    @Override
    public Object select(Query query,Class entityType) {
        return execute.count(query,entityType);
    }
}
