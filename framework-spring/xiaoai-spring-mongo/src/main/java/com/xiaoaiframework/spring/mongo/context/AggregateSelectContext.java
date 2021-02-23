package com.xiaoaiframework.spring.mongo.context;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.*;

public class AggregateSelectContext extends MongoContext {

    List<AggregationOperation> operations = new ArrayList<>();

    public void addOperation(AggregationOperation operation){
        operations.add(operation);
    }

    public List<AggregationOperation> getOperations() {
        return operations;
    }
}
