package com.xiaoaiframework.spring.mongo.factory;

import com.xiaoaiframework.spring.mongo.MongoExecute;
import com.xiaoaiframework.spring.mongo.annotation.Mapping;
import com.xiaoaiframework.spring.mongo.parsing.ConditionParsing;
import com.xiaoaiframework.spring.mongo.processor.ExecuteProcessor;
import com.xiaoaiframework.spring.mongo.processor.SaveProcessor;
import com.xiaoaiframework.spring.mongo.processor.SelectProcessor;
import com.xiaoaiframework.spring.mongo.processor.UpdateProcessor;
import com.xiaoaiframework.spring.mongo.proxy.MongoProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

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


    static class BeanFactoryHolder {

        BeanFactory factory;

        public BeanFactoryHolder(BeanFactory factory) {
            this.factory = factory;
        }

        public <T>List<T> getBeansByType(Class<T> c){

            Map<String,T> beans = ((DefaultListableBeanFactory) factory).getBeansOfType(c);
            List<T> list = new ArrayList<>(beans.size());
            list.addAll(beans.values());
            return list;
        }
        
        public <T> T getBean(Class<T> c){

            return factory.getBean(c);
        }

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
        proxy.setSelectFrontProcessor(factory.getBeansByType(SelectProcessor.class));
        proxy.setUpdateFrontProcessors(factory.getBeansByType(UpdateProcessor.class));
        return Proxy.newProxyInstance(this.mongoInterface.getClassLoader()
        ,new Class[]{this.mongoInterface},proxy);
    }

    @Override
    public Class<?> getObjectType() {
        return mongoInterface;
    }
}
