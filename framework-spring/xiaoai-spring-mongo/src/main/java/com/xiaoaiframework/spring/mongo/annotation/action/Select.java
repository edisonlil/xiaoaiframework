package com.xiaoaiframework.spring.mongo.annotation.action;

import java.lang.annotation.*;

/**
 * 查询接口
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface Select {

    /**
     * 返回的实际类型
     * @return
     */
    Class rawType() default Void.class;


}
