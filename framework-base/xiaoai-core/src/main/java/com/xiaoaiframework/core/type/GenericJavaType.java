package com.xiaoaiframework.core.type;

import java.util.Collection;

/**
 * 泛型Java类型
 * @author edison
 */
public abstract class GenericJavaType extends JavaType {

    Class elementType = Object.class;


    public GenericJavaType(Class<Collection> type){super(type);}

    public GenericJavaType(Class<Collection> type,Class elementType){
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
