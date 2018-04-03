package com.android.d.services;

import android.content.Context;
import android.content.Intent;

import com.android.d.db.DAOImpl;
import com.android.d.db.ThreadDAO;
import com.android.d.entry.DownloadInfo;
import com.android.d.entry.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 下载文件类
 */
public class Downloader {

    private Context context=null;
    private FileInfo fileInfo=null;
    private  ThreadDAO dao;
    private int progress=0;
    public boolean isPause=false;



    public Downloader(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;

        dao=new DAOImpl(context);
    }


    public void  download(){
        //从数据库中获得线程信息
        List<DownloadInfo> list=dao.getdownloadInfo(fileInfo.getUrl());

        DownloadInfo info=null;
        //若没有数据则新建一个线程下载信息
        if (list.size()==0){
            info=new DownloadInfo(0,fileInfo.getUrl(),0,fileInfo.getLength(),0);
        }else {
            info=list.get(0);
        }
        //开启一个子线程下载信息
        new Download(info).start();


    }


    /**
     * 定义一个下载类
     */
    class Download extends  Thread{

        private DownloadInfo downloadInfo=null;

        public Download(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //向数据库中插入下载信息
            if (!dao.isExist(downloadInfo.getUrl(),downloadInfo.getId())){

                dao.insertDownloadInfo(downloadInfo);
            }


            HttpURLConnection conn=null;
            RandomAccessFile randomFile=null;
            InputStream is=null;
            try {
                //打开 网络连接
                URL url=new URL(downloadInfo.getUrl());
                conn= (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");

                //设置下载位置
                int start=downloadInfo.getStart()+downloadInfo.getProgress();
                conn.setRequestProperty("Range","bytes="+start+"-"+downloadInfo.getEnd());

                //设置文件写入位置
                File file=new File(DownloadService.DOWNLOAD_PATH,fileInfo.getFileName());
                //用随机文件写入流写入文件
                randomFile=new RandomAccessFile(file,"rwd");
                randomFile.seek(start);

                //开始下载
                if (conn.getResponseCode()==HttpURLConnection.HTTP_PARTIAL){
                    //读取数据
                    is=conn.getInputStream();

                    byte[] buffer=new byte[1024*4];
                    int length=-1;
                    Intent intent=new Intent(DownloadService.ACTION_UPDATE);
                    progress+=downloadInfo.getProgress();

                    long time=System.currentTimeMillis();

                    while ((length = is.read(buffer)) !=-1 ){
                        //写入文件
                        randomFile.write(buffer,0,length);

                        progress+=length;// 更新进度

                        //将下载进度广播给Activity
                        //设置发送广播间隔
                        if (System.currentTimeMillis()-time>500){
                            time=System.currentTimeMillis();
                            intent.putExtra("progress",progress*100/fileInfo.getLength());
                            context.sendBroadcast(intent);//广播发送intent
                        }

                        //下载再停时，保存下载进度
                        if (isPause){
                            dao.updateDownloadInfo(downloadInfo.getUrl(),
                                    downloadInfo.getId(),progress);
                            return;
                        }
                    }
                    //下载完成后，删除下载信息
                    dao.deleteDownloadInfo(downloadInfo.getUrl(),downloadInfo.getId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (randomFile != null) {
                        randomFile.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
