package com.xiaoaiframework.spring.activemq.autoconfigure;

import com.xiaoaiframework.spring.activemq.properties.ActiveMQActivemqProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ActiveMQActivemqPropertiesBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    ActiveMQConnectionRegistrar registrar;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        if(bean instanceof ActiveMQActivemqProperties){

            ActiveMQActivemqProperties properties = (ActiveMQActivemqProperties) bean;
            properties.getDataSources().forEach(dataSource->{

                registrar.registrar(dataSource);
                
            });

        }


        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.registrar = beanFactory.getBean(ActiveMQConnectionRegistrar.class);
    }
}
