package com.xiaoaiframework.util.bean;

import java.lang.reflect.Field;

/**
 * @author edison
 */
public class ObjectUtil {


    public static void copyProperties(Object source,Object target){

        Field[] fields = ReflectUtil.getFields(source);

        for (Field field : fields) {
            ReflectUtil.setFieldValue(target,field.getName(),ReflectUtil.getFieldValue(source,field));
        }

    }

}
