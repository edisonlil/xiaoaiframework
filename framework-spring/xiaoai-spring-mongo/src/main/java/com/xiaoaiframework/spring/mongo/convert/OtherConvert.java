package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.core.type.JavaType;
import com.xiaoaiframework.util.base.ConvertUtil;

/**
 * @author edison
 */
public class OtherConvert implements TypeConvert {


    @Override
    public Object convert(Object data, JavaType type) {
        return ConvertUtil.convert(data,type.getType());
    }



    @Override
    public boolean match(JavaType type) {
        return type.getType().getClassLoader() != null;
    }
}
