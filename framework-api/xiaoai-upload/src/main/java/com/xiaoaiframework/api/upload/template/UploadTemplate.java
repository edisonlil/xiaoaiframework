package com.xiaoaiframework.api.upload.template;

import com.xiaoaiframework.api.upload.UploaderInfo;
import com.xiaoaiframework.api.upload.client.UploadClient;
import com.xiaoaiframework.core.base.ResultBean;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;

import com.xiaoaiframework.util.base.StrUtil;
import com.xiaoaiframework.util.file.FilenameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 文件上传模板
 * @author edison
 */
public abstract class UploadTemplate {

    Logger logger = LoggerFactory.getLogger(UploadTemplate.class);


    public abstract UploadClient client();

    public abstract URI getUri();

    public abstract String getFolder();

    public abstract String getBucketName();

    /**
     * 上传文件
     * @param file
     * @return
     */
    ResultBean<UploaderInfo> upload(File file){

        Long start = System.currentTimeMillis();
        logger.info("--------------------文件上传开始----------------------");
        logger.info("文件名称为:{}",file.getName());
        String uuid = StrUtil.uuid(true);
        //获取文件后缀
        String suffix = FilenameUtil.getExtension(file.getName());

        UploadClient client = null;
        try {
            client = client();
            //assertHasBucket(client, bucketName);

            String folder = getFolder();
            // 设置文件路径和名称
            String fileUrl = folder + "/" + LocalDate.now().toString() + "/" + suffix + "/" + uuid + "-" + file.getName();
            logger.info("上传文件的名称为:{}", file.getName());

            client.upload(getBucketName(),fileUrl,file);
            
            logger.info("上传消耗的时间{}", System.currentTimeMillis() - start);
            return ResultBean.success().setData(new UploaderInfo(
                    file.length(),
                    fileUrl,
                    getUri().toString() + "/" + fileUrl,
                    suffix,
                    getBucketName(),
                    file.getName(),
                    folder
            ));

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return ResultBean.fail();

    }



    /**
     * 获取临时访问文件路径
     * @param fileOssKey
     * @param expireTime
     * @return
     */
    public abstract String getTempUrl(String fileOssKey, Long expireTime);

}
