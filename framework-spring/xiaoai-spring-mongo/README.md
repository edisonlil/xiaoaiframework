# xiaoai-spring-mongodb

作者：edison

邮箱：edisonlil@163.com



此项目作为`spring-boot-starter-data-mongodb`的二次封装，采用基于注解的形式提供基本的增，删，改，查。

##### 查询使用案例

1. 简单查询

```java
@Mapping(entityType = Entity.class)
public interface Example {

    /**
     * 根据ID查询实体
     */
    @Select
    Entity findById(@Eq(name = "id") String id);

    /**
     * 根据name进行模糊查询
     */
    @Select
    List<Entity> findByName(@Like(name = "name") String name);

    /**
     * 根据account和pwd进行查询
     */
    @Select
    Entity findByAccountAndPwd(@Eq(name = "account") String account,@Eq(name="pwd") String pwd);
    

}

```

2. 复杂查询


```java
class Condition {

    @Eq
    String id;
    
    @In
    String name;
    
    @Lt
    Integer age;

}

@Mapping(entityType = Entity.class)
public interface Example {
    
    /**
       根据条件查询
     */
    @Select
    Entity findByCondition(@Condition Condition condition);
}
```
