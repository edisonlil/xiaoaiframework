package com.xiaoaiframework.spring.rabbitmq.constant;

/**
 * 调用类型
 * @author edison
 */
public enum RpcType {

    /**
     * 回复模式(实际意义上RPC模式)
     */
    REPLY(0,"REPLY"),

    /**
     * 简单模式
     */
    DIRECT(1,"DIRECT"),

    /**
     * 延时模式
     */
    DELAY(2,"DELAY"),

    /**
     * 广播模式
     */
    @Deprecated
    FANOUT(3,"FANOUT"),

    /**
     * 主题模式
     * @deprecated 没有实现
     */
    @Deprecated
    TOPIC(4,"TOPIC")

    ;



    private int type;
    private String name;

    RpcType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static RpcType getRpcType(Integer type) {
        if (type == null) {
            return REPLY;
        }
        for (RpcType e : RpcType.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return REPLY;
    }

}
