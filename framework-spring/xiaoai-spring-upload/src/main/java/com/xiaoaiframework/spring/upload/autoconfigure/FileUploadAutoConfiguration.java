package com.xiaoaiframework.spring.upload.autoconfigure;

import com.xiaoaiframework.spring.upload.configuration.FileUploadConfiguration;
import com.xiaoaiframework.spring.upload.configuration.properties.FileUploadProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author edison
 */
@EnableConfigurationProperties(FileUploadProperties.class)
@Import(FileUploadConfiguration.class)
public class FileUploadAutoConfiguration {
}
