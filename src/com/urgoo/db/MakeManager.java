package com.urgoo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * Created by urgoo_01 on 2016/6/22.
 */
public class MakeManager {
    private DbNowMake helper;
    private SQLiteDatabase db;

    public MakeManager(Context context) {
        helper = new DbNowMake(context);
        if (db == null)
            db = helper.getWritableDatabase();

        Log.d("shujuku", "  创建成功 ");
    }

    public void add(List<MakePerson> persons) {
        db.beginTransaction();  //开始事务
        try {
            for (MakePerson person : persons) {
                db.execSQL("INSERT INTO person VALUES(?, ?, ?, ?)", new Object[]{person.type, person.item, person.state, person.num});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }

        Log.d("makes", "add:  " + persons.toString());
    }

    public void add(int type, int item, int state) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO person VALUES(?, ?, ?,0)", new Object[]{type, item, state});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
        Log.d("makes", "type  " + type + "  item  " + item + "  state  " + state + "  0");
    }


    public void add() {
        db.execSQL("INSERT INTO person VALUES(0, 0, 0, 0)");
    }

    // 更新数据，需传入type，item值，确定到要修改的信息列
    public void update(int type, int item, int state) {
        ContentValues cv = new ContentValues();
        cv.put("state", state);
        db.update("person", cv, " type = ? and item = ? ", new String[]{String.valueOf(type), String.valueOf(item)});
    }

    // 重载方法 更新的是num的数据,传入的是T，变为选中，为0，传入的是F，变为没选中，为1
    public void update(int type, int item, int num, boolean Fig) {
        ContentValues cv = new ContentValues();
        cv.put("num", num);
        db.update("person", cv, " type = ? and item = ? ", new String[]{String.valueOf(type), String.valueOf(item)});
    }


    public int query(int type, int item) {
        Cursor cursor = db.query("person", null, "type = ? and item = ? ", new String[]{String.valueOf(type), String.valueOf(item)}, null, null, null);
        if (cursor.moveToFirst()) {
            int st = cursor.getInt(cursor.getColumnIndex("state"));
            return st;
        }
        return 999999999;
    }

    public int query(int type, int item, boolean Fig) {
        Cursor cursor = db.query("person", null, "type = ? and item = ? ", new String[]{String.valueOf(type), String.valueOf(item)}, null, null, null);
        if (cursor.moveToFirst()) {
            int st = cursor.getInt(cursor.getColumnIndex("num"));
            return st;
        }
        return 999999999;
    }


    public void closeDB() {
        db.close();

    }

    public void close() {
        db.execSQL("delete from person");
        Log.d("shujuku", "  删除表数据成功 ");
    }
}
