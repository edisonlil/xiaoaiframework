package com.xiaoaiframework.spring.mongo.parsing;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 条件解析策略
 */
@Component
public class CriteriaParsingStrategy {


    @Autowired
    ObjectFactory<List<CriteriaParsing>> factory;


    public Criteria parsing(Criteria criteria,Annotation annotation,Object val) {

        List<CriteriaParsing> parsings = factory.getObject();
        
        for (CriteriaParsing parsing : parsings) {
            criteria = parsing.parsing(criteria,annotation,val);
            continue;
        }

        return criteria;
    }


}
