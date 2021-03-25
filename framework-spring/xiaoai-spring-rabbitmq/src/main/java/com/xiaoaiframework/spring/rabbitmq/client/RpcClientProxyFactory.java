package com.xiaoaiframework.spring.rabbitmq.client;


import com.xiaoaiframework.spring.rabbitmq.annotation.RpcClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.UUID;

/**
 * RpcClient代理工厂
 * @author edison
 */
public class RpcClientProxyFactory implements FactoryBean, BeanFactoryAware {


    private BeanFactory beanFactory;
    private Class<?> rpcClientInterface;
    private ConnectionFactory connectionFactory;
    private DirectExchange replyDirectExchange;


    private RpcClientProxyFactory(Class<?> rpcClientInterface){this.rpcClientInterface = rpcClientInterface;}

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getObject() throws Exception {

        RpcClient rpcClient = this.rpcClientInterface.getAnnotation(RpcClient.class);
        String rpcName = rpcClient.value();
        int replyTimeout = rpcClient.replyTimeout();
        int maxAttempts = rpcClient.maxAttempts();

        //初始化同步队列
        Queue replyQueue = replyQueue(rpcName,UUID.randomUUID().toString());
        replyBinding(rpcName, replyQueue);
        RabbitTemplate replyTemplate = replyTemplate(rpcName, replyQueue, replyTimeout, maxAttempts, getConnectionFactory());
        SimpleMessageListenerContainer replyMessageListenerContainer = replyMessageListenerContainer(rpcName, replyQueue, replyTemplate, getConnectionFactory());

        //初始化异步队列
        RabbitTemplate defaultTemplate = defaultTemplate(getConnectionFactory());

        return Proxy.newProxyInstance(this.rpcClientInterface.getClassLoader()
                ,new Class[]{this.rpcClientInterface}
        ,new RpcClientProxy(rpcName,this.rpcClientInterface,defaultTemplate,replyTemplate,replyMessageListenerContainer));
    }

    /**
     * 实例化默认DefaultTemplate
     */
    private RabbitTemplate defaultTemplate(ConnectionFactory connectionFactory) {
        //注册一个Bean
        RabbitTemplate defaultTemplate = registerBean("defaultSender", RabbitTemplate.class, connectionFactory);
        return defaultTemplate;
    }

    /**
     * 实例化默认ReplyTemplate
     * @param rpcName
     * @param replyQueue 回复队列
     * @param replyTimeout 回复超时时间
     * @param maxAttempts 最大尝试次数
     * @param connectionFactory 连接池工厂
     * @return
     */
    private RabbitTemplate replyTemplate(String rpcName, Queue replyQueue, int replyTimeout, int maxAttempts, ConnectionFactory connectionFactory) {


        //构建同步RabbitTemplate
        RabbitTemplate replySender = registerBean("replySender-"+rpcName, RabbitTemplate.class, connectionFactory);
        replySender.setExchange("simple.rpc.reply");
        replySender.setRoutingKey(rpcName+".reply");
        replySender.setDefaultReceiveQueue(rpcName+".reply");


        //定义重试策略
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(maxAttempts);
        //定义重试模板
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        //设置重试信息
        replySender.setReplyAddress(replyQueue.getName());
        replySender.setReplyTimeout(replyTimeout);
        replySender.setRetryTemplate(retryTemplate);
        return replySender;
    }


    /**
     * 实例化 ReplyMessageListenerContainer
     */
    private SimpleMessageListenerContainer replyMessageListenerContainer(String rpcName, Queue queue, RabbitTemplate syncSender, ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer replyMessageListenerContainer = registerBean("reReplyMessageListenerContainer-" + rpcName, SimpleMessageListenerContainer.class, connectionFactory);
        replyMessageListenerContainer.setQueueNames(queue.getName());
        replyMessageListenerContainer.setMessageListener(syncSender);
        return replyMessageListenerContainer;
    }

    /**
     * 实例化 replyQueue
     */
    private Queue replyQueue(String rpcName,String clientId) {
        return registerBean("reReplyQueue-" + rpcName, Queue.class, rpcName + ".reply."+clientId, false, false, true);
    }

    /**
     * 实例化 ReplyBinding
     */
    private void replyBinding(String rpcName, Queue queue) {
        registerBean("reReplyBinding-" + rpcName, Binding.class, queue.getName(), Binding.DestinationType.QUEUE, getReplyDirectExchange().getName(), queue.getName(), Collections.<String, Object>emptyMap());
    }

    /**
     * 实例化 SyncReplyDirectExchange
     * 实例化同步回复交换机
     */
    private DirectExchange getReplyDirectExchange() {

        if (this.replyDirectExchange == null) {
            this.replyDirectExchange = registerBean("reReplyDirectExchange", DirectExchange.class, "simple.rpc.re.reply", true, false);
        }
        return this.replyDirectExchange;
    }




    @Override
    public Class<?> getObjectType() {
        return this.rpcClientInterface;
    }


    private ConnectionFactory getConnectionFactory(){

        if(this.connectionFactory == null){
            this.connectionFactory = this.beanFactory.getBean(ConnectionFactory.class);
        }
        return this.connectionFactory;
    }


    /**
     * 注册Bean到Spring上下文
     * @param name
     * @param clazz
     * @param args
     * @param <T>
     * @return
     */
    private <T>T registerBean(String name,Class<T> clazz,Object... args){

        //通过建造者模式去创建一个BeanDefinition
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                //设置Bean的构造函数传入的参数值
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        //获取未加工的Bean定义。
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) this.beanFactory;
        //查看beanName是否已经在注册表中存在
        if (beanDefinitionRegistry.isBeanNameInUse(name)) {
            return this.beanFactory.getBean(name, clazz);
        }

        //往注册表中注册一个新的 BeanDefinition 实例
        beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
        return this.beanFactory.getBean(name, clazz);
    }




}
