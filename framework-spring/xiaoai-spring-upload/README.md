### 文件上传依赖

在系统开发中，文件上传作为非常常见的功能，为了统一不同对象存储的接入方式，故对文件上传进行了简单的封装。

#### 1.安装

```gradle
compile "com.xiaoaiframework:xiaoai-spring-upload:$xiaoaiVersion"
```

× 对接腾讯云对象存储引入依赖

```gradle
compile "com.xiaoaiframework:xiaoai-upload-oss:$xiaoaiVersion"
```

× 对接啊里云对象存储引入依赖

```gradle
compile "com.xiaoaiframework:xiaoai-upload-cos:$xiaoaiVersion"
```

× 对接本地搭建FTP文件系统引入依赖

```gradle
compile "com.xiaoaiframework:xiaoai-upload-ftp:$xiaoaiVersion"
```



> Tips：默认则选择FTP模式。

#### 2.application.yml 配置

```yml

file:
  upload:
    ip: 115.159.157.148 #文件存储地址 ftp:对应ftp服务器地址，cos or oss 对应存储空间地址
    port: 21 #服务器端口号,cos or oss 该配置作废
    username: admin #用户名
    password: ****** #密码
    uri: http://0.0.0.0:9080 #上传后供访问的uri
    folder: /longhua/security #基本目录
    bucket-name: /var/vsftp # 空间
```



#### 3.注入上传文件模板类

```java
 @Autowired
 UploadTemplate template;
```



#### 4.调用上传文件接口

```java
template.upload(FileTransferUtil.transferTo(multipartFile));
```

