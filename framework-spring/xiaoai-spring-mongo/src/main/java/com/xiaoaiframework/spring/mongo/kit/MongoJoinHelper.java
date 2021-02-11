package com.xiaoaiframework.spring.mongo.kit;

/**
 * TODO 连表查询待实现
 * mongo连表查询工具类
 * @author edison
 */
public class MongoJoinHelper {

    static ThreadLocal<JoinDescribe> JOIN_DESCRIBE = new ThreadLocal<>();


    public static void startJoin(Class slave,Object condition){
        JoinDescribe describe = new JoinDescribe();
        describe.slave = slave;
        describe.condition = condition;
        JOIN_DESCRIBE.set(describe);
    }

    public static class JoinDescribe{

        /**
         * 从表
         */
        Class slave;


        /**
         * 条件 on
         */
        Object condition;


        public void setSlave(Class slave) {
            this.slave = slave;
        }

        public Class getSlave() {
            return slave;
        }

        public Object getCondition() {
            return condition;
        }

        public void setCondition(Object condition) {
            this.condition = condition;
        }
    }
}
