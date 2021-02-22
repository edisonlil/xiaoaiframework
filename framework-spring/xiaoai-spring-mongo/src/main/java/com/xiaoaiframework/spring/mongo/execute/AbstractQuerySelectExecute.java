package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;


import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractQuerySelectExecute extends AbstractSelectExecute{


    @Autowired
    ObjectFactory<List<QuerySelectProcessor>> selectProcessorsObjectFactory;

    @Autowired
    ConditionParsing parsing;

    @Override
    public Object select(Select select, Method method, Object[] objects,Class entityType) {

        Query query = parsing.getQuery(method, objects);
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
