package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.chain.ConditionParserChain;
import com.xiaoaiframework.spring.mongo.context.AggregateSelectContext;
import com.xiaoaiframework.spring.mongo.processor.AggregateSelectProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractAggregateSelectExecute extends AbstractSelectExecute {




    @Autowired
    ObjectFactory<List<AggregateSelectProcessor>> selectProcessorsObjectFactory;

    @Autowired
    ConditionParserChain chain;


    @Override
    public Object select(Select select, Method method, Object[] objects,Class entityType) {


        AggregateSelectContext context = new AggregateSelectContext();
        context.setMethod(method);
        context.setObjects(objects);
        context.setEntityType(entityType);
        chain.doParsing(context);

        List<AggregationOperation> operations = context.getOperations();

        //预先处理
        selectFrontProcessors(operations,entityType,select.rawType());

        Object data = select(operations,entityType,select.rawType());

        //处理返回结果
        return selectPostProcessors(data,entityType,select.rawType());
    }



    public abstract Object select(List<AggregationOperation> operations,Class input,Class output);



    Object selectPostProcessors(Object result,Class input,Class output) {

        List<AggregateSelectProcessor> selectProcessors = selectProcessorsObjectFactory.getObject();

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return result;
        }

        for (AggregateSelectProcessor processor : selectProcessors) {
            result = processor.postProcessor(result,input,output);
        }

        return result;
    }

    void selectFrontProcessors(List<AggregationOperation> operations,Class input,Class output) {

        List<AggregateSelectProcessor> selectProcessors = selectProcessorsObjectFactory.getObject();

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return;
        }

        for (AggregateSelectProcessor processor : selectProcessors) {
            processor.frontProcessor(operations, input,output);
        }
    }
}
