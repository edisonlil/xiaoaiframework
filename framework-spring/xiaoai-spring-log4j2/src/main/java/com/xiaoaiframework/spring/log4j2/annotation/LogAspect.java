package com.xiaoaiframework.spring.log4j2.annotation;

import java.lang.annotation.*;

/**
 * 收集日志注解。作用于方法
 *
 * @author srillia(srillia@coldweaponera.com)
 * @version 1.0.0
 * @since 15:17 2017/07/03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LogAspect {

    /**
     * 模块名，事件名
     *
     * @return 模块名
     */
    String module() default "";

    /**
     * 操作详情
     *
     * @return 操作说明
     */
    String option() default "";

}