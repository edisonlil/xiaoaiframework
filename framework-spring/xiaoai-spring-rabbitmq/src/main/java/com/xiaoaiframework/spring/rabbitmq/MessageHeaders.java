package com.xiaoaiframework.spring.rabbitmq;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MessageHeaders extends HashMap<String,Object> {

    /**
     * 消息延时发送时间
     */
    String DELAY_TIME = "DELAY-TIME";

    /**
     * 消息延时发送时间单位
     * @return
     */
    String DELAY_TIME_UNIT = "DELAY-TIME-UNIT";



    public void setDelayTime(long delayTime){
        put(DELAY_TIME,delayTime);
    }


    public Long getDelayTime(){
        return (Long) get(DELAY_TIME);
    }


    public void setDelayTimeUnit(TimeUnit timeUnit){
        put(DELAY_TIME_UNIT,timeUnit);
    }


    public TimeUnit getDelayTimeUnit(){
        return (TimeUnit) get(DELAY_TIME_UNIT);
    }
}
