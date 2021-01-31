package com.xiaoaiframework.spring.activemq.scanner;

import com.xiaoaiframework.spring.activemq.annotation.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @author edison
 */
public class ActiveMQConsumerScanner extends ClassPathBeanDefinitionScanner {


    static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQConsumerScanner.class);

    public ActiveMQConsumerScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerFilters(){
        addIncludeFilter(new AnnotationTypeFilter(Consumer.class));
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        if(beanDefinitionHolders.isEmpty()){
            LOGGER.warn("No @Consumer was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }

        return beanDefinitionHolders;
    }



}
