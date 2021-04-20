package com.xiaoaiframework.util.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author edison
 */
public class AnnotationUtil {

    /**
     * 修改注解的值
     * @param annotation
     * @param name
     * @param value
     */
    public static void alterValue(Annotation annotation, String name, Object value)  {

        InvocationHandler h = Proxy.getInvocationHandler(annotation);
        Field hField = null;
        try {
            hField = h.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        hField.setAccessible(true);
        Map memberValues = null;
        try {
            memberValues = (Map) hField.get(h);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        memberValues.put(name, value);
    }


    /**
     * 修改注解的值
     * @param annotations
     * @param name
     * @param value
     */
    public static void alterValues(List<? extends Annotation> annotations, String name, Object value)  {
        annotations.forEach(annotation -> {
            alterValue(annotation, name, value);
        });
    }



    public static boolean isCandidateMethod(Method targetMethod, Class<? extends Annotation> annotation){
        return targetMethod.isAnnotationPresent(annotation);
    }


    /**
     * 获取指定类的所有方法上的注解
     * @param clazz
     * @param specifyAnnotation
     * @param <T>
     * @return
     */
    public static <T extends Annotation> List<T> getMethodAnnotationInClass(Class clazz, Class<T> specifyAnnotation){

        Method[] methods =  ReflectUtil.getDeclaredMethods(clazz);
        List<T> list = new ArrayList<>(methods.length);
        for (Method declaredMethod : methods) {
            declaredMethod.setAccessible(true);
            T annotation = declaredMethod.getAnnotation(specifyAnnotation);
            if(annotation != null){
                list.add(annotation);
            }
        }
        return list;
    }


    /**
     * 注解数组里面是否有指定注解
     * @param annotations
     * @param annotation
     * @return
     */
    public static boolean match(Annotation[] annotations,Class<? extends Annotation> annotation){

        for (Annotation item : annotations) {

            if(item.annotationType().equals(annotation)){
                return true;
            }

        }

        return false;


    }
}
