package com.xiaoaiframework.spring.mongo.convert;

import com.xiaoaiframework.core.type.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author edison
 */
@Component
public class ConvertRegistrar {

    Map<Class<? extends JavaType>,TypeConvert> registry = new HashMap<>();
    {
        registry.put(DefaultType.class,new OtherConvert());
        registry.put(CollType.class,new CollConvert());
        registry.put(MapType.class,new MapConvert());
        registry.put(ArrayType.class,new ArrayConvert());

    }

    public void registrar(Class<? extends JavaType> type,TypeConvert convert){
        registry.put(type, convert);
    }


    public TypeConvert getConvert(Class<? extends JavaType> type){
        return registry.get(type);
    }


}
