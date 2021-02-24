package com.xiaoaiframework.spring.mongo.convert;
import com.xiaoaiframework.util.base.ConvertUtil;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import com.xiaoaiframework.util.type.TypeUtil;

import java.util.*;

/**
 * 集合对象转换器
 * @author edison
 */
public class CollConvert extends GenericTypeConvert<Collection> {


    @Override
    public Collection convert(Object data, Class type) {

        Collection collection;
        if(CollUtil.isColl(data)){

            collection = ((Collection) data);
            Iterator iterator = collection.iterator();
            if(iterator.hasNext()){

                if(iterator.next().getClass() == genericType && TypeUtil.isAssignableFrom(type,data.getClass())){
                    return collection;
                }


            }

        }else {
            collection = CollUtil.create(type);
            collection.addAll(ConvertUtil.convertColl(data,genericType));
        }
        return collection;
    }



}
