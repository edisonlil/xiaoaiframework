package com.xiaoaiframework.util.base;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 反射工具类
 * @author edison
 */
public class ReflectUtil {


    public static Field[] getDeclaredFields(Class c){
        return c.getDeclaredFields();
    }


    public static Field[] getDeclaredFields(Object obj){
        return getDeclaredFields(obj.getClass());
    }

    public static Field[] getFields(Object obj){
        return obj.getClass().getFields();
    }
    public static Object getFieldValue(Object obj,String fieldName,boolean ignoreNoSuchField){

        try {
            getFieldValue(obj, obj.getClass().getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            if(ignoreNoSuchField){return null;}
            e.printStackTrace();
        }


        return null;
    }

    public static Object getFieldValue(Object obj,Field field){

        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getDeclaredField(Object obj,String fieldName){

        try {
           return obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object obj,String fieldName,Object value) throws IllegalAccessException {
        setFieldValue(obj, getDeclaredField(obj, fieldName), value);
    }



    public static void setFieldValue(Object obj,Field field,Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static<T> T newInstance(Class<T> c,boolean ignoreCreateFail){

        T t = null;
        try {
            Constructor constructor = c.getConstructor(new Class[]{});
            constructor.setAccessible(true);
            t = (T) constructor.newInstance(new Object(){});
        } catch (NoSuchMethodException e) {
            if(ignoreCreateFail){return null;}
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            if(ignoreCreateFail){return null;}
            e.printStackTrace();
        } catch (InstantiationException e) {
            if(ignoreCreateFail){return null;}
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if(ignoreCreateFail){return null;}
            e.printStackTrace();
        }


        return t;
    }

    /**
     * 創建對象
     * @param c
     * @param <T>
     * @return
     */
    public static<T> T newInstance(Class<T> c){

        return newInstance(c,true);
    }


    public static Class forName(String className){

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    

    public static Method[] getDeclaredMethods(Class c){
        return c.getDeclaredMethods();
    }
}
