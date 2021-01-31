package com.xiaoaiframework.spring.activemq.autoconfigure;

import com.xiaoaiframework.spring.activemq.DataSource;
import com.xiaoaiframework.spring.activemq.properties.ActiveMQActivemqProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ注册器
 * @author edison
 */
public class ActiveMQConnectionRegistrar implements BeanFactoryAware {

    BeanFactory factory;


    public void registrar(DataSource source){
        //创建监听容器工厂 如果没有连接工厂则创建.
        createJmsListenerContainerFactory(source);
        //创建发送模板
        createJmsTemplate(source);
    }


    /**
     * MQ连接工厂
     * @param source
     * @return
     */
    private ActiveMQConnectionFactory createActiveMQConnectionFactory(DataSource source){
        BeanDefinition beanDefinition = buildBeanDefinition(ActiveMQConnectionFactory.class,null,source.getUsername(),source.getPassword(),source.getBrokerURl());
        beanDefinition.setPrimary(source.isPrimary());
        return beanRegister(source.getId()+"ActiveMQConnectionFactory",beanDefinition);
    }


    /**
     * 监听容器工厂
     * @param source
     * @return
     */
    private JmsListenerContainerFactory createJmsListenerContainerFactory(DataSource source) {
        Map<String,Object> map = new HashMap<>();
        map.put("connectionFactory",createActiveMQConnectionFactory(source));
        map.put("pubSubDomain",source.isPubSubDomain());
        BeanDefinition beanDefinition = buildBeanDefinition(DefaultJmsListenerContainerFactory.class,map);
        return beanRegister(source.getId()+"JmsListenerContainerFactory", beanDefinition);
    }

    /**
     * jms发送模板
     * @param source
     * @return
     */
    private JmsOperations createJmsTemplate(DataSource source) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("connectionFactory",createActiveMQConnectionFactory(source));
        map.put("pubSubDomain",source.isPubSubDomain());
        BeanDefinition beanDefinition = buildBeanDefinition(JmsTemplate.class,map);
        return beanRegister(source.getId()+"JmsTemplate", beanDefinition);
    }




    /**
     * 注册Bean
     * @param beanName
     * @param definition
     */
    <T>T beanRegister(String beanName,BeanDefinition definition){

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;

        if(!registry.isBeanNameInUse(beanName)){
            registry.registerBeanDefinition(beanName,definition);
        }
        return (T)factory.getBean(beanName);

    }

    BeanDefinition buildBeanDefinition(Class bean, Map<String,Object> property,Object... arg){
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(bean);
        property.forEach((k,v)->{
            definitionBuilder.addPropertyValue(k,v);
        });

        for (Object val : arg) {
            definitionBuilder.addConstructorArgValue(val);
        }
        return definitionBuilder.getBeanDefinition();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        factory = beanFactory;
    }
}
