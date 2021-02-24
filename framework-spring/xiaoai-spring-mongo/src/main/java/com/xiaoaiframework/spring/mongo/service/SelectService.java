package com.xiaoaiframework.spring.mongo.service;

import com.xiaoaiframework.spring.mongo.annotation.Join;
import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.execute.*;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Method;

public class SelectService implements SelectExecute, BeanFactoryAware {


    BeanFactory factory;


    @Override
    public Object doSelect(Select select, Method method, Object[] objects,Class entityType) {
        return getOrCreateExecute(select, method).doSelect(select, method, objects,entityType);
    }


    private SelectExecute getOrCreateExecute(Select select,Method method){

        AbstractSelectExecute execute;
        if (CollUtil.isColl(method.getReturnType())) {
            execute = factory.getBean(SimpleSelectExecute.class);
        }else if(ObjectUtil.isNumber(method.getReturnType())){
            execute = factory.getBean(CountSelectExecute.class);
        }else if(method.getAnnotation(Join.class) != null){
            execute = factory.getBean(AggregateSelectExecute.class);
        }else{
            execute = factory.getBean(SingleSelectExecute.class);
        }

        return execute;
    }
    

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }
}
