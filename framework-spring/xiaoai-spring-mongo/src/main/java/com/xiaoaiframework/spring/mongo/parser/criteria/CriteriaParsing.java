package com.xiaoaiframework.spring.mongo.parser.criteria;

import com.xiaoaiframework.spring.mongo.wrapper.CriteriaWrapper;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.annotation.Annotation;


/**
 * 条件解析器
 * @author edsion
 */
public interface CriteriaParsing {


    void parsing(CriteriaWrapper criteria, Annotation annotation, String key, Object val);

    boolean match(Annotation annotation);




}
