package com.xiaoaiframework.spring.mongo.factory;

import com.xiaoaiframework.spring.mongo.annotation.Mapping;
import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.proxy.MongoProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Proxy;

public class MongoMappingProxyFactory implements FactoryBean, BeanFactoryAware {


    BeanFactory factory;

    Class<?> mongoInterface;

    public MongoMappingProxyFactory(Class<?> mongoInterface){
        this.mongoInterface = mongoInterface;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }

    @Override
    public Object getObject() throws Exception {

        Mapping mapping = mongoInterface.getAnnotation(Mapping.class);

        MongoProxy proxy = new MongoProxy();
        proxy.setKeyType(mapping.keyType());
        proxy.setEntityType(mapping.entityType());
        proxy.setTemplate(factory.getBean(MongoTemplate.class));
        proxy.setParsing(factory.getBean(ConditionParsing.class));
       
        return Proxy.newProxyInstance(this.mongoInterface.getClassLoader()
        ,new Class[]{this.mongoInterface},proxy);
    }

    @Override
    public Class<?> getObjectType() {
        return mongoInterface;
    }
}
