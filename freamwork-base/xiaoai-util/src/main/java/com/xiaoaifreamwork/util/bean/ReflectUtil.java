package com.xiaoaifreamwork.util.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 反射工具类
 * @author edison
 */
public class ReflectUtil {

    
    public static Field[] getDeclaredFields(Object obj){
        return obj.getClass().getDeclaredFields();
    }

    public static Object getDeclaredFieldValue(Object obj,String fieldName,boolean ignoreNoSuchField){

        try {
            getDeclaredFieldValue(obj, obj.getClass().getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            if(ignoreNoSuchField){return null;}
            e.printStackTrace();
        }


        return null;
    }

    public static Object getDeclaredFieldValue(Object obj,Field field){

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

    public static void setDeclaredFieldValue(Object obj,String fieldName,Object value){
        setDeclaredFieldValue(obj, getDeclaredField(obj, fieldName), value);
        
    }



    public static void setDeclaredFieldValue(Object obj,Field field,Object value){

        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
      
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

    public static<T> T newInstance(Class<T> c){

        return newInstance(c,true);
    }

}
