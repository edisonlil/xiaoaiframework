package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Eq;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class EqCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Eq){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Criteria criteria,Annotation annotation,String key, Object val) {
        return criteria.is(val);
    }

}
