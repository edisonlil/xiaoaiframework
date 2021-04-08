package com.xiaoaiframework.util.net;

import com.alibaba.fastjson.util.IOUtils;
import com.xiaoaiframework.util.file.FileUtil;
import com.xiaoaiframework.util.io.IOUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author edison
 */
public class DownloadUtil {

    public static InputStream download(String url){

        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String downloadText(String url){

        StringBuilder builder = new StringBuilder();

        try(InputStreamReader input = new InputStreamReader(download(url),"utf-8")){

            int content = 0;
            while((content = input.read())!=-1){
                builder.append((char) content);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();


    }

}
