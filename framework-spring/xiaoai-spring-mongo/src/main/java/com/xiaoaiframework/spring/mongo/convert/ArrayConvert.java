package com.xiaoaiframework.spring.mongo.convert;


import com.xiaoaiframework.core.type.ArrayType;
import com.xiaoaiframework.core.type.JavaType;
import com.xiaoaiframework.util.base.ConvertUtil;

/**
 * @author edison
 */
public class ArrayConvert implements TypeConvert<Object[]> {


    @Override
    public Object[] convert(Object data, JavaType type) {

        ArrayType arrayType = (ArrayType) type;
        return ConvertUtil.convertArray(data,arrayType.getElementType());
    }

    @Override
    public boolean match(JavaType type) {
        return type.getType().isArray();
    }
}
