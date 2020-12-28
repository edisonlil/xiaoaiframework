package com.xiaoaiframework.spring.mongo.base;

import com.xiaoaiframework.spring.mongo.annotation.*;

public interface MongoMapping<E,ID> {

    @Select
    E findById(@Eq(name = "id") ID id);

    @Delete
    boolean delete(@Eq(name = "id") ID id);

    @Save
    boolean save(@Eq(name = "id") ID id);


    @Update
    boolean delete(@Eq(name = "id") ID id,@Set E e);

}
