package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.core.type.JavaType;
import com.xiaoaiframework.core.type.MapType;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.coll.CollUtil;

import java.util.Map;

/**
 * 实体类转MAP
 * @author edison
 */
public class MapConvert implements TypeConvert<Map>{



    @Override
    public Map convert(Object data, JavaType type) {

        if(CollUtil.isColl(data) || ObjectUtil.isArray(data) || ObjectUtil.isPrimitiveWrapper(data) || ObjectUtil.isPrimitive(data)){
            throw new ClassCastException(data.getClass().getName()+"转换为"+type.getTypeName()+"失败");
        }

        return BeanUtil.beanToMap(data);
    }

    @Override
    public boolean match(JavaType type) {

        if(type instanceof MapType){
            return true;
        }

        return false;
    }
}
