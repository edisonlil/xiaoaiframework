package com.xiaoaiframework.core.type;


import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author edison
 */
public class ArrayType extends GenericJavaType{



    public ArrayType(Class type) {
        super(type);
    }

    public ArrayType(Class type,Class elementType){
        super(type,elementType);
    }



}
