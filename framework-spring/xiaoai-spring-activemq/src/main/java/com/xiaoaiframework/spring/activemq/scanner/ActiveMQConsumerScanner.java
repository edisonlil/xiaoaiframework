package com.xiaoaiframework.spring.activemq.scanner;

import com.xiaoaiframework.spring.activemq.annotation.Consumer;
import com.xiaoaiframework.util.base.AnnotationUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.base.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.xiaoaiframework.spring.activemq.autoconfigure.ActiveMQConnectionRegistrar.CONTAINER_FACTORY_SUFFIX;

/**
 * @author edison
 */
public class ActiveMQConsumerScanner extends ClassPathBeanDefinitionScanner {


    BeanDefinitionRegistry registry;

    static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQConsumerScanner.class);

    public ActiveMQConsumerScanner(BeanDefinitionRegistry registry) {
        super(registry);
        this.registry = registry;
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
            return beanDefinitionHolders;
        }

        LOGGER.info("Start registration @Consumer");
        registryConsumer(beanDefinitionHolders);

        return beanDefinitionHolders;
    }




    public void registryConsumer(Set<BeanDefinitionHolder> beanDefinitionHolderSet){


        beanDefinitionHolderSet.forEach(beanDefinitionHolder -> {

            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();

            Class beanClass = ReflectUtil.forName(beanDefinition.getBeanClassName());
            List<JmsListener> jmsListeners =
                    AnnotationUtil.getMethodAnnotationInClass(beanClass, JmsListener.class);
            Consumer consumer = (Consumer) beanClass.getAnnotation(Consumer.class);


            String consumerId = consumer.id();
            //If the consumer id is null the consumer prefix is used as the consumer id.
            if(StringUtils.isEmpty(consumerId)){
                consumerId = beanClass.getSimpleName().replace("Consumer","");
                consumerId = StrUtil.lowerFirst(consumerId);
            }

            String containerFactoryName = consumerId + CONTAINER_FACTORY_SUFFIX;

            if(isUseBeanName(containerFactoryName)){
                AnnotationUtil.alterValues(jmsListeners,"containerFactory",
                        containerFactoryName);
                beanRegister(beanDefinitionHolder.getBeanName(),beanDefinition,beanClass);
                LOGGER.info("The {} Container start listen.",consumerId);
            }

        });


    }


    public boolean isUseBeanName(String beanName){
        return registry.isBeanNameInUse(beanName);
    }

    /**
     * 注册Bean
     * @param beanName
     * @param definition
     */
    <T>T beanRegister(String beanName,BeanDefinition definition,Class<T> type){
        if(!registry.isBeanNameInUse(beanName)){
            registry.registerBeanDefinition(beanName,definition);
        }
        return ((BeanFactory)registry).getBean(beanName,type);
    }

}
