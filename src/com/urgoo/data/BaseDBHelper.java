package com.urgoo.data;

import com.zw.express.tool.log.ZWLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BaseDBHelper {
	private static BaseDBHelper instance = new BaseDBHelper();
	public static BaseDBHelper getInstance(){
		return instance;
	}
	
	private String TAG = "BaseDBHelper";
	
	private String DB_NAME = "xd";
	private int DB_VERSION = 1; 
	private Context context;
	private OpenHelper openHelper;
	
	public void init(Context context) {
		this.context = context;
    }
	
	public void destroy() {
		context = null;
        instance = null;
    }
	
	public SQLiteDatabase getDBHelper() {
        synchronized (this) {
            if (openHelper == null) {
                openConnection(DB_NAME);
            }
            return openHelper.getWritableDatabase();
        }
    }
	
	/**
	 * 建立连接
	 * @param dbName
	 */
    private void openConnection(String dbName) {
        try {
            openHelper = new OpenHelper(context, dbName, null, DB_VERSION);
        } catch (Exception e) {
        }
    }
    
    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
        }
    }
    
    /**
     * 数据库是否存在
     * @param tableName
     * @return
     */
    public boolean isExistDB(String tableName) {
    	ZWLog.i(TAG, "isExistDB");
        return existTable(getDBHelper(), tableName);
    }
    
    /**
     * 数据库是否存在
     * @param db
     * @param tableName
     * @return
     */
    private boolean existTable(SQLiteDatabase db, String tableName) {
        StringBuilder builder = new StringBuilder("select 1 from sqlite_master where type='table' and name='");
        builder.append(tableName).append("';");
        Cursor cur = null;
        try {
            cur = db.rawQuery(builder.toString(), null);
            if (cur.moveToNext()) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }
    
    /**
     * 执行sql语句
     * 
     * @param sql
     */
    public void execSQL(String sql) {
        try {
            getDBHelper().execSQL(sql);
        } catch (Exception e) {
        }
    }

    /**
     * rawQuery
     * @param sql
     * @param selectionArgs
     * @return
     */
    public synchronized Cursor rawQuery(String sql, String[] selectionArgs){
    	return getDBHelper().rawQuery(sql, selectionArgs);
    }
    
    /**
     * 数据库查询
     * 
     * @param table
     * @param columns
     * @param selection
     * @return
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String orderBy) {
    	return query(false, table, columns, selection, null, null, null, orderBy, null);
    }
    
    /**
     * query
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @param limit
     * @return
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
        return query(false, table, columns, selection, selectionArgs, null, null, orderBy, limit);
    }
    
    /**
     * query
     * @param distinct
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public synchronized Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
    	if(columns == null){
    		columns = new String[]{" * "};
    	}
    	return getDBHelper().query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 数据删除
     * 
     * @param tableName
     * @param whereClause
     */
    public int delete(String table) {
        return delete(table, null, null);
    }

    /**
     * 数据删除
     * 
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        return getDBHelper().delete(table, whereClause, whereArgs);
    }

    /**
     * insert
     * @param tableName
     * @param values
     * @return id 如果成功，否则返回-1.
     */
    public int insert(String table, ContentValues values) {
        return (int) getDBHelper().insert(table, "Null", values);
    }
    
    /**
     * update
     * @param table
     * @param values
     * @return
     */
    public int update(String table, ContentValues values) {
        return getDBHelper().update(table, values, null, null);
    }
    
    /**
     * update
     * @param table
     * @param values
     * @param whereClause
     * @return
     */
    public int update(String table, ContentValues values, String whereClause) {
        return getDBHelper().update(table, values, whereClause, null);
    }

    /**
     * update
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getDBHelper().update(table, values, whereClause, whereArgs);
    }
    
}
