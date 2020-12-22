package com.xiaoaiframework.spring.mongo.scanner;

import com.xiaoaiframework.spring.mongo.annotation.Mapping;
import com.xiaoaiframework.spring.mongo.factory.MongoMappingProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * MongoMapping掃描器
 * @author edison
 */
public class MongoMappingScanner extends ClassPathBeanDefinitionScanner {

    static final Logger LOGGER = LoggerFactory.getLogger(MongoMappingScanner.class);

    public MongoMappingScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerFilters(){addIncludeFilter(new AnnotationTypeFilter(Mapping.class));}


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        if(beanDefinitionHolders.isEmpty()){
            LOGGER.warn("No @Mapping was found in '"+ Arrays.toString(basePackages)+"' package. Pleas check your configuration");
        }else{
            processBeanDefinitions(beanDefinitionHolders);
        }

        return beanDefinitionHolders;
    }



    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitionHolders){

        GenericBeanDefinition beanDefinition;
        for (BeanDefinitionHolder holder : beanDefinitionHolders) {
            beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            beanDefinition.setBeanClass(MongoMappingProxyFactory.class);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }



}
