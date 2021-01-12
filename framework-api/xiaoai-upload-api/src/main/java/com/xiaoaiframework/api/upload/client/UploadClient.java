package com.xiaoaiframework.api.upload.client;

import java.io.File;

/**
 * 文件上传客户端
 * @author edison
 * @param <T>
 */
public interface UploadClient<T> {

    T connect();

    void upload(String root, String path, File file);

    void close(T connect);
}
