package com.xiaoaiframework.spring.mongo.parsing;

import com.xiaoaiframework.spring.mongo.parsing.criteria.CriteriaParsing;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 条件解析策略
 * @author edison
 */
@Component
public class QueryParsing {


    @Autowired
    ObjectFactory<List<CriteriaParsing>> factory;


    public Query parsing(Query query, Annotation annotation, Object val) {

        Criteria criteria = new Criteria();
        List<CriteriaParsing> parsings = factory.getObject();
        
        for (CriteriaParsing parsing : parsings) {
            criteria = parsing.parsing(criteria,annotation,val);
            continue;
        }
        query.addCriteria(criteria);
        return query;
    }
    

}
