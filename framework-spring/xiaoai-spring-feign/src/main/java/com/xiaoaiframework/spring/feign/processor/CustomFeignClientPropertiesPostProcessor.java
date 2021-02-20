package com.xiaoaiframework.spring.feign.processor;

import com.xiaoaiframework.spring.feign.FeignDefinition;
import com.xiaoaiframework.spring.feign.MultiFeignDefinition;
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
            
            register(properties.getDefinition(),registrar);

            List<MultiFeignDefinition> multiFeignDefinitions = properties.getMultiDefinition();
            if(multiFeignDefinitions != null && multiFeignDefinitions.size() > 0){
                for (MultiFeignDefinition multiDefinition : multiFeignDefinitions) {
                    register(multiDefinition.getFeignDefinitions(),registrar);
                }
            }


        }

        return bean;
    }

    private void register(List<FeignDefinition> definitions,CustomFeignClientRegistrar registrar){

        if(definitions == null || definitions.size() == 0){return;}

        for (FeignDefinition definition : definitions) {
            registrar.register(definition);
        }

    }



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
