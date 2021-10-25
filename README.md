# Xiaoai Framework

基于`Spring Boot`封装的基础框架，作为所有项目的基础支撑，封装了大量常用的基础组件，根据需求进行引入。

### 项目环境

**JDK  Version**

```text
java version "11.0.5"
```

**Gradle Version**

```text
Gradle 6.1
```

 **组件依赖版本信息参考 
 
```text
gradle.properties
```
### 框架概述

```text

xiaoaiframework 
  |- framework-api //定义抽象接口规范模块，具体实现参考`framework-impl`模块
  |---- xiaoai-upload-api //文件上传通用组件
  |- framework-base //基础通用模块 （作为该架构体系最基础支撑，其他模块将依赖该模块的子模块，该模块的子模块不依赖其他模块）
  |---- xiaoai-common //第三方工具依赖模块
  |---- xiaoai-core //核心模块，框架中最基础的实体定义
  |---- xiaoai-log4j2 //日志模块
  |---- xiaoai-test //单元测试模块
  |---- xiaoai-util //工具包模块，提供框架中常用的工具（目的尽量不依赖第三方工具依赖包实现，减少依赖）
  |- framework-impl //`framework-api`中的具体实现模块
  |---- xiaoai-upload-cos //腾讯云对象存储文件上传实现 //TODO 待实现
  |---- xiaoai-upload-ftp //FTP文件上传实现
  |---- xiaoai-upload-oss //啊里云对象存储文件上传实现 //TODO 待实现
  |- framework-spring //该模块对Spring组件 or 自己定义的组件进行二次封装或集成定制化。依赖于SpringBoot环境
  |---- xiaoai-spring-activemq //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-cloud-config //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-cloud-zipkin //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-feign //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-kafka //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-log4j2 //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-mongo //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-mybatis-generator //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-mybatis-plus //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-rabbitmq //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-redis //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-seata //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-security //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-test //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-upload //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-web //具体参考各个模块的`README.md` //TODO README.md 文档待补充
  |---- xiaoai-spring-xxl-job //具体参考各个模块的`README.md` //TODO README.md 文档待补充
```
