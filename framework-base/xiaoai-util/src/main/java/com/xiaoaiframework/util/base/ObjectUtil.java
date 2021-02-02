package com.xiaoaiframework.util.base;


import com.xiaoaiframework.util.coll.CollUtil;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author edison
 */
public class ObjectUtil {


    /**
     * 属性复制
     * @param source
     * @param target
     */
    public static void copyProperties(Object source,Object target){

        Field[] fields = ReflectUtil.getDeclaredFields(source);

        for (Field field : fields) {
            try {
                ReflectUtil.setFieldValue(target,field.getName(),ReflectUtil.getFieldValue(source,field));
            } catch (Exception e) {
                continue;
                //ignore...
            }
        }

    }
    
    public static boolean isPrimitive(Object obj){
        
        if(obj.getClass().isPrimitive()){
            return true;
        }
        return false;
    }

    /**
     * 是否为数字类型
     * @param obj
     * @return
     */
    public static boolean isNumber(Object obj){

        if(obj instanceof Number){
            return true;
        }
        return false;
    }

    /**
     * 是否为数字类型
     * @param c
     * @return
     */
    public static boolean isNumber(Class c){

        if(c.equals(Number.class)){
            return true;
        }
        return false;
    }

    /**
     * 是否为字符序列
     * @param obj
     * @return
     */
    public static boolean isCharSequence(Object obj){
        
        if(obj instanceof CharSequence){
            return true;
        }
        return false;
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }


    public static boolean isEmpty(Object o){

        if(o == null){
            return true;
        }else if(o instanceof String){
            return o.toString().isEmpty();
        }else if(o instanceof Collection){
            return CollUtil.isEmpty((Collection) o);
        }
        return false;
    }
}
