package com.xiaoaiframework.spring.web.util;

import com.xiaoaiframework.util.file.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * 文件传输工具
 * @author edison
 */
public class FileTransferUtil {

    /**
     * 文件下载的基础路径
     */
    public static final String PATH = System.getProperty("user.home")+"/tmp/file";


    private static File getPath(String location){

        if(location == null){
            return FileUtil.newPath(PATH);
        }

        if(location.indexOf("/") != 0){
            location = "/"+location;
        }

        return FileUtil.newPath(PATH+location);
    }

    public static File transferTo(MultipartFile multipartFile,String fileName){

        File file = FileUtil.newFile(getPath(LocalDate.now().toString()),fileName);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File transferTo(MultipartFile multipartFile) {
        return transferTo(multipartFile,multipartFile.getOriginalFilename());
    }


}
