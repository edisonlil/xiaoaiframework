package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Gt;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class GtCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Gt){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Criteria criteria,Annotation annotation,String key, Object val) {
        return criteria.and(key).gt(val);
    }

}
