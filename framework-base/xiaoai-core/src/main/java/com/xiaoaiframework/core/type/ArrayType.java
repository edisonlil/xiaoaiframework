package com.xiaoaiframework.core.type;


import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author edison
 */
public class ArrayType extends JavaType{


    /**
     * 数组元素类型
     * 默认为Class
     */
    Class elementType = Object.class;


    public ArrayType(Class<Array> type) {
        super(type);
    }

    public ArrayType(Class<Collection> type, Class elementType){
        super(type);
        this.elementType = elementType;
    }


    public void setElementType(Class elementType) {
        this.elementType = elementType;
    }

    public Class getElementType() {
        return elementType;
    }



}
