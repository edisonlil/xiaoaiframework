package com.xiaoaiframework.core.type;

import java.util.Collection;

/**
 * @author edison
 */
public class CollType extends JavaType<Collection> {


    /**
     * 数组元素类型
     * 默认为Class
     */
    Class elementType = Object.class;


    public CollType(Class<Collection> type){super(type);}

    public CollType(Class<Collection> type,Class elementType){
        super(type);
        this.elementType = elementType;
    }



    public Class getElementType() {
        return elementType;
    }
}
