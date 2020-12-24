package com.xiaoaiframework.spring.mongo.proxy;

import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.spring.mongo.parsing.CriteriaParsingStrategy;
import com.xiaoaiframework.util.bean.ObjectUtil;
import com.xiaoaiframework.util.bean.ReflectUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MongoProxy implements InvocationHandler {

    Class<?> keyType;

    Class<?> entityType;

    Executor executor;

    CriteriaParsingStrategy criteriaParsingStrategy;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {


        String methodName = method.getName();

        if(methodName.startsWith("find")){

            Annotation[][] annotations = method.getParameterAnnotations();
            Criteria criteria = new Criteria();
            for (int i = 0; i < objects.length; i++) {
                Annotation annotation = annotations[i][0];
                criteria = criteriaParsingStrategy.parsing(criteria,annotation,objects[i]);
            }
            Query query = new Query();
            query.addCriteria(criteria);
            return executor.find(query,entityType);
            
        }else if(method.getName().startsWith("save")){

            assert objects.length == 1;

            Object val = objects[0];
            if (entityType != val.getClass()) {
                Object entity = ReflectUtil.newInstance(entityType);
                ObjectUtil.copyProperties(val, entity);
            }
            executor.save(val);

        }else if(method.getName().startsWith("update")){
            throw new RuntimeException("Not implemented update");
        }else if(method.getName().startsWith("delete")){
            throw new RuntimeException("Not implemented del");
        }
        throw new RuntimeException("The method prefix definition cannot be found");

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

    public void setCriteriaParsingStrategy(CriteriaParsingStrategy criteriaParsingStrategy) {
        this.criteriaParsingStrategy = criteriaParsingStrategy;
    }

    public CriteriaParsingStrategy getCriteriaParsingStrategy() {
        return criteriaParsingStrategy;
    }
}
