package com.xiaoaiframework.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

    public static final Integer BUF_SIZE = 1024;

    /**
     * 从input流读取并写入out
     * @param input
     * @param out
     */
    public static void readAndWrite(InputStream input,OutputStream out){

        byte[] buf = new byte[BUF_SIZE];
        int len = 0;

        try {
            while ((len = input.read(buf)) != -1){
                out.write(buf,0,len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 关闭流
     * @param closeable
     */
    public static void close(Closeable closeable){

        try {
            if(closeable != null){closeable.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(closeable != null){closeable.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
