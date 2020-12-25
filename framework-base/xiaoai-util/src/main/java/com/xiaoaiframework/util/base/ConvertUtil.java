package com.xiaoaiframework.util.base;

import com.xiaoaiframework.util.bean.BeanUtil;

import java.util.*;
import java.util.Map;

/**
 * @author edison
 */
public class ConvertUtil {


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
