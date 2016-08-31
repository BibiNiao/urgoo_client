package com.urgoo.data;

import java.util.ArrayList;
import java.util.List;

import com.urgoo.domain.Systems;
import com.urgoo.domain.User;
import com.zw.express.tool.log.ZWLog;

import android.content.ContentValues;
import android.database.Cursor;

public class SystemsManager {
	private final static SystemsManager systemManager = new SystemsManager();
	public static SystemsManager getInstance(){
		return systemManager;
	}

	private String TAG = "SystemManager";

	private String TABLE_SYSTEM = "systems";
	
	private String SYSTEM_ID = "id";
	private String SYSTEM_KEY = "keycode";
	private String SYSTEM_CODE = "code";
	private String SYSTEM_STATUS = "status";
	private String SYSTEM_DESC = "description";
	
	private User user = null;
	
	public User getUser(){
		if(user == null){
			user = (User) getByCodeAndStatus(User.class.getSimpleName());
		}
		if(user == null){
			user = new User();
			user.userId = user.autoUserId();
			saveOrUpdate(user);
		}
		return user;
	}
	
	public String getCreateTableSQL(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(TABLE_SYSTEM).append("(");
		sb.append(SYSTEM_ID).append(" integer primary key autoincrement, ");
		sb.append(SYSTEM_KEY).append(" long, ");
		sb.append(SYSTEM_CODE).append(" varchar(64), ");
		sb.append(SYSTEM_STATUS).append(" integer, ");
		sb.append(SYSTEM_DESC).append(" text ");
		sb.append(");");
		return sb.toString();
	}
	
	public void saveOrUpdate(final Systems systems){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ContentValues values = new ContentValues();
				values.put(SYSTEM_KEY, systems.keycode);
				values.put(SYSTEM_CODE, systems.code);
				values.put(SYSTEM_STATUS, systems.status);
				values.put(SYSTEM_DESC, systems.description);
				if(systems.id > 0){
					values.put(SYSTEM_ID, systems.id);
					String whereClause = SYSTEM_ID + "=" + systems.id;
					int res = BaseDBHelper.getInstance().update(TABLE_SYSTEM, values, whereClause);
					ZWLog.d(TAG, "update res = " + res);
				}else{
					int res = BaseDBHelper.getInstance().insert(TABLE_SYSTEM, values);
					ZWLog.d(TAG, "save res = " + res);
				}
			}
		}).start();
	}
	
	public void saveOrUpdate(Object o){
		if(o != null){
			String code = o.getClass().getSimpleName();
			if(code.equals(User.class.getSimpleName())){
				user = (User) o;
			}
			Systems systems = getByCodeAndStatus(code, Systems.STATUS_NORMAL);
			if(systems == null){
				systems = new Systems();
			}
			systems.code = code;
			systems.description = o.toString();
			saveOrUpdate(systems);
		}
	}
	
	public Object getByCodeAndStatus(String code){
		Object o = null;
		Systems systems = getByCodeAndStatus(code, Systems.STATUS_NORMAL);
		if(systems != null){
			String desc = systems.description;
			if(desc != null && desc.length() > 0){
				if(code.equals(User.class.getSimpleName())){
					o = new User(desc);
				}
			}
		}
		return o;
	}
	
	public List<Systems> getAll(){
		List<Systems> systemsList = null;
		Cursor cursor = null;
		try{
			cursor = BaseDBHelper.getInstance().query(TABLE_SYSTEM, null, null, null, null, null);
			if(cursor != null){
				systemsList = new ArrayList<Systems>();
				while(cursor.moveToNext()){
					systemsList.add(getFromCursor(cursor));
				}
			}
		}catch(Exception e){
			ZWLog.e(TAG, "search error : " + e);
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return systemsList;
	}
	
	private Systems getByCodeAndStatus(String code, int status){
		Systems systems = null;
		Cursor cursor = null;
		try{
			String selection = SYSTEM_CODE + " =? and " + SYSTEM_STATUS + " =? ";
			cursor = BaseDBHelper.getInstance().query(TABLE_SYSTEM, null, selection, new String[]{code, status+""}, null, null);
			if(cursor != null){
				while(cursor.moveToNext()){
					systems = getFromCursor(cursor);
					break;
				}
			}
		}catch(Exception e){
			ZWLog.e(TAG, "search error : " + e);
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return systems;
	}
	
	private Systems getFromCursor(Cursor cursor){
		Systems systems = new Systems();
		systems.id = cursor.getInt(cursor.getColumnIndex(SYSTEM_ID));
		systems.keycode = cursor.getInt(cursor.getColumnIndex(SYSTEM_KEY));
		systems.status = cursor.getInt(cursor.getColumnIndex(SYSTEM_STATUS));
		systems.code = cursor.getString(cursor.getColumnIndex(SYSTEM_CODE));
		systems.description = cursor.getString(cursor.getColumnIndex(SYSTEM_DESC));
		return systems;
	}
	
}
