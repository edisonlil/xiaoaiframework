package com.xiaoaiframework.spring.feign.processor;

import com.xiaoaiframework.spring.feign.FeignDefinition;
import com.xiaoaiframework.spring.feign.configuration.properties.CustomFeignClientProperties;
import com.xiaoaiframework.spring.feign.registrar.CustomFeignClientRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.*;

public class CustomFeignClientPropertiesPostProcessor  implements BeanPostProcessor, BeanFactoryAware {

    BeanFactory beanFactory;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        if(bean instanceof CustomFeignClientProperties){

            CustomFeignClientProperties properties = (CustomFeignClientProperties) bean;

            CustomFeignClientRegistrar registrar = beanFactory.getBean(CustomFeignClientRegistrar.class);

            List<FeignDefinition> definitions = properties.getDefinition();
            for (FeignDefinition definition : definitions) {
                registrar.register(definition);
            }

        }

        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
