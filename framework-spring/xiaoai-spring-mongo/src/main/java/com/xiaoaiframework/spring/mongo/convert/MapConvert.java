package com.xiaoaiframework.spring.mongo.convert;

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
    public Map convert(Object data, Class type) {

        return BeanUtil.beanToMap(data);
    }

    @Override
    public boolean canConvert(Object data) {

        if(CollUtil.isColl(data) || ObjectUtil.isArray(data) || ObjectUtil.isPrimitiveWrapper(data) || ObjectUtil.isPrimitive(data)){
            return false;
        }

        return true;

    }
}
