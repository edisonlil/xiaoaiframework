package com.xiaoaiframework.spring.mongo.convert;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author edison
 */
@Component
public class ConvertRegistrar {

    Map<Class,TypeConvert> registry = new HashMap<>();
    {
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
