package com.xiaoaiframework.spring.mongo.execute;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import java.util.List;

public class AggregateSelectExecute extends AbstractAggregateSelectExecute {





    @Override
    public Object select(List<AggregationOperation> operations, Class input, Class output) {
        return execute.aggregate(operations,input,output).getMappedResults();
    }
}
