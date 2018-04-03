package com.android.d.entry;

import java.io.Serializable;

/**
 * 文件信息
 */
public class FileInfo implements Serializable{

    private int id;
    private String url;
    private  int length;
    private String fileName;
    private int progress;

    public FileInfo() {

    }

    public FileInfo(int id, String url, int length, String fileName, int progress) {
        this.id = id;
        this.url = url;
        this.length = length;
        this.fileName = fileName;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", length=" + length +
                ", fileName='" + fileName + '\'' +
                ", progress=" + progress +
                '}';
    }
}
