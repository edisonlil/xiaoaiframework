package com.xiaoaiframework.spring.mongo.context;

import org.springframework.data.mongodb.core.query.Query;

public class QueryContext extends MongoContext {

    Query query = new Query();


    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
