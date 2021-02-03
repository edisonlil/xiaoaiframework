package com.xiaoaiframework.spring.mongo.type;
import java.util.Map;

/**
 * @author edison
 */
public class MapType extends JavaType<Map> {

    Class _key;

    Class _value;

    public MapType(Class<Map> type) {
        super(type);
    }




}
