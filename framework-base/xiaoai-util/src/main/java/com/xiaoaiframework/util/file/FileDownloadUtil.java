package com.xiaoaiframework.util.file;

import com.alibaba.fastjson.util.IOUtils;
import com.xiaoaiframework.util.base.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件下载工具类
 */
public class FileDownloadUtil {

    private FileDownloadUtil(){}


    /**
     * 文件下载的基础路径
     */
    public final String DEFAULT_PATH = System.getProperty("user.home")+"/tmp/file";

    private String path = DEFAULT_PATH;


    public static FileDownloadUtil create(){

        return new FileDownloadUtil();
    }

    public FileDownloadUtil path(String path){
        this.path = path;
        return this;
    }

    public FileDownloadUtil location(String location){

        if(location.indexOf("/") != 0){
            location = "/"+location;
        }
        this.path = this.path+location;
        return this;
    }

    public File downloadFile(String url){return downloadFile(url,null);}

    public File downloadFile(String url,String fileName){

        try {

            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            fileName = fileName == null ? getFileName(url) : fileName;
            File file = FileUtil.newFile(this.path, fileName);
            InputStream input = connection.getInputStream();
            FileUtil.transferFile(input,file);
            IOUtils.close(input);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    private String getFileName(String url){
        String suffix = FilenameUtil.getSuffix(url);
        String fileName = StrUtil.uuid(true);
        if(suffix != null){
            fileName = fileName+suffix;
        }
        return fileName;
    }
}
