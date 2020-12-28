package com.xiaoaiframework.util.coll;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * 集合工具类
 * @author edison
 */
public class CollUtil {

    /**
     * 判断是否为集合类型
     * @param obj
     * @return
     */
    public static boolean isColl(Object obj){

        //是否为集合类型
        if(obj instanceof Collection){
            return true;
        }

        return false;
    }

    /**
     * 判断是否为集合类型
     * @param c
     * @return
     */
    public static boolean isColl(Class c){
        return Collection.class.isAssignableFrom(c);
    }

    /**
     * 是否为map类型
     * @param obj
     * @return
     */
    public static boolean isMap(Object obj){

        if(obj instanceof Map){
            return true;
        }

        return false;
    }


    public static <E>HashSet<E> newHashSet(E... es){

        HashSet<E> set = new HashSet(es.length);
        for (E e : es) { set.add(e); }

        return set;

    }
}
