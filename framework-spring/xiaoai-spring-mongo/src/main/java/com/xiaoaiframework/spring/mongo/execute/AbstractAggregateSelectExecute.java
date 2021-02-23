package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.Join;
import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.AggregateSelectProcessor;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAggregateSelectExecute extends AbstractSelectExecute {


    @Autowired
    ConditionParsing conditionParsing;

    @Autowired
    ObjectFactory<List<AggregateSelectProcessor>> selectProcessorsObjectFactory;


    @Override
    public Object select(Select select, Method method, Object[] objects,Class entityType) {

        //TODO 算子解析器暂时没有完成
        List<AggregationOperation> operations = new ArrayList<>();


        Join join = method.getAnnotation(Join.class);

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from(join.slave().getSimpleName())
                .localField(join.localField())
                .foreignField(join.foreignField())
                .as(join.as());
        operations.add(lookupOperation);
        operations.add(new MatchOperation(conditionParsing.getCriteria(method, objects)));



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
