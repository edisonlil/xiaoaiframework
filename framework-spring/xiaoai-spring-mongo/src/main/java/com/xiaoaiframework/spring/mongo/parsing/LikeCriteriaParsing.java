package com.xiaoaiframework.spring.mongo.parsing;

import com.xiaoaiframework.spring.mongo.annotation.Like;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

@Component
public class LikeCriteriaParsing implements CriteriaParsing {


    @Override
    public Criteria parsing(Criteria criteria, Annotation annotation, Object val) {

        if(!match(annotation)){
            return criteria;
        }

        Like like = (Like) annotation;
        if(like.action() == ActionType.AND){
            criteria.andOperator(Criteria.where(like.name()).regex(Pattern.compile("^.*" + val + ".*$", Pattern.CASE_INSENSITIVE)));
        }else if(like.action() == ActionType.OR){
            criteria.orOperator(Criteria.where(like.name()).regex(Pattern.compile("^.*" + val + ".*$", Pattern.CASE_INSENSITIVE)));
        }

        return criteria;
    }

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof Like;
    }



}
