package com.xiaoaiframework.util.coll;

import java.awt.*;
import java.util.ArrayList;
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

        if(obj instanceof Class){
            return Collection.class.isAssignableFrom(obj.getClass());
        }

        //是否为集合类型
        if(obj instanceof Collection){
            return true;
        }

        return false;
    }

    /**
     * 是否为map类型
     * @param obj
     * @return
     */
    public static boolean isMap(Object obj){

        if(obj instanceof Class){
            return Map.class.isAssignableFrom(obj.getClass());
        }

        if(obj instanceof Map){
            return true;
        }

        return false;
    }


    public static <E> ArrayList<E> newArrayList(Collection<E> coll){
        ArrayList<E> list = new ArrayList(coll.size());
        list.addAll(coll);
        return list;

    }

    public static <E>HashSet<E> newHashSet(E... es){

        HashSet<E> set = new HashSet(es.length);
        for (E e : es) { set.add(e); }

        return set;

    }

    public static boolean isEmpty(Collection c){
        if(c == null || c.isEmpty()){
            return true;
        }

        return false;
    }
}
