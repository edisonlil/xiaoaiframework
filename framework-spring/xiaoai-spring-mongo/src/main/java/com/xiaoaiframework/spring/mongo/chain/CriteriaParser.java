package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.context.AggregateSelectContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.QuerySelectContext;
import com.xiaoaiframework.spring.mongo.parsing.criteria.CriteriaParsing;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class CriteriaParser extends ConditionParser {


    List<CriteriaParsing> parsings;


    @Override
    public void parsing(MongoContext context) {

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();

        Criteria criteria = new Criteria();
        if(context.getObjects() != null){
            for (int i = 0; i < context.getObjects().length; i++) {
                Annotation[] annotation = annotations[i];
                criteria = criteriaParsing(criteria,annotation,context.getObjects());
            }
        }

        if(context instanceof QuerySelectContext){
            ((QuerySelectContext)context).getQuery().addCriteria(criteria);
        }else if(context instanceof AggregateSelectContext){
            ((AggregateSelectContext)context).addOperation(new MatchOperation(criteria));
        }


    }


    public Criteria criteriaParsing(Criteria criteria, Annotation[] annotations, Object val) {


        for (Annotation annotation : annotations) {
            //If it is not the basic type
            if (annotation instanceof Condition && !ObjectUtil.isPrimitive(val)) {

                Field[] fields = ReflectUtil.getDeclaredFields(val);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getAnnotations() != null) {
                        try {
                            criteriaParsing(criteria, field.getAnnotations(), field.get(val));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            for (CriteriaParsing parsing : parsings) {
                criteria = parsing.parsing(criteria, annotation, val);
                continue;
            }

        }

        return criteria;
    }

    @Autowired
    public void setParsers(ObjectFactory<List<CriteriaParsing>> factory) {
        this.parsings = factory.getObject();
    }


}
