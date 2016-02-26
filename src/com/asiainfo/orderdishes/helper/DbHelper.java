package com.asiainfo.orderdishes.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author gjr 构造一个数据库帮助类
 */
public class DbHelper {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dishes.db";
    private static final int DATABASE_VERSION = 42;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //每次创建帮助类的时候清空下原来的表，弥补litepal的不足
            db.execSQL("DROP TABLE IF EXISTS dishesorder");// 删除原来的表
            db.execSQL("DROP TABLE IF EXISTS ordergoods");//删除原来的表
        }

        // 更新数据库版本,需求变更，升级原表的操作
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public DbHelper(Context ctx) {
        this.mCtx = ctx;
    }

    public DbHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
//		mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
}
