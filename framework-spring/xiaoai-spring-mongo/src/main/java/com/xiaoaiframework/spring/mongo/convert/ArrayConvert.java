package com.xiaoaiframework.spring.mongo.convert;


import com.xiaoaiframework.util.base.ConvertUtil;

/**
 * @author edison
 */
public class ArrayConvert extends GenericTypeConvert<Object[]> {



    @Override
    public Object[] convert(Object data, Class type) {

        return ConvertUtil.convertArray(data,genericType);
    }


}
