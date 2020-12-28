package com.xiaoaiframework.spring.mongo.parsing.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Gt;
import com.xiaoaiframework.spring.mongo.annotation.Gte;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class GteCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Gte){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Annotation annotation,String key, Object val) {
        return Criteria.where(key).gte(val);
    }

}
