package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.annotation.Like;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

@Component
public class LikeCriteriaParsing extends AbstractCriteriaParsing {
    @Override
    public boolean match(Annotation annotation) {

        if(annotation instanceof Like){
            return true;
        }
        return false;
    }

    @Override
    public Criteria operand(Annotation annotation,String key, Object val) {
        return Criteria.where(key).regex(Pattern.compile("^.*" + val + ".*$", Pattern.CASE_INSENSITIVE));
    }

}
