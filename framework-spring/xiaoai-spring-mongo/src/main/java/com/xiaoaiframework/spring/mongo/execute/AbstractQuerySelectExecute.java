package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import org.springframework.data.mongodb.core.query.Query;


import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractQuerySelectExecute extends AbstractSelectExecute {

    List<QuerySelectProcessor> selectProcessors;


    ConditionParsing parsing;

    @Override
    public Object select(Select select, Method method, Object[] objects) {

        Query query = parsing.getQuery(method, objects);
        selectFrontProcessors(query,entityType);

        Object data = select(query);

        return selectPostProcessors(data,entityType);
    }


    public abstract Object select(Query query);


    Object selectPostProcessors(Object result,Class rawType) {

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return result;
        }

        for (QuerySelectProcessor processor : selectProcessors) {
            result = processor.postProcessor(result,rawType);
        }

        return result;
    }

    void selectFrontProcessors(Query query, Class entityType) {

        if (selectProcessors == null || selectProcessors.isEmpty()) {
            return;
        }

        for (QuerySelectProcessor processor : selectProcessors) {
            processor.frontProcessor(query, entityType);
        }
    }
}
