package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.constant.ActionType;
import com.xiaoaiframework.spring.mongo.wrapper.CriteriaWrapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class AbstractCriteriaParsing implements CriteriaParsing {

    
    @Override
    public void parsing(CriteriaWrapper criteria, Annotation annotation, String key, Object val) {

        if(!match(annotation)){
            return;
        }

        Map<String,Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);

        Boolean ignoreNull = (Boolean) attributes.get("ignoreNull");
        if(ignoreNull && val == null){
            return;
        }
        key = (attributes.get("name") != null && !"".equals(attributes.get("name"))) ? attributes.get("name").toString():key;

        ActionType action = (ActionType)attributes.get("action");
        switch (action){

            case AND:
                operand(criteria.getAndCriteria(key),annotation,key,val);
                break;
            case OR:
                operand(criteria.getOrCriteria(key),annotation,key,val);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }


    abstract Criteria operand(Criteria criteria,Annotation annotation,String key,Object val);
    
    
}
