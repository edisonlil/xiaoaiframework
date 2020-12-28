package com.xiaoaiframework.util.base;


import java.lang.reflect.Field;

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

        Field[] fields = ReflectUtil.getFields(source);

        for (Field field : fields) {
            ReflectUtil.setFieldValue(target,field.getName(),ReflectUtil.getFieldValue(source,field));
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
    
}
