package com.xiaoaiframework.spring.rabbitmq.client;

import com.alibaba.fastjson.JSONObject;


import com.xiaoaiframework.core.base.PageResultBean;
import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.spring.rabbitmq.annotation.RpcClientMethod;
import com.xiaoaiframework.spring.rabbitmq.constant.RpcType;
import com.xiaoaiframework.spring.rabbitmq.decoder.Decoder;
import com.xiaoaiframework.spring.rabbitmq.decoder.impl.SimpleDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.xiaoaiframework.spring.rabbitmq.constant.RpcType.REPLY;


/**
 * RpcClient代理。
 * @author edison
 */
public class RpcClientProxy implements InvocationHandler {


    private final static Logger LOGGER = LoggerFactory.getLogger(RpcClientProxy.class);

    private final static Decoder DECODER = new SimpleDecoder();

    private final String rpcName;
    private final Class<?> rpcClientInterface;
    private final RabbitTemplate defaultTemplate;
    private final RabbitTemplate replyTemplate;


    /**
     * 简单的消息监听器
     */
    private final SimpleMessageListenerContainer messageListenerContainer;

    public RpcClientProxy(String rpcName,Class<?> rpcClientInterface,RabbitTemplate defaultTemplate,RabbitTemplate replyTemplate,SimpleMessageListenerContainer messageListenerContainer){
        this.rpcName = rpcName;
        this.rpcClientInterface = rpcClientInterface;
        this.defaultTemplate = defaultTemplate;
        this.replyTemplate = replyTemplate;
        this.messageListenerContainer = messageListenerContainer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取方法注解
        RpcClientMethod rpcClientMethod = method.getAnnotation(RpcClientMethod.class);
        if (rpcClientMethod == null) {
            //TODO 后续可做熔断操作。
            return method.invoke(this, args);
        }
        RpcType methodRpcType = rpcClientMethod.type();

        //返回值类型限制
        if (methodRpcType != REPLY && method.getReturnType() != void.class) {
            throw new RuntimeException("ASYNC-RpcClient 返回类型只能为 void, Class: " + this.rpcClientInterface.getName() + ", Method: " + method.getName());
        }else if (methodRpcType == REPLY && (method.getReturnType() != ResultBean.class && method.getReturnType() != PageResultBean.class)) {
            throw new RuntimeException("SYNC-RpcClient 返回类型只能为 ResultBean 或者 PageResultBean, Class: " + this.rpcClientInterface.getName() + ", Method: " + method.getName());
        }

        // 未初始化完成
        if (methodRpcType == REPLY && !this.messageListenerContainer.isRunning()) {
            LOGGER.warn("内部rpc，监听器没启动");
            return ResultBean.fail().setMessage("内部rpc，监听器没启动");
        }

        String methodName = rpcClientMethod.name();
        if (StringUtils.isEmpty(methodName)) {
            methodName = method.getName();
        }
        // 组装data
        JSONObject data = new JSONObject();
        //得到方法的参数
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            data.put(String.valueOf(i), args[i]);
        }

        JSONObject paramData = new JSONObject();
        paramData.put("command", methodName);
        paramData.put("data", data);

        //要发送的消息
        String message = paramData.toJSONString();
        Object result = null;
        try{
            switch (rpcClientMethod.type()){
                case DIRECT:
                    directSend(message);
                    break;
                case DELAY:
                    delaySend(message,rpcClientMethod);
                    break;
                case FANOUT:
                    fanoutSend(message,methodName);
                    break;
                case REPLY:
                    result = replySend(message,method);
                    break;
                case TOPIC:
                    topicSend(message);
                    break;
                default:
                    throw new RuntimeException("rpc type invalid...");
            }
            LOGGER.debug(rpcClientMethod.type().getName() + "-RpcClient-" + this.rpcName + ", Method: " + methodName + " Call Success, Param: " + message);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    private Object replySend(String message,Method method){

        // 发起请求并返回结果
        long start = System.currentTimeMillis();
        Object resultJsonStr = replyTemplate.convertSendAndReceive(message);
        if (resultJsonStr == null) {
            // 无返回任何结果，说明服务器负载过高，没有及时处理请求，导致超时
            LOGGER.error("Duration: " + (System.currentTimeMillis() - start) + "ms, " + "-ReplyRpcClient-" + this.rpcName + ", Method: " + method.getName() + " Service Unavailable, Param: " + message);
            throw new RuntimeException("请求超时");
        }

        // 获取调用结果的状态
        JSONObject resultJson = JSONObject.parseObject(resultJsonStr.toString());
        JSONObject _data = resultJson.getJSONObject("_data");

        return DECODER.decode(_data.toJSONString(),method.getReturnType());
    }

    private void directSend(String message){
        defaultTemplate.convertAndSend("simple.rpc.direct",this.rpcName+".direct",message);
    }

    private void fanoutSend(String message,String methodName){
        defaultTemplate.convertAndSend(rpcName+".fanout","",message);
    }

    private void topicSend(String message){
        //TODO 待实现
        throw new RuntimeException("topic模式未实现...");
        //defaultTemplate.convertAndSend(this.rpcName+".topic","",message);
    }

    private void delaySend(String message,RpcClientMethod rpcClientMethod){
        defaultTemplate.convertAndSend("simple.rpc.delay", this.rpcName + ".delay", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay",
                        rpcClientMethod.delayTimeUnit().toMillis(rpcClientMethod.delayTime()));
                return message;
            }
        });
    }
}
