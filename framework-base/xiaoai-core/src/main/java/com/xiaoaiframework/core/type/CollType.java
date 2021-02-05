package com.xiaoaiframework.core.type;


import java.util.Collection;

/**
 * @author edison
 */
public class CollType extends GenericJavaType {

    public CollType(Class<Collection> type) {
        super(type);
    }

    public CollType(Class<Collection> type,Class elementType){
        super(type,elementType);
    }

}
