package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.util.base.ObjectUtil;
import org.bson.json.Converter;

import java.util.Collection;
import java.util.List;

/**
 * 对象转换
 * @author edison
 */
public class CollConvert<O> implements TypeConvert<Collection,Collection> {


    @Override
    public Collection convert(Collection data, Class target) {




        return null;
    }

    @Override
    public boolean canConvert(Collection data, Class target) {

        if(data.isEmpty()){
            return false;
        }

        if(data instanceof List){

            List list = (List) data;



            if(ObjectUtil.isNumber(list.get(0)) && ObjectUtil.isNumber(target)){

            }
        }


        return false;
    }
}
