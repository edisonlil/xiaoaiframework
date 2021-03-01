package com.xiaoaiframework.util.coll;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.awt.*;
import java.util.*;

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
            return Collection.class.isAssignableFrom((Class<?>) obj);
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
            return Map.class.isAssignableFrom((Class<?>) obj);
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


    /**
     * 创建新的集合对象
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     * @since 3.0.8
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(Class<?> collectionType) {
        Collection<T> list;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            // 抽象集合默认使用ArrayList
            list = new ArrayList<>();
        }

        // Set
        else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>();
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
        }

        // List
        else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        }

        // Others，直接实例化
        else {
            try {
                list = (Collection<T>) ReflectUtil.newInstance(collectionType);
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        return list;
    }

}
