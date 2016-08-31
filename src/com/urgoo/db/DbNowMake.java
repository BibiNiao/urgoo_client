package com.urgoo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by urgoo_01 on 2016/6/22.
 */
public class DbNowMake extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "make.db";
    private static final int DATABASE_VERSION = 1;

    public DbNowMake(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table person" +
                "(type integer,item integer,state integer,num integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
