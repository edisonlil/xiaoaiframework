package com.xiaoaiframework.spring.mongo.convert;
import cn.hutool.core.convert.Convert;
import com.xiaoaiframework.core.type.CollType;
import com.xiaoaiframework.core.type.JavaType;
import com.xiaoaiframework.util.base.ConvertUtil;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.coll.CollUtil;

import java.lang.reflect.Type;
import java.util.Collection;
/**
 * 集合对象转换器
 * @author edison
 */
public class CollConvert implements TypeConvert<Collection> {




    @Override
    public Collection convert(Object data, JavaType type) {

        CollType collType = (CollType) type;

        Collection collection = ReflectUtil.newInstance(collType.getType());

        if(ObjectUtil.isArray(data)){
            convertArray(collection,data,collType.getElementType());
        }else if(CollUtil.isColl(data)){
            convertColl(collection,data,collType.getElementType());
        }else {
            convertBasis(collection,data,collType.getElementType());
        }
        return collection;
    }

    @Override
    public boolean match(JavaType type) {

        if(type instanceof CollType){
            return true;
        }


        return false;
    }

    private void convertBasis(Collection collection,Object data, Class elementType){

        if(elementType.equals(data.getClass())){
            collection.add(data);
            return;
        }

        Object element;
        if(ObjectUtil.isPrimitiveWrapper(data.getClass()) && ObjectUtil.isPrimitiveWrapper(elementType)){
            element = Convert.convert(elementType,data);
        }else if(CollUtil.isMap(elementType)){
            element = BeanUtil.beanToMap(data);
        }else {
            element = ReflectUtil.newInstance(elementType);
            ObjectUtil.copyProperties(data,element);
        }
        if(element != null){
            collection.add(element);
        }
    }

    private void convertArray(Collection collection,Object data, Class elementType){
        Object[] objects = (Object[]) data;
        for (Object value : objects) {
            convertBasis(collection, value, elementType);
        }
    }

    private void convertColl(Collection collection,Object data, Class elementType){
        Collection c = (Collection) data;
        for (Object value : c) {
            convertBasis(collection, value, elementType);
        }
    }
}
