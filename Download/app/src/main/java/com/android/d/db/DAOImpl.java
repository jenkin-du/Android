package com.android.d.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.d.entry.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**实现数据库访问接口
 * Created by D on 2016/4/28.
 */
public class DAOImpl implements ThreadDAO {

    private DBHelper dbHelper=null;//创建数据库帮助类，用于连接数据库


    public DAOImpl(Context context) {
       dbHelper=new DBHelper(context);
    }

    //插入数据
    @Override
    public void insertDownloadInfo(DownloadInfo Info) {
        //获得数据库对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //执行插入语句
        db.execSQL("insert into downloadInfo(threadId,url,start,end,progress) values(?,?,?,?,?)"
                ,new Object[]{Info.getId(),Info.getUrl(),Info.getStart(),Info.getEnd(),Info.getProgress()});
        db.close();

    }

    //删除信息
    @Override
    public void deleteDownloadInfo(String url, int id) {

        //获得数据库对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //执行查询语句
        db.execSQL("delete from downloadInfo where url = ? and threadId = ? "
                ,new Object[]{url,id});
        db.close();
    }

    //更新数据库
    @Override
    public void updateDownloadInfo(String url, int id, int progress) {

        //获得数据库对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //执行更新语句
        db.execSQL("update  downloadInfo set progress= ? where url = ? and threadId = ?"
                ,new Object[]{progress,url,id});
        db.close();
    }

    /**
     * 查询数据库
     */
    @Override
    public List<DownloadInfo> getdownloadInfo(String url) {
        List<DownloadInfo> list=new ArrayList<>();
        //获得数据库对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //执行查询语句
        Cursor cursor=db.rawQuery("select * from downloadInfo where url = ?",new String[]{url});
        //通过查询游标遍历出查询结果
        while (cursor.moveToNext()){
            DownloadInfo info=new DownloadInfo();

            //将查询的结果返回
            info.setId(cursor.getInt(cursor.getColumnIndex("threadId")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            info.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            //将查询结果添加到集合中
            list.add(info);
        }
        cursor.close();
        db.close();

        return list;
    }

    @Override
    public boolean isExist(String url, int id) {
        //获得数据库对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //执行查询语句
        Cursor cursor=db.rawQuery("select * from downloadInfo where url = ? and threadId = ?"
                ,new String[]{url,String .valueOf(id)});
        //判断是否有数据存在
       boolean isExists=cursor.moveToNext();

        cursor.close();
        db.close();
        return isExists;
    }
}
