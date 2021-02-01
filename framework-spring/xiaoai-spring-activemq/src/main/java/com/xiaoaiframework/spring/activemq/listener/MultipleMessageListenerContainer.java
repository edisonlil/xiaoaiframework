package com.xiaoaiframework.spring.activemq.listener;

import org.springframework.jms.listener.AbstractPollingMessageListenerContainer;

import javax.jms.JMSException;

/**
 * @author edison
 */
public class MultipleMessageListenerContainer extends AbstractPollingMessageListenerContainer {



    @Override
    public void setConcurrency(String concurrency) {


    }

    @Override
    protected boolean sharedConnectionEnabled() {
        return false;
    }

    @Override
    protected void doInitialize() throws JMSException {

    }

    @Override
    protected void doShutdown() throws JMSException {

    }
}
