package com.xiaoaiframework.upload.ftp.client;

import cn.hutool.extra.ftp.Ftp;
import com.xiaoaiframework.api.upload.client.UploadClient;
import com.xiaoaiframework.util.file.FilenameUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    Logger logger = LoggerFactory.getLogger(FtpUploadClient.class);

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
    public boolean upload(String root, String path, File file) {
        
        Ftp ftp = connect();
        if(!ftp.pwd().equals(root)){
            ftp.cd(root);
        }

        FTPClient ftpClient = ftp.getClient();
        //设置被动模式
        ftpClient.enterLocalPassiveMode();

        boolean ok =  ftp.upload(FilenameUtil.getPreviousPath(path), FilenameUtil.getName(path), file);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)){

            logger.warn("连接ftp服务器失败！ftp服务器地址:" + ip
                    + ", 端口:" + port + ", 初始化工作目录:" + path);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        close(ftp);

        return ok;
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
