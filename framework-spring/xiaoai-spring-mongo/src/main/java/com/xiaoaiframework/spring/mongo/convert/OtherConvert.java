package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.spring.mongo.type.JavaType;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;

/**
 * @author edison
 */
public class OtherConvert implements TypeConvert {


    @Override
    public Object convert(Object data, JavaType type) {

        Object o = ReflectUtil.newInstance(type.getType());
        ObjectUtil.copyProperties(data,o);
        return o;
    }



    @Override
    public boolean match(JavaType type) {
        return type.getType().getClassLoader() != null;
    }
}
