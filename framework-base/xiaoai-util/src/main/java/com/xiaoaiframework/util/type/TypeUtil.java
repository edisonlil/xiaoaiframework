package com.xiaoaiframework.util.type;

import com.xiaoaiframework.core.type.*;
import com.xiaoaiframework.util.coll.CollUtil;

import java.lang.reflect.*;


/**
 * @author edison
 */
public class TypeUtil {


    /**
     * 判断指定sub类是否是parent的子类
     * @param parent
     * @param sub
     * @return
     */
    public static boolean isAssignableFrom(Class parent,Class sub){
        return parent.isAssignableFrom(sub);
    }


    public static <T extends JavaType> T getJavaType(Class c){

        if (c.isArray()) {
            return (T) new ArrayType(c);
        }else if(CollUtil.isMap(c)){
            return (T) new MapType(c);
        }else if(CollUtil.isColl(c)) {
            return (T) new CollType(c);
        }else{
            return (T) new DefaultType(c);
        }
    }

    /**
     * 获得Type对应的原始类
     *
     * @param type {@link Type}
     * @return 原始类，如果无法获取原始类，返回{@code null}
     */
    public static Class<?> getClass(Type type) {
        if (null != type) {
            if (type instanceof Class) {
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            } else if (type instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable<?>) type).getBounds()[0];
            } else if (type instanceof WildcardType) {
                final Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getClass(upperBounds[0]);
                }
            }
        }
        return null;
    }


    /**
     * 获取字段对应的Type类型<br>
     * 方法优先获取GenericType，获取不到则获取Type
     *
     * @param field 字段
     * @return {@link Type}，可能为{@code null}
     */
    public static Type getType(Field field) {
        if (null == field) {
            return null;
        }
        Type type = field.getGenericType();
        if (null == type) {
            type = field.getType();
        }
        return type;
    }

    /**
     * 获得Field对应的原始类
     *
     * @param field {@link Field}
     * @return 原始类，如果无法获取原始类，返回{@code null}
     * @since 3.1.2
     */
    public static Class<?> getClass(Field field) {
        return null == field ? null : field.getType();
    }
}
