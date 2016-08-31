package com.urgoo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class OpenHelper extends SQLiteOpenHelper {

    public OpenHelper(Context context, String dbname, CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //ZWLog.e("xdcreate", ArticleManager.getInstance().getCreateTableSQL());
        //ZWLog.e("xdcreate", SystemsManager.getInstance().getCreateTableSQL());
        //db.execSQL(ArticleManager.getInstance().getCreateTableSQL());
        db.execSQL(SystemsManager.getInstance().getCreateTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
