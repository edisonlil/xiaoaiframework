package com.xiaoaiframework.util.file;

import java.io.*;

import static com.xiaoaiframework.util.io.IoUtils.readAndWrite;


public class FileUtil {

    public static File newPath(String path){

        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }


    public static File newFile(File filePath,String fileName){
        return new File(filePath,fileName);
    }

    public static File newFile(String filePath,String fileName){
        return new File(filePath,fileName);
    }

    public static void transferFile(InputStream input,String path, String fileName){
        transferFile(input, FileUtil.newFile(path,fileName));
    }

    public static void transferFile(InputStream input, File file){

        try(FileOutputStream out = new FileOutputStream(file)){
            readAndWrite(input,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
