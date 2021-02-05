package com.xiaoaiframework.spring.mongo.convert;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.xiaoaiframework.core.type.CollType;
import com.xiaoaiframework.core.type.JavaType;
import com.xiaoaiframework.util.base.ConvertUtil;
import com.xiaoaiframework.util.coll.CollUtil;

import java.util.*;

/**
 * 集合对象转换器
 * @author edison
 */
public class CollConvert implements TypeConvert<Collection> {


    @Override
    public Collection convert(Object data, JavaType type) {

        CollType collType = (CollType) type;
        Class<Collection> cc = collType.getType();
        Collection collection = CollUtil.create(cc);
        collection.addAll(ConvertUtil.convertColl(data,collType.getElementType()));
        return collection;
    }

    @Override
    public boolean match(JavaType type) {

        if(type instanceof CollType){
            return true;
        }
        return false;
    }


}
