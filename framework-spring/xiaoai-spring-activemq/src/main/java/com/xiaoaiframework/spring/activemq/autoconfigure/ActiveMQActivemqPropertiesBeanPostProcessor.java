package com.xiaoaiframework.spring.activemq.autoconfigure;

import com.xiaoaiframework.spring.activemq.properties.ActiveMQActivemqProperties;
import com.xiaoaiframework.spring.activemq.scanner.ActiveMQConsumerScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @author edison
 */
public class ActiveMQActivemqPropertiesBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    BeanFactory factory;

    ActiveMQConnectionRegistrar registrar;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        if(bean instanceof ActiveMQActivemqProperties){

            ActiveMQActivemqProperties properties = (ActiveMQActivemqProperties) bean;

            this.registrar = factory.getBean(ActiveMQConnectionRegistrar.class);

            properties.getDataSources().forEach(dataSource->{

                registrar.registrar(dataSource);

                ActiveMQConsumerScanner scanner = new ActiveMQConsumerScanner((BeanDefinitionRegistry) factory);
                scanner.setResourceLoader((ResourceLoader) factory);
                scanner.registerFilters();
                List<String> packages = AutoConfigurationPackages.get(factory);
                scanner.scan(StringUtils.tokenizeToStringArray(StringUtils.collectionToCommaDelimitedString(packages)
                        , ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
            });
        }




        return null;
    }




    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }
}
