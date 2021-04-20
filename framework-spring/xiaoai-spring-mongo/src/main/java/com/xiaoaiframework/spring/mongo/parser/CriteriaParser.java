package com.xiaoaiframework.spring.mongo.parser;

import cn.hutool.core.annotation.AnnotationUtil;
import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import com.xiaoaiframework.spring.mongo.context.AggregateContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.QueryContext;
import com.xiaoaiframework.spring.mongo.parser.criteria.CriteriaParsing;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class CriteriaParser implements OperationParser {


    List<CriteriaParsing> parsings;


    @Override
    public void parsing(MongoContext context) {

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();

        CriteriaWrapper criteria = new CriteriaWrapper();


        Object[] objects = context.getObjects();



        if(context.getObjects() != null){
            for (int i = 0; i < objects.length; i++) {
                Annotation[] annotation = annotations[i];
                criteria = criteriaParsing(criteria,annotation,null,objects[i]);
            }
        }

        Criteria raw = criteria.build();
        if(raw.getCriteriaObject().size() == 0){
           return;
        }
        if(context instanceof QueryContext){
            ((QueryContext)context).getQuery().addCriteria(raw);
        }else if(context instanceof AggregateContext){
            ((AggregateContext)context).addOperation(new MatchOperation(raw));
        }


    }


    /**
     *
     * @param criteria
     * @param annotations
     * @param key  字段名
     * @param val 值
     * @return
     */
    public CriteriaWrapper criteriaParsing(CriteriaWrapper criteria, Annotation[] annotations, String key, Object val) {


        for (Annotation annotation : annotations) {
            //If it is not the basic type

            if (annotation instanceof Condition && ObjectUtil.isNotNull(val) && ObjectUtil.isNotPrimitive(val)) {

                Field[] fields = ReflectUtil.getDeclaredFields(val);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getAnnotations() != null) {
                        try {
                            criteriaParsing(criteria, field.getAnnotations(), field.getName(),field.get(val));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(key  == null){ key = AnnotationUtils.getValue(annotation,"name").toString(); }

            ActionType action = (ActionType) AnnotationUtils.getValue(annotation,"action");
            for (CriteriaParsing parsing : parsings) {

                switch (action){

                    case OR:
                        criteria.and = parsing.parsing(criteria.and(), annotation, key,val);
                        break;
                    case AND:
                        criteria.or = parsing.parsing(criteria.or(), annotation, key,val);
                        break;

                }

                continue;
            }

        }

        return criteria;
    }

    @Autowired
    public void setParsers(ObjectFactory<List<CriteriaParsing>> factory) {
        this.parsings = factory.getObject();
    }



   static class CriteriaWrapper{

        Criteria criteria = new Criteria();

        Criteria and = new Criteria();
        Criteria or = new Criteria();


       public void setCriteria(Criteria criteria) {
           this.criteria = criteria;
       }

       public Criteria and() {
           return and;
       }

       public Criteria or() {
           return or;
       }

       public Criteria build(){


           if(and.getCriteriaObject().size() >= 1){
               criteria.andOperator(and);
           }

           if(or.getCriteriaObject().size() >= 1){
               criteria.orOperator(or);
           }

           return criteria;
       }
   }


}
