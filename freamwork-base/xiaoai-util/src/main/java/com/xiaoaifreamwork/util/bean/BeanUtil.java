package com.xiaoaifreamwork.util.bean;

import com.xiaoaifreamwork.util.base.StrUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * bean工具类
 * @author edison
 */
public class BeanUtil {


    /**
     * 对象转Map
     *
     * @param bean              bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue){


        Field[] fields = ReflectUtil.getDeclaredFields(bean);
        Map<String,Object> map = new HashMap<>(fields.length);
        for (Field field : fields) {

            Object obj = ReflectUtil.getDeclaredFieldValue(bean,field);
            if(ignoreNullValue && obj == null){
                continue;
            }
            String fieldName = isToUnderlineCase ? StrUtil.toUnderlineCase(field.getName()):field.getName();
            map.put(fieldName,obj);
        }

        return map;
    }
    
    /**
     * Map转换为Bean对象
     *
     * @param <T>           Bean类型
     * @param map           {@link Map}
     * @param beanClass     Bean Class
     * @param isIgnoreError 是否忽略注入错误
     * @return Bean
     */
    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError){

        T t = ReflectUtil.newInstance(beanClass);
        if(t == null){
            return null;
        }



        map.forEach((k,v)->{

            try {
                ReflectUtil.setDeclaredFieldValue(t, k.toString(), v);
            }catch (Exception e){
                if(isIgnoreError){ return; }
                e.printStackTrace();
            }
        });


        return null;
    }
}
