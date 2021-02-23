package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.chain.ConditionParserChain;
import com.xiaoaiframework.spring.mongo.context.QueryContext;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;


import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractQuerySelectExecute extends AbstractSelectExecute{


    @Autowired
    ObjectFactory<List<QuerySelectProcessor>> selectProcessorsObjectFactory;

    @Autowired
    ConditionParserChain chain;

    @Override
    public Object select(Select select, Method method, Object[] objects,Class entityType) {


        QueryContext context = new QueryContext();
        context.setMethod(method);
        context.setObjects(objects);
        context.setEntityType(entityType);
        chain.doParsing(context);
        Query query = context.getQuery();


        selectFrontProcessors(query,entityType);

        Object data = select(query,entityType);

        return selectPostProcessors(data,entityType);
    }


    public abstract Object select(Query query,Class entityType);


    Object selectPostProcessors(Object result,Class rawType) {

        List<QuerySelectProcessor> selectProcessors = selectProcessorsObjectFactory.getObject();

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return result;
        }

        for (QuerySelectProcessor processor : selectProcessors) {
            result = processor.postProcessor(result,rawType);
        }

        return result;
    }

    void selectFrontProcessors(Query query, Class entityType) {

        List<QuerySelectProcessor> selectProcessors = selectProcessorsObjectFactory.getObject();

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return;
        }

        for (QuerySelectProcessor processor : selectProcessors) {
            processor.frontProcessor(query, entityType);
        }
    }


}
