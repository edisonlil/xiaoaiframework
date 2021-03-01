package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.util.base.ConvertUtil;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.coll.CollUtil;

/**
 * @author edison
 */
public class OtherConvert implements TypeConvert {


    @Override
    public Object convert(Object data, Class type) {

        if(data.getClass().equals(type)){
            return data;
        }

        return ConvertUtil.convert(data,type);
    }

    @Override
    public boolean canConvert(Object data) {

        if(CollUtil.isColl(data) || ObjectUtil.isArray(data) || ObjectUtil.isPrimitiveWrapper(data) || ObjectUtil.isPrimitive(data)){
            return false;
        }

        return true;

    }
}
