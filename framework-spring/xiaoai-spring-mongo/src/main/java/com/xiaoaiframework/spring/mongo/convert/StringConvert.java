package com.xiaoaiframework.spring.mongo.convert;

/**
 * @author edison
 */
public class StringConvert implements TypeConvert{


    @Override
    public Object convert(Object data, Class type) {
        return data.toString();
    }

}
