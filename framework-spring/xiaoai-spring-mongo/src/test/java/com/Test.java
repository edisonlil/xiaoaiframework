package com;

import com.xiaoaiframework.spring.mongo.annotation.Eq;
import com.xiaoaiframework.spring.mongo.annotation.Mapping;

public class Test {

    public void find(@Eq(name = "name") String name,@Eq(name = "id") Integer id){
    }

    public static void main(String[] args) throws NoSuchMethodException {


    }
}
