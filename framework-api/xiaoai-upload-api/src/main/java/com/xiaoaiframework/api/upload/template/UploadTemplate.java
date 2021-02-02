package com.xiaoaiframework.api.upload.template;


import com.xiaoaiframework.api.upload.UploaderInfo;
import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.util.base.StrUtil;
import com.xiaoaiframework.util.file.FilenameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xiaoaiframework.api.upload.client.UploadClient;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;

/**
 * 文件上传模板
 * @author edison
 */
public class UploadTemplate {

    static final Logger LOGGER = LoggerFactory.getLogger(UploadTemplate.class);

    URI uri;

    String folder;

    String bucketName;

    UploadClient client;

    public UploadTemplate(){
    }

    /**
     * 上传的客户端
     * @return
     */
    public void setClient(UploadClient client) {
        this.client = client;
    }


    public URI getUri() {
        return this.uri;
    }


    public String getFolder() {
        return this.folder;
    }


    public String getBucketName() {
        return this.bucketName;
    }


    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public ResultBean<UploaderInfo> upload(File file){

        Long start = System.currentTimeMillis();
        LOGGER.info("--------------------文件上传开始----------------------");
        LOGGER.info("文件名称为:{}",file.getName());
        String uuid = StrUtil.uuid(true);
        //获取文件后缀
        String suffix = FilenameUtil.getExtension(file.getName());

        UploadClient client = this.client;
        try {

            String folder = getFolder();
            // 设置文件路径和名称
            String fileUrl = folder + "/" + LocalDate.now().toString() + "/" + suffix + "/" + uuid + "$" + file.getName();
            LOGGER.info("上传文件的名称为:{}", file.getName());

            client.upload(getBucketName(),fileUrl,file);

            LOGGER.info("上传消耗的时间:{}", System.currentTimeMillis() - start);
            return ResultBean.success().setData(new UploaderInfo(
                    file.length(),
                    fileUrl,
                    getUri().toString() + fileUrl,
                    suffix,
                    getBucketName(),
                    file.getName(),
                    folder
            ));

        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return ResultBean.fail();

    }



    /**
     * 获取临时访问文件路径
     * @param key
     * @param expireTime
     * @return
     */
    public String getTempUrl(String key, Long expireTime){
        return client.getTempUrl(key, expireTime);
    }

}
