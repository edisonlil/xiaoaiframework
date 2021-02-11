package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.util.base.ConvertUtil;

/**
 * @author edison
 */
public class OtherConvert implements TypeConvert {


    @Override
    public Object convert(Object data, Class type) {
        return ConvertUtil.convert(data,type);
    }


}
