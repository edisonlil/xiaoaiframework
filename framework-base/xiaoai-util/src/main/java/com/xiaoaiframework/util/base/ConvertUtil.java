package com.xiaoaiframework.util.base;

import cn.hutool.core.convert.Convert;
import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.coll.CollUtil;

import java.util.*;

/**
 * @author edison
 */
public class ConvertUtil {


    /**
     * 对象转换
     * @param obj
     * @param targetType
     * @return
     */
    public static <T>T convert(Object obj,Class<T> targetType){


        if(CollUtil.isMap(targetType)){
           return (T) BeanUtil.beanToMap(obj);
        }else{
            return Convert.convert(targetType,obj);
        }

    }



    public static <T>List<T> convertColl(Object data, Class<T> elementType){

        Object[] arr = new Object[0];
        if(CollUtil.isColl(data)){
            arr = ((Collection)data).toArray();
        }else if(data.getClass().isArray()){
            arr = (Object[]) data;
        }else{
            arr[0] = data;
        }

        List<T> list = new ArrayList();
        for (Object val : arr){
            list.add(convert(val,elementType));
        }

        return list;
    }

    public static <T>T[] convertArray(Object data, Class<T> elementType){

        Object[] arr = new Object[0];
        if(CollUtil.isColl(data)){
            arr = ((Collection)data).toArray();
        }else if(data.getClass().isArray()){
            arr = (Object[]) data;
        }else{
            arr[0] = data;
        }

        T[] ts = (T[]) new Object[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ts[i] = convert(arr[i],elementType);
        }
        return ts;
    }
}
