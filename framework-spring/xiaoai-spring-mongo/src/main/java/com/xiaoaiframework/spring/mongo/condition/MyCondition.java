package com.xiaoaiframework.spring.mongo.condition;

import com.xiaoaiframework.spring.mongo.annotation.Condition;
import com.xiaoaiframework.spring.mongo.annotation.Eq;
import com.xiaoaiframework.spring.mongo.annotation.Like;
import com.xiaoaiframework.spring.mongo.constant.ActionType;


@Condition
public class MyCondition {

    @Eq
    String id;

    @Eq(action = ActionType.OR)
    String schoolCode;

    @Like(name = "name")
    String likeName;


    

}
