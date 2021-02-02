package com.xiaoaiframework.spring.mongo.base;

import com.xiaoaiframework.spring.mongo.annotation.*;
import com.xiaoaiframework.spring.mongo.annotation.action.Delete;
import com.xiaoaiframework.spring.mongo.annotation.action.Save;
import com.xiaoaiframework.spring.mongo.annotation.action.Select;
import com.xiaoaiframework.spring.mongo.annotation.action.Update;

import java.util.List;

public interface MongoMapping<E,ID> {


    @Select
    List<E> findAll();

    @Select
    E findById(@Eq(name = "id") ID id);
    
    @Delete
    boolean delete(@Eq(name = "id") ID id);

    @Save
    boolean save(E e);


    @Update
    boolean update(@Eq(name = "id") ID id,@Set E e);

}
