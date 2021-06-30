package com.xiaoaiframework.spring.mongo.parser;

import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import com.xiaoaiframework.spring.mongo.context.AggregateContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.QueryContext;
import com.xiaoaiframework.spring.mongo.wrapper.CriteriaWrapper;
import com.xiaoaiframework.util.base.AnnotationUtil;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class CriteriaParser implements OperationParser {

    @Override
    public void parsing(MongoContext context) {

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();

        CriteriaWrapper criteria = new CriteriaWrapper();


        Object[] objects = context.getObjects();

        if(objects != null){
            for (int i = 0; i < objects.length; i++) {
                Annotation[] annotation = annotations[i];
                criteria = criteriaParsing(criteria,annotation,null,objects[i]);
            }
        }

        if(criteria.getCriteriaObject().size() == 0){
           return;
        }
        if(context instanceof QueryContext){
            ((QueryContext)context).getQuery().addCriteria(criteria);
        }else if(context instanceof AggregateContext){
            ((AggregateContext)context).addOperation(new MatchOperation(criteria));
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
    private CriteriaWrapper criteriaParsing(CriteriaWrapper criteria, Annotation[] annotations, String key, Object val) {


        if(AnnotationUtil.match(annotations,Deprecated.class)){
            return criteria;
        }

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

            if(AnnotationUtils.getValue(annotation,"operation") == null){
                return criteria;
            }
            parsing(criteria, annotation, key,val);
        }

        return criteria;
    }


    private void parsing(CriteriaWrapper criteria, Annotation annotation, String key, Object val) {

        Map<String,Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);

        Boolean ignoreNull = (Boolean) attributes.get("ignoreNull");
        if(ignoreNull && val == null){
            return;
        }
        key = (attributes.get("name") != null && !"".equals(attributes.get("name"))) ? attributes.get("name").toString():key;
        operand(criteria,attributes,key,val);
    }


    private void operand(CriteriaWrapper criteriaWrapper, Map<String,Object> attributes, String key, Object val){

        ActionType action = (ActionType)attributes.get("action");
        String operation = attributes.get("operation").toString();

        switch (action){

            case AND:
                criteriaWrapper.and(key,operation,val);
                break;
            case OR:
                criteriaWrapper.or(key,operation,val);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }


}
