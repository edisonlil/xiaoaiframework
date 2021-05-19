package com.xiaoaiframework.spring.rabbitmq;

/**
 * 消息体
 * @author edison
 */
public class RpcMessage<T> {

    /**
     * 消息体
     */
    T body;

    /**
     * 消息头部
     */
    MessageHeaders headers = new MessageHeaders();


    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public MessageHeaders getHeaders() {
        return headers;
    }
}


