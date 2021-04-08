package com.xiaoaiframework.util.file;

import com.alibaba.fastjson.util.IOUtils;
import com.xiaoaiframework.util.base.StrUtil;
import com.xiaoaiframework.util.io.IOUtil;
import com.xiaoaiframework.util.net.DownloadUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件下载工具类
 * @author edison
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

        fileName = fileName == null ? getFileName(url) : fileName;
        File file = FileUtil.newFile(this.path, fileName);
        InputStream input = DownloadUtil.download(url);
        if(input == null){
            throw new NullPointerException("input is null");
        }
        FileUtil.transferFile(input,file);
        IOUtil.close(input);
        return file;
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
