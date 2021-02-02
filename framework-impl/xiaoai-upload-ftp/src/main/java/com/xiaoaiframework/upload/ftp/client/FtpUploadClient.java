package com.xiaoaiframework.upload.ftp.client;

import cn.hutool.extra.ftp.Ftp;
import com.xiaoaiframework.api.upload.client.UploadClient;
import com.xiaoaiframework.util.file.FilenameUtil;

import java.io.File;
import java.io.IOException;

/**
 * Ftp上传客户端
 * @author edison
 */
public class FtpUploadClient implements UploadClient<Ftp> {

    String ip;

    Integer port;

    String username;

    String password;


    public FtpUploadClient(String ip,Integer port,String username,String password){
        super();
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public Ftp connect() {
        return new Ftp(ip,port,username,password);
    }

    @Override
    public void upload(String root, String path, File file) {
        
        Ftp ftp = connect();
        if(!ftp.pwd().equals(root)){
            ftp.cd(root);
        }
        ftp.upload(path, FilenameUtil.getBaseName(path), file);
        close(ftp);
    }

    @Override
    public void close(Ftp connect) {
        try {
            if(connect != null){
                connect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connect != null){
                try {
                    connect.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getTempUrl(String key, Long expireTime) {
        return null;
    }

}
