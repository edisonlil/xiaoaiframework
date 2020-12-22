package com.xiaoaiframework.spring.mongo.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

public class MongoMappingScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {


    Logger LOGGER = LoggerFactory.getLogger(MongoMappingScannerRegistrar.class);

    BeanFactory factory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        this.factory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        if(!AutoConfigurationPackages.has(factory)) {
            //无法确定自动配置程序包自动rpcClient扫描已禁用
            LOGGER.debug("Could not determine auto-configuration package,automatic mongo mapping scanning disabled");
            return;
        }

        LOGGER.debug("Searching for mongo annotated with @Mapping");


        List<String> packages = AutoConfigurationPackages.get(this.factory);
        if(LOGGER.isDebugEnabled()){
            packages.forEach(pkg -> LOGGER.debug("Using auto-configuration base package '{}'", pkg));
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MongoMappingScannerConfiguration.class);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
        registry.registerBeanDefinition(MongoMappingScannerConfiguration.class.getName(),builder.getBeanDefinition());


    }
}
