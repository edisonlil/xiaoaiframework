package com.xiaoaiframework.api.upload;

/**
 * 文件上传后的信息
 */
public class UploaderInfo {

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件的绝对路径
     */
    private String fileApUrl;

    /**
     * 文件的web访问地址
     */
    private String webUrl;

    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 存储的bucket
     */
    private String fileBucket;

    /**
     * 原文件名
     */
    private String oldFileName;
    /**
     * 存储的文件夹
     */
    private String folder;

    public UploaderInfo() {
    }

    public UploaderInfo(Long fileSize, String fileAPUrl, String webUrl, String fileSuffix, String fileBucket, String oldFileName, String folder) {
        this.fileSize = fileSize;
        this.fileApUrl = fileAPUrl;
        this.webUrl = webUrl;
        this.fileSuffix = fileSuffix;
        this.fileBucket = fileBucket;
        this.oldFileName = oldFileName;
        this.folder = folder;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileApUrl() {
        return fileApUrl;
    }

    public void setFileApUrl(String fileApUrl) {
        this.fileApUrl = fileApUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileBucket() {
        return fileBucket;
    }

    public void setFileBucket(String fileBucket) {
        this.fileBucket = fileBucket;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

}
