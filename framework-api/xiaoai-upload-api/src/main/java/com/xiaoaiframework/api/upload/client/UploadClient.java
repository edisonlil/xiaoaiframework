package com.xiaoaiframework.api.upload.client;

import java.io.File;

/**
 * 文件上传客户端
 * @author edison
 * @param <T>
 */
public interface UploadClient<T> {

    T connect();

    boolean upload(String root, String path, File file);

    void close(T connect);


    /**
     * 获取临时访问文件路径
     * @param key
     * @param expireTime
     * @return
     */
    String getTempUrl(String key, Long expireTime);

}
