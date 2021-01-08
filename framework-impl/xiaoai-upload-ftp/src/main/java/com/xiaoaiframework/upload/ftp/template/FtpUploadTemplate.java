package com.xiaoaiframework.upload.ftp.template;

import com.xiaoaiframework.api.upload.client.UploadClient;
import com.xiaoaiframework.api.upload.template.UploadTemplate;

import java.net.URI;

public class FtpUploadTemplate extends UploadTemplate {

    @Override
    public UploadClient client() {
       return null;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public String getFolder() {
        return null;
    }

    @Override
    public String getBucketName() {
        return null;
    }

    @Override
    public String getTempUrl(String fileOssKey, Long expireTime) {
        return null;
    }
}
