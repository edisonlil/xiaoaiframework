package com.xiaoaiframework.spring.mongo.factory;

import com.xiaoaiframework.spring.mongo.annotation.Mapping;
import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.spring.mongo.proxy.MongoProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
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
        proxy.setExecutor(factory.getBean(Executor.class));
       
        return Proxy.newProxyInstance(this.mongoInterface.getClassLoader()
        ,new Class[]{this.mongoInterface},proxy);
    }

    @Override
    public Class<?> getObjectType() {
        return mongoInterface;
    }
}
