package com.xiaoaiframework.spring.mongo.convert;
import com.xiaoaiframework.util.base.ConvertUtil;
import com.xiaoaiframework.util.coll.CollUtil;

import java.util.*;

/**
 * 集合对象转换器
 * @author edison
 */
public class CollConvert extends GenericTypeConvert<Collection> {


    @Override
    public Collection convert(Object data, Class type) {

        Collection collection = CollUtil.create(type);
        collection.addAll(ConvertUtil.convertColl(data,genericType));
        return collection;
    }



}
