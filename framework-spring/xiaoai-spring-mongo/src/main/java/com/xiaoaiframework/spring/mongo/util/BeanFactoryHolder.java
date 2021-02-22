package com.xiaoaiframework.spring.mongo.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanFactoryHolder {

    BeanFactory factory;

    public BeanFactoryHolder(BeanFactory factory) {
        this.factory = factory;
    }

    public <T> List<T> getBeansByType(Class<T> c){

        Map<String,T> beans = ((DefaultListableBeanFactory) factory).getBeansOfType(c);
        List<T> list = new ArrayList<>(beans.size());
        list.addAll(beans.values());
        return list;
    }

    public <T> T getBean(Class<T> c){
        return factory.getBean(c);
    }
}
