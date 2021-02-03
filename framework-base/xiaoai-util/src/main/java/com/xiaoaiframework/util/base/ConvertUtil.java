package com.xiaoaiframework.util.base;

import com.xiaoaiframework.util.bean.BeanUtil;

import java.util.*;
import java.util.Map;

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
    public static Object convert(Object obj,Class targetType){

        if(ObjectUtil.isPrimitive(obj)){
            throw new RuntimeException("无法转换基本类型的值");
        }

        if(obj instanceof Map){
           return BeanUtil.beanToMap(obj);
        }


        Object o = ReflectUtil.newInstance(targetType);
        ObjectUtil.copyProperties(obj,o);
        return o;

    }


}
