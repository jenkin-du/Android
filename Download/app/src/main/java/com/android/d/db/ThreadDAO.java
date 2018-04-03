package com.android.d.db;

import com.android.d.entry.DownloadInfo;

import java.util.List;

/**数据访问接口
 * Created by D on 2016/4/27.
 */
public  interface ThreadDAO {
    /**
     * 插入下载信息
     */
    void insertDownloadInfo(DownloadInfo Info);

    /**
     * 删除下载信息
     */
    void deleteDownloadInfo(String url,int id);

    /**
     * 更新下载信息
     */
    void updateDownloadInfo(String url,int id,int progress);

    /***
     * 查询下载信息
     */
    List<DownloadInfo> getdownloadInfo(String url);

    /**
     * 判断下载信息是否存在
     */
    boolean isExist(String url ,int id);
}
