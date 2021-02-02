package com.xiaoaiframework.spring.mongo.convert;
import org.bson.json.Converter;

import java.util.Collection;
import java.util.List;

/**
 * 对象转换
 * @author edison
 */
public class CollConvert implements TypeConvert {


    @Override
    public List convert(Object data, Class target) {




        return null;
    }

    @Override
    public boolean canConvert(Object data, Class target) {

        return false;
    }
}
