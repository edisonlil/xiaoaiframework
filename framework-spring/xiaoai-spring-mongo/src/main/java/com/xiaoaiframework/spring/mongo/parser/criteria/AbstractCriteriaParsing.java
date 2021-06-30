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
        key = (attributes.get("name") != null && !"".equals(attributes.get("name"))) ? attributes.get("name").toString():key;
        return criteria.andOperator(operand(new Criteria(),annotation,key,val));
    }


    abstract Criteria operand(Criteria criteria,Annotation annotation,String key,Object val);
    
    
}
