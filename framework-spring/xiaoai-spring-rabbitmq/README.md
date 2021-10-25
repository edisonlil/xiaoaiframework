# xiaoai-spring-rabbitmq

基于`spring-boot-starter-amqp`的封装。通过接口+注解的方式进行使用，更符合开发人员的习惯。

### 安装方式

##### gradle方式
```shell script
compile com.xiaoaiframework:xiaoai-spring-rabbitmq:${version}
```

##### maven方式
```xml
<dependency>  
    <groupId>com.xiaoaiframework</groupId>  
    <artifactId>xiaoai-spring-rabbitmq</artifactId>  
    <version>${version}</version>  
</dependency>
```

### 使用方式

生产者

```java

/**
 * @author edison
 */
@RpcClient(value = "rabbitmq.demo")
public interface Sender{
   
    /**
     *  消息发送
     */ 
    @RpcClientMethod
    void channel(String msg);
}
```


消费者
```java

/**
 * @author edison
 */
@RpcServertMethod(value = "rabbitmq.demo")
public class Consumer{
   
    /**
     *  消息发送
     */ 
    @RpcServertMethod
    public void channel(String msg){
        log.info("The message received is" + msg);
    }
}
```