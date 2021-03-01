package com.xiaoaiframework.spring.mongo.service;

import com.xiaoaiframework.spring.mongo.convert.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author edison
 */
public class ConvertService {

    Map<Class, TypeConvert> registry = new HashMap<>();
    {
        registry.put(String.class,new StringConvert());
        registry.put(List.class,new CollConvert());
        registry.put(Collection.class,new CollConvert());
        registry.put(Map.class,new MapConvert());
        registry.put(Array.class,new ArrayConvert());

    }

    public void registrar(Class type,TypeConvert convert){
        registry.put(type, convert);
    }


    public TypeConvert getConvert(Class type){
        return registry.get(type);
    }


}
