package com.xiaoaiframework.spring.activemq.autoconfigure;

import com.xiaoaiframework.spring.activemq.properties.ActiveMQActivemqProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author edison
 */
@EnableConfigurationProperties(ActiveMQActivemqProperties.class)
@ConditionalOnBean(ActiveMQActivemqProperties.class)
public class MultipleActiveMQAutoConfiguration {


    @Bean
    public ActiveMQConnectionRegistrar registrar(){
        return new ActiveMQConnectionRegistrar();
    }


    @Bean
    public ActiveMQActivemqPropertiesBeanPostProcessor activeMQActivemqPropertiesBeanPostProcessor(){
        return new ActiveMQActivemqPropertiesBeanPostProcessor();
    }

}
