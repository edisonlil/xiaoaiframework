package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.annotation.Order;
import com.xiaoaiframework.spring.mongo.context.AggregateSelectContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.QuerySelectContext;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 排序解析器
 * @author edison
 */
@Component
public class OrderParser implements OperationParser{



    @Override
    public void parsing(MongoContext context) {

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();

        Sort sort = null;
        if(context.getObjects() != null){
            for (int i = 0; i < context.getObjects().length; i++) {
                Annotation[] annotation = annotations[i];
                sort = orderParsing(annotation,context.getObjects());
            }
        }

        if(context instanceof QuerySelectContext){
            ((QuerySelectContext)context).getQuery().with(sort);
        } else if(context instanceof AggregateSelectContext){
            ((AggregateSelectContext)context).addOperation(new SortOperation(sort));
        }
    }



    public Sort orderParsing(Annotation[] annotations, Object val){

        for (Annotation annotation : annotations) {

            if (annotation instanceof Condition && !ObjectUtil.isPrimitive(val)) {

                Field[] fields = ReflectUtil.getDeclaredFields(val);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getAnnotations() != null) {
                        try {
                            return orderParsing(field.getAnnotations(), field.get(val));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            //排序
            if(annotation instanceof Order && CollUtil.isMap(val)){

                Map order = (Map) val;
                if(order != null && !order.isEmpty()){
                   return convertSort(order);
                }
            }
        }

        return null;

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

}
