package com.xiaoaiframework.spring.mongo.annotation;

import java.lang.annotation.*;

/**
 * Indicates that an annotation annotated class is a  mongo's conditional query annotation,
 * and the annotation marked by this annotation is mongo's conditional query annotation.
 * @author edison
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
public @interface ICriteria {
}
