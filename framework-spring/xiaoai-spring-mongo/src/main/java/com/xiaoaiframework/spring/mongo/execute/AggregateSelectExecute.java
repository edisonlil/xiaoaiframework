package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AggregateSelectExecute extends AbstractAggregateSelectExecute {

    @Override
    public Object select(List<AggregationOperation> operations, Class input, Class output) {
        return execute.aggregate(operations,input,output).getMappedResults();
    }
}
