package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Lt;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class LtCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Lt){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Annotation annotation,String key, Object val) {
        return Criteria.where(key).lt(val);
    }

}
