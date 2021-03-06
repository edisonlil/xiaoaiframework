package com.xiaoaiframework.spring.upload.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author edison
 */
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    /**
     * 文件服务器地址
     */
    String ip;

    /**
     * 文件服务器端口
     */
    Integer port;

    /**
     * 用户名
     */
    String username;

    /**
     * 密码
     */
    String password;

    /**
     * 文件访问的URI
     */
    URI uri;

    /**
     * 文件路径不包含文件名 例如/a/b/c...
     */
    String folder;

    /**
     * 根目录
     */
    String bucketName;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }


}
