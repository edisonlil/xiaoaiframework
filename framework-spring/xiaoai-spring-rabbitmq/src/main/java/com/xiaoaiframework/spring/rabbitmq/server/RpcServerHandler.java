package com.xiaoaiframework.spring.rabbitmq.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import com.xiaoaiframework.core.base.PageResultBean;
import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.spring.rabbitmq.annotation.RpcServerMethod;
import com.xiaoaiframework.spring.rabbitmq.constant.RpcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author edison
 */
public class RpcServerHandler implements ChannelAwareMessageListener, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private final static Map<String, FastMethod> FAST_METHOD_CACHE = new ConcurrentHashMap<>();

    @Value("${spring.rabbitmq.slow-call-time:1000}")
    private int slowCallTime;

    private final Object rpcServerBean;
    private final String rpcName;
    private final RpcType rpcType;

    RpcServerHandler(Object rpcServerBean, String rpcName, RpcType rpcType){
        this.rpcServerBean = rpcServerBean;
        this.rpcName = rpcName;
        this.rpcType = rpcType;
    }


    String getFastMethodCacheKey(String methodName){
        return this.rpcType.getName()+":"+this.rpcName+":"+methodName;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        MessageProperties messageProperties = message.getMessageProperties();
        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
        // 构建返回JSON值
        JSONObject resultJson = new JSONObject();
        // 组装参数json
        JSONObject paramData = JSON.parseObject(messageStr);
        // 获得当前command
        String command = paramData.getString("command");
        if (StringUtils.isEmpty(command)) {
            LOGGER.error("Method Invoke Exception: Command 参数为空, " + this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Received: " + messageStr);
            return;
        }
        // 获取data数据
        JSONObject data = paramData.getJSONObject("data");
        if (data == null) {
            LOGGER.error("Method Invoke Exception: Data 参数错误, " + this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Method: " + command + ", Received: " + messageStr);
            return;
        }
        try{
            long start = System.currentTimeMillis();
            Object result = execute(command,data);
            double offset = System.currentTimeMillis() - start;
            LOGGER.info("Duration: " + offset + "ms, " + this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Method: " + command + ", Received: " + messageStr);
            if (offset > this.slowCallTime) {
                LOGGER.warn("Duration: " + offset + "ms, " + this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Method: " + command + ", Call Slowing");
            }

            if (result != null) {
                resultJson.put("_data",result);
                // 构建配置
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(messageProperties.getCorrelationId())
                        .contentEncoding(StandardCharsets.UTF_8.name()).contentType(messageProperties.getContentType()).build();
                //反馈消息
                channel.basicPublish(messageProperties.getReplyToAddress().getExchangeName()
                        ,messageProperties.getReplyToAddress().getRoutingKey()
                        , replyProps,resultJson.toJSONString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOGGER.error(this.rpcType.getName() + "-RpcServer-" + this.rpcName + " Exception! Received: " + messageStr);
            e.printStackTrace();
        } finally {
            // 确认处理任务
            if (messageProperties != null) {
                channel.basicAck(messageProperties.getDeliveryTag(), false);
            }
        }
    }


    /**
     * 执行方法
     * @param command
     * @param data
     * @return
     * @throws InvocationTargetException
     */
    private Object execute(String command, JSONObject data) throws InvocationTargetException {

        // 获取当前服务的反射方法调用
        String key = getFastMethodCacheKey(command);
        // 通过缓存来优化性能
        FastMethod fastMethod = FAST_METHOD_CACHE.get(key);
        if (fastMethod == null) {
            LOGGER.error(this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Method: " + command + " Not Found");
            return null;
        }
        List args = convertParamsTypes(command, data);

        if(fastMethod.getReturnType() == void.class){
            // 通过发射来调用方法
            fastMethod.invoke(this.rpcServerBean, args.toArray());
            return null;
        }else{
            // 通过发射来调用方法
            return fastMethod.invoke(this.rpcServerBean, args.toArray());
        }

    }


    private List convertParamsTypes(String command, JSONObject data) {

        List args = new ArrayList();
        Class<?> targetClazz = rpcServerBean.getClass();
        if(rpcServerBean.getClass().getName().contains("CGLIB")) {
            targetClazz = rpcServerBean.getClass().getSuperclass();
        }
        for (Method targetMethod : targetClazz.getMethods()) {

            if (targetMethod.getName().equals(command)) {

                Parameter[] parameters = targetMethod.getParameters();
                for (int j = 0; j < parameters.length; j++) {
                    String param = data.getString(String.valueOf(j));
                    Type type = parameters[j].getParameterizedType();
                    if(!StringUtils.isEmpty(param)) {
                        try {
                            args.add(JSON.parseObject(param, type));
                        } catch (JSONException e) {
                            args.add(JSON.parseObject(JSON.toJSONString(param), type));
                        }
                    } else {
                        args.add(param);
                    }
                }
            }
        }
        return args;
    }


    private Class<?> getRpcServerClass() {

        //初始化所有接口
        Class<?> rpcServerClass = this.rpcServerBean.getClass();

        //如果当前类是代理类的话则获取到它的父类。
        if(this.rpcServerBean.getClass().getName().contains("CGLIB")){
            rpcServerClass = this.rpcServerBean.getClass().getSuperclass();
        }

        return rpcServerClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //初始化所有接口
        Class<?> rpcServerClass = getRpcServerClass();

        Method[] methods = rpcServerClass.getDeclaredMethods();
        for (Method method : methods){

            if(method == null || !method.isAnnotationPresent(RpcServerMethod.class)){
                return;
            }

            String methodName = method.getAnnotation(RpcServerMethod.class).value();
            if (StringUtils.isEmpty(methodName)) {
                methodName = method.getName();
            }

            String key = getFastMethodCacheKey(methodName);
            if (FAST_METHOD_CACHE.containsKey(key)) {
                throw new RuntimeException("Class: " + rpcServerClass.getName() + ", Method: " + methodName + " 重复");
            }
            FastMethod fastMethod = FastClass.create(rpcServerClass).getMethod(method.getName(),method.getParameterTypes());
            if (fastMethod == null) {
                throw new RuntimeException("Class: " + rpcServerClass.getName() + ", Method: " + method.getName() + " Invoke Exception");
            }
//TODO 支持其他类型的返回值参数
            if (fastMethod.getReturnType() != ResultBean.class && fastMethod.getReturnType() != PageResultBean.class && fastMethod.getReturnType() != void.class) {
                throw new RuntimeException("返回类型只能为 ResultBean, PageResultBean 或者 void, Class: " + rpcServerClass.getName() + ", Method: " + fastMethod.getName() + ", ReturnType: " + fastMethod.getReturnType());
            }

            FAST_METHOD_CACHE.put(key, fastMethod);
            LOGGER.debug(this.rpcType.getName() + "-RpcServer-" + this.rpcName + ", Method: " + methodName + " 已启动");
        }
        LOGGER.info(this.rpcType.getName() + "-RpcServerHandler-" + this.rpcName + " 已启动");

    }
}
