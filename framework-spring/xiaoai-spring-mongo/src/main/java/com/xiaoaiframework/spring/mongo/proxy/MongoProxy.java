package com.xiaoaiframework.spring.mongo.proxy;

import com.xiaoaiframework.spring.mongo.annotation.Eq;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.util.bean.ObjectUtil;
import com.xiaoaiframework.util.bean.ReflectUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    Executor executor;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        //判斷方法是什麽類型的

        String methodName = method.getName();

        if(methodName.startsWith("find")){


            Criteria andCriteria = new Criteria();
            Criteria orCriteria = new Criteria();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < objects.length; i++) {

                Annotation annotation = annotations[i][0];
                if(annotation instanceof Eq){
                    Eq eq = (Eq) annotation;
                    String filedName = eq.name();
                    if(eq.action() == ActionType.AND){
                        andCriteria.and(filedName).is(objects[i]);
                    }else if(eq.action() == ActionType.OR){
                        orCriteria.and(filedName).is(objects[i]);
                    }
                }
            }
            Query query = new Query();
            Criteria criteria = new Criteria();
            
            criteria.andOperator(andCriteria).orOperator(orCriteria);
            query.addCriteria(criteria);
            executor.find(query,entityType);


        }else if(method.getName().startsWith("save")){

            assert objects.length == 1;

            Object val = objects[0];
            if (entityType != val.getClass()) {
                Object entity = ReflectUtil.newInstance(entityType);
                ObjectUtil.copyProperties(val, entity);
            }
            executor.save(val);

        }


        method.getName().startsWith("update");
        method.getName().startsWith("delete");



        method.getParameterAnnotations();





        return null;
    }



    public Class<?> getKeyType() {
        return keyType;
    }

    public void setKeyType(Class<?> keyType) {
        this.keyType = keyType;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
