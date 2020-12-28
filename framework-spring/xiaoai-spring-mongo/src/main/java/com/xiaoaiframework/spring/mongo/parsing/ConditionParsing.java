package com.xiaoaiframework.spring.mongo.parsing;

import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.annotation.Order;
import com.xiaoaiframework.spring.mongo.parsing.criteria.CriteriaParsing;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 条件解析
 * @author edison
 */
@Component
public class ConditionParsing {


    @Autowired
    ObjectFactory<List<CriteriaParsing>> factory;


    public Query getQuery(Method method, Object[] objects){

        Annotation[][] annotations = method.getParameterAnnotations();

        Query query = new Query();
        for (int i = 0; i < objects.length; i++) {
            Annotation[] annotation = annotations[i];
            query = queryParsing(query,annotation,objects[i]);
        }

        return query;
    }

    private Query queryParsing(Query query, Annotation[] annotations, Object val) {
        
        for (Annotation annotation : annotations) {
            //If it is not the basic type
            if(annotation instanceof Condition && !ObjectUtil.isPrimitive(val)){

                Field[] fields = ReflectUtil.getFields(val);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if(field.getAnnotations() != null){
                        try {
                            queryParsing(query,field.getAnnotations(), field.get(val));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            //排序
            if(annotation instanceof Order && CollUtil.isMap(val)){
                query.with(convertSort((Map<String, Boolean>) val));
                return query;
            }

            //条件解析
            Criteria criteria = new Criteria();
            List<CriteriaParsing> parsings = factory.getObject();
            for (CriteriaParsing parsing : parsings) {
                criteria = parsing.parsing(criteria,annotation,val);
                continue;
            }
            query.addCriteria(criteria);

        }
        
        return query;
    }

    public Sort convertSort(Map<String,Boolean> orderMap){
        List<Sort.Order> orders = convertOrders(orderMap);
        return Sort.by(orders);
    }

    private List<Sort.Order> convertOrders(Map<String,Boolean> orderMap){
        /**
         * 设置排序字段与排序方式
         * 例如：a asc, b desc
         * 设定值方式：
         * a true ---> a asc
         * b desc ---> b desc
         */
        if(!orderMap.isEmpty()){
            List<Sort.Order> orders = new ArrayList(orderMap.size());
            Set<String> orderSet = orderMap.keySet();
            for (String key : orderSet){
                if(orderMap.get(key)){
                    orders.add(Sort.Order.asc(key));
                }else{
                    orders.add(Sort.Order.desc(key));
                }
            }
            return orders;
        }

        return null;
    }


    public Update convertUpdate(Object object){
        return convertUpdate(object,null);
    }

    /**
     * 转换update参数
     * @param object
     * @return
     */
    public Update convertUpdate(Object object, String... exclude){

        Set<String> excludeSet = null;
        if(exclude != null && exclude.length > 0){
            excludeSet = CollUtil.newHashSet(exclude);
        }

        Update update = new Update();
        Field[] fields = ReflectUtil.getFields(object);
        try {
            for (Field field : fields) {

                String fieldName = field.getName();
                if(excludeSet == null || !excludeSet.contains(fieldName)){
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if(!ObjectUtil.isNull(value)){
                        update = update.set(fieldName,value);
                    }
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }

}
