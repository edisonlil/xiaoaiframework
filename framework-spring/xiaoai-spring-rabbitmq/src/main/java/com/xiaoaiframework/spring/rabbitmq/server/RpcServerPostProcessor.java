package com.xiaoaiframework.spring.rabbitmq.server;


import com.xiaoaiframework.spring.rabbitmq.annotation.RpcServer;
import com.xiaoaiframework.spring.rabbitmq.constant.RpcType;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.xiaoaiframework.spring.rabbitmq.constant.RpcType.FANOUT;
import static com.xiaoaiframework.spring.rabbitmq.constant.RpcType.REPLY;


/**
 * @author edison
 */
@Component
public class RpcServerPostProcessor implements BeanPostProcessor {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    @Lazy
    private ConnectionFactory connectionFactory;

    private FanoutExchange fanoutExchange;
    private DirectExchange directExchange;
    private CustomExchange delayExchange;
    private DirectExchange replyDirectExchange;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> rpcServerClass = bean.getClass();
        RpcServer rpcServer =  rpcServerClass.getAnnotation(RpcServer.class);
        if (rpcServerClass.getName().contains("CGLIB")){
            rpcServer = rpcServerClass.getSuperclass().getAnnotation(RpcServer.class);
        }

        if(rpcServer == null){
            return bean;
        }
        rpcServerStart(bean,rpcServer);
        return bean;

    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    /**
     * 启动服务监听
     * @param rpcServerBean
     * @param rpcServer
     */
    private void rpcServerStart(Object rpcServerBean,RpcServer rpcServer){

        String rpcName = rpcServer.value();
        RpcType[] rpcTypes = rpcServer.types();
        for (RpcType rpcType: rpcTypes){
            switch (rpcType){
                case REPLY:
                    replyMessageListenerContainer(rpcName,rpcServerBean,rpcServer);
                    break;
                default:
                    defaultMessageListenerContainer(rpcName,rpcServerBean,rpcServer,rpcType);
                    break;
            }
        }

    }


    private void replyMessageListenerContainer(String rpcName,Object rpcServerBean,RpcServer rpcServer){

        final RpcType reply = REPLY;
        Map<String, Object> params = new HashMap<>(1);
        params.put("x-message-ttl", rpcServer.xMessageTTL());
        Queue replyQueue = queue(rpcName, reply, params);
        binding(rpcName, reply, replyQueue);
        RpcServerHandler syncServerHandler = rpcServerHandler(rpcName, reply, rpcServerBean);
        messageListenerContainer(rpcName, reply, replyQueue, syncServerHandler, rpcServer.threadNum());
    }

    private void defaultMessageListenerContainer(String rpcName,Object rpcServerBean,RpcServer rpcServer,RpcType rpcType){
        Map<String, Object> params = new HashMap<>(1);
        params.put("x-queue-type", "classic");
        Queue queue = queue(rpcName, rpcType, null);
        binding(rpcName, rpcType, queue);
        RpcServerHandler directServerHandler = rpcServerHandler(rpcName, rpcType, rpcServerBean);
        messageListenerContainer(rpcName, rpcType, queue, directServerHandler, rpcServer.threadNum());
    }


    /**
     * 实例化Queue
     * @param rpcName
     * @param rpcType
     * @param params
     * @return
     */
    private Queue queue(String rpcName,RpcType rpcType,Map<String,Object> params){

        String rpcTypeName = rpcType.getName().toLowerCase();

        if(rpcType == FANOUT){
            return registerBean(this.applicationContext,rpcTypeName + "Queue-" + rpcName,Queue.class
                    ,rpcName+"."+rpcTypeName+"."+ ManagementFactory.getRuntimeMXBean().getName(),false,false,true,params);
        }

        return registerBean(this.applicationContext,rpcTypeName + "Queue-" + rpcName,Queue.class
                ,rpcName+"."+rpcTypeName,true,false,false,params);
    }


    /**
     * 实例化 Binding
     */
    private void binding(String rpcName, RpcType rpcType, Queue queue) {

        String rpcTypeName = rpcType.getName().toLowerCase();
        String queueName = queue.getName();
        String routingKey  = queueName;
        if(rpcType == FANOUT){
            routingKey = "";
        }
        registerBean(this.applicationContext, rpcTypeName + "Binding-" + rpcName,
                Binding.class, queueName,
                Binding.DestinationType.QUEUE,
                getExchange(rpcName,rpcType).getName(),
                routingKey, Collections.<String, Object>emptyMap());
    }

    /**
     * 实例化 RpcServerHandler
     */
    private RpcServerHandler rpcServerHandler(String rpcName, RpcType rpcType, Object rpcServerBean) {
        String rpcTypeName = rpcType.getName().toLowerCase();
        return registerBean(this.applicationContext, rpcTypeName + "RpcServerHandler-" + rpcName, RpcServerHandler.class, rpcServerBean, rpcName, rpcType);
    }

    /**
     * 实例化 SimpleMessageListenerContainer
     */
    private void messageListenerContainer(String rpcName, RpcType rpcType, Queue queue, RpcServerHandler rpcServerHandler, int threadNum) {
        String rpcTypeName = rpcType.getName().toLowerCase();
        SimpleMessageListenerContainer messageListenerContainer = registerBean(this.applicationContext, rpcTypeName + "MessageListenerContainer-" + rpcName, SimpleMessageListenerContainer.class, this.connectionFactory);
        messageListenerContainer.setQueueNames(queue.getName());
        messageListenerContainer.setMessageListener(rpcServerHandler);
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        messageListenerContainer.setConcurrentConsumers(threadNum);
    }



    private Exchange getExchange(String rpcName,RpcType rpcType) {

        switch (rpcType){
            case DIRECT:
                if(this.directExchange == null){
                   this.directExchange = registerBean(this.applicationContext, "directExchange", DirectExchange.class,"simple.rpc.direct",true,false);
                }
                return this.directExchange;
            case DELAY:
                if(this.delayExchange == null){
                    Map<String, Object> args = new HashMap<>(1);
                    args.put("x-delayed-type", "direct");
                    this.delayExchange = registerBean(this.applicationContext,"delayExchange",CustomExchange.class,"simple.rpc.delay","x-delayed-message",true,false,args);
                }
                return this.delayExchange;
            case FANOUT:
                if(this.fanoutExchange == null){
                    this.fanoutExchange = registerBean(this.applicationContext,"fanoutExchange",FanoutExchange.class,rpcName+".fanout",true,false);
                }
                return this.fanoutExchange;
            case REPLY:
                if(this.replyDirectExchange == null){
                    this.replyDirectExchange = registerBean(this.applicationContext, "replyDirectExchange", DirectExchange.class,"simple.rpc.reply",true,false);
                }
                return this.replyDirectExchange;
        }

        return null;
    }

    private <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, Object... args) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (args.length > 0) {
            for (Object arg : args) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) (applicationContext).getBeanFactory();
        if (beanFactory.isBeanNameInUse(name)) {
            throw new RuntimeException("BeanName: " + name + " 重复");
        }
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }



}
