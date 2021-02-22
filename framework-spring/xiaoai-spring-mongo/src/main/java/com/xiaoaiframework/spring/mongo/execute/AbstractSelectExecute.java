package com.xiaoaiframework.spring.mongo.execute;

import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.service.ConvertService;
import com.xiaoaiframework.spring.mongo.convert.GenericTypeConvert;
import com.xiaoaiframework.spring.mongo.convert.OtherConvert;
import com.xiaoaiframework.spring.mongo.convert.TypeConvert;
import com.xiaoaiframework.util.base.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

public abstract class AbstractSelectExecute implements SelectExecute {


    @Autowired
    protected MongoExecute execute;

    @Autowired
    private ConvertService convertService;


    @Override
    public Object doSelect(Select select, Method method, Object[] objects,Class entityType) {


        Object data = select(select,method,objects,entityType);
        if (ObjectUtil.isEmpty(data)) {
            return data;
        }
        
        Class rawClass = select.rawType() != Void.class ? select.rawType() : entityType;
        data = returnTypeConvert(data,method,rawClass);
        return data;

    }


    Object returnTypeConvert(Object data, Method method, Class rawType){

        TypeConvert convert = convertService.getConvert(method.getReturnType());

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


    public abstract Object select(Select select, Method method, Object[] objects,Class entityType);



    public void setExecute(MongoExecute execute) {
        this.execute = execute;
    }

    public void setConvertService(ConvertService convertService) {
        this.convertService = convertService;
    }

}
