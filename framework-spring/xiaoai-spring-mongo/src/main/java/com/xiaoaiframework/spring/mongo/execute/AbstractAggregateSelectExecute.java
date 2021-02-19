package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.processor.AggregateSelectProcessor;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAggregateSelectExecute extends AbstractSelectExecute {


    List<AggregateSelectProcessor> selectProcessors;


    @Override
    public Object select(Select select, Method method, Object[] objects) {


        List<AggregationOperation> operations = new ArrayList<>();
        //预先处理
        selectFrontProcessors(operations,entityType,select.rawType());

        Object data = select(operations,entityType,select.rawType());

        //处理返回结果
        return selectPostProcessors(data,entityType,select.rawType());
    }



    public abstract Object select(List<AggregationOperation> operations,Class input,Class output);



    Object selectPostProcessors(Object result,Class input,Class output) {

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return result;
        }

        for (AggregateSelectProcessor processor : selectProcessors) {
            result = processor.postProcessor(result,input,output);
        }

        return result;
    }

    void selectFrontProcessors(List<AggregationOperation> operations,Class input,Class output) {

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return;
        }

        for (AggregateSelectProcessor processor : selectProcessors) {
            processor.frontProcessor(operations, input,output);
        }
    }
}
