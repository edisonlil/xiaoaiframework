package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.constant.ActionType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class AbstractCriteriaParsing implements CriteriaParsing {

    
    @Override
    public Criteria parsing(Criteria criteria, Annotation annotation,String key,Object val) {

        if(!match(annotation)){
            return criteria;
        }

        Map<String,Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);

        Boolean ignoreNull = (Boolean) attributes.get("ignoreNull");
        if(ignoreNull && val == null){
            return criteria;
        }

        ActionType action = (ActionType) attributes.get("action");

        key = key == null ? attributes.get("name").toString():key;

        if(action == ActionType.AND){
            criteria.andOperator(operand(annotation,key,val));
        }else if(action == ActionType.OR){
            criteria.orOperator(operand(annotation,key,val));
        }

        return criteria;
    }

    
    
}
