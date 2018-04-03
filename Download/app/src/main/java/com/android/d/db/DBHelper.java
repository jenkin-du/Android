package com.android.d.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{


    private  static final String DB_NAME="download.db";
    private static final int VERSION=1;

    private static  final String SQL_CREATE="create table downloadInfo (_id integer primary key autoincrement," +
            "threadId integer,url text,start integer,end integer, progress integer)";

    private static  final String SQL_DROP="drop table if exists threadInfo";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 创建数据库
     * @param db  数据库
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
