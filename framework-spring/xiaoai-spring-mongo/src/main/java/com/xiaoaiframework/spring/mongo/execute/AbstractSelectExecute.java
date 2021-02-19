package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.convert.ConvertRegistrar;
import com.xiaoaiframework.spring.mongo.convert.GenericTypeConvert;
import com.xiaoaiframework.spring.mongo.convert.OtherConvert;
import com.xiaoaiframework.spring.mongo.convert.TypeConvert;
import com.xiaoaiframework.util.base.ObjectUtil;

import java.lang.reflect.Method;

public abstract class AbstractSelectExecute implements SelectExecute {


    protected MongoExecute execute;

    private ConvertRegistrar convertRegistrar;

    protected Class entityType;


    @Override
    public Object doSelect(Select select, Method method, Object[] objects) {


        Object data = select(select,method,objects);
        if (ObjectUtil.isEmpty(data)) {
            return data;
        }
        
        Class rawClass = select.rawType() != Void.class ? select.rawType() : entityType;
        data = returnTypeConvert(data,method,rawClass);
        return data;

    }


    Object returnTypeConvert(Object data, Method method, Class rawType){

        TypeConvert convert = convertRegistrar.getConvert(method.getReturnType());

        if(convert != null){

            if(convert instanceof GenericTypeConvert){
                ((GenericTypeConvert)convert).setGenericType(rawType);
            }

            if(convert.canConvert(data)){
                throw new ClassCastException("不支持的类型转换");
            }

            return convert.convert(data, method.getReturnType());
        }
        return new OtherConvert().convert(data,method.getReturnType());
    }


    public abstract Object select(Select select, Method method, Object[] objects);



    public void setExecute(MongoExecute execute) {
        this.execute = execute;
    }

    public void setConvertRegistrar(ConvertRegistrar convertRegistrar) {
        this.convertRegistrar = convertRegistrar;
    }

    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }
}
