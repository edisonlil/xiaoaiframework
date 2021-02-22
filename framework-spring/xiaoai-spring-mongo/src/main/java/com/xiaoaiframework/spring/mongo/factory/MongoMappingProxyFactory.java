package com.xiaoaiframework.spring.mongo.factory;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.execute.MongoExecute;
import com.xiaoaiframework.spring.mongo.annotation.Mapping;
import com.xiaoaiframework.spring.mongo.service.ConvertService;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.ExecuteProcessor;
import com.xiaoaiframework.spring.mongo.processor.QuerySelectProcessor;
import com.xiaoaiframework.spring.mongo.processor.SaveProcessor;
import com.xiaoaiframework.spring.mongo.processor.UpdateProcessor;
import com.xiaoaiframework.spring.mongo.proxy.MongoProxy;
import com.xiaoaiframework.spring.mongo.service.SelectService;
import com.xiaoaiframework.spring.mongo.util.BeanFactoryHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoMappingProxyFactory implements FactoryBean, BeanFactoryAware {

    BeanFactoryHolder factory;

    Class<?> mongoInterface;

    public MongoMappingProxyFactory(Class<?> mongoInterface){
        this.mongoInterface = mongoInterface;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = new BeanFactoryHolder(beanFactory);
    }






    @Override
    public Object getObject() throws Exception {

        Mapping mapping = mongoInterface.getAnnotation(Mapping.class);

        MongoProxy proxy = new MongoProxy();
        proxy.setKeyType(mapping.keyType());
        proxy.setEntityType(mapping.entityType());
        proxy.setExecute(factory.getBean(MongoExecute.class));
        proxy.setParsing(factory.getBean(ConditionParsing.class));


        proxy.setExecuteFrontProcessors(factory.getBeansByType(ExecuteProcessor.class));
        proxy.setSaveFrontProcessor(factory.getBeansByType(SaveProcessor.class));
        proxy.setSelectService(factory.getBean(SelectService.class));
        proxy.setUpdateFrontProcessors(factory.getBeansByType(UpdateProcessor.class));
        return Proxy.newProxyInstance(this.mongoInterface.getClassLoader()
        ,new Class[]{this.mongoInterface},proxy);
    }

    @Override
    public Class<?> getObjectType() {
        return mongoInterface;
    }
}
