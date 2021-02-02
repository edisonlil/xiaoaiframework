package com.xiaoaiframework.spring.upload.configuration;

import cn.hutool.core.io.file.FileAppender;
import com.xiaoaiframework.api.upload.client.UploadClient;
import com.xiaoaiframework.api.upload.template.UploadTemplate;
import com.xiaoaiframework.spring.upload.configuration.properties.FileUploadProperties;
import com.xiaoaiframework.upload.ftp.client.FtpUploadClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


/**
 * @author edison
 */
public class FileUploadConfiguration {


    @Bean
    @ConditionalOnMissingBean(UploadClient.class)
    public UploadClient client(FileUploadProperties properties){

        return new FtpUploadClient(properties.getIp(),properties.getPort(),properties.getUsername(),properties.getPassword());

    }

    @Bean
    @ConditionalOnBean(UploadClient.class)
    public UploadTemplate template(FileUploadProperties properties,UploadClient client){
        UploadTemplate uploadTemplate = new UploadTemplate();
        uploadTemplate.setBucketName(properties.getBucketName());
        uploadTemplate.setFolder(properties.getFolder());
        uploadTemplate.setUri(properties.getUri());
        uploadTemplate.setClient(client);
        return uploadTemplate;

    }


}
