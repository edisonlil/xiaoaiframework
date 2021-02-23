package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.annotation.NotIn;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class NotInCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof NotIn){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Annotation annotation,String key, Object val) {
        return Criteria.where(key).nin(val);
    }

}
