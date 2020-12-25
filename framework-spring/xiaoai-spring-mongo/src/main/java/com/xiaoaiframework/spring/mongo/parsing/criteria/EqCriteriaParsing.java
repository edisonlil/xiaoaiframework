package com.xiaoaiframework.spring.mongo.parsing.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Eq;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;


/**
 * @author edison
 */
@Component
public class EqCriteriaParsing implements CriteriaParsing {


    @Override
    public Criteria parsing(Criteria criteria, Annotation annotation, Object val) {

        if(!match(annotation)){
            return criteria;
        }

        Eq eq = (Eq) annotation;

        if(!eq.ignoreNull() && val == null){
            return criteria;
        }
        if(eq.action() == ActionType.AND){
            criteria.andOperator(Criteria.where(eq.name()).is(val));
        }else if(eq.action() == ActionType.OR){
            criteria.orOperator(Criteria.where(eq.name()).is(val));
        }

        return criteria;
    }


    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Eq){
            return true;
        }
        return false;
    }



}
