/**
 * 
 */
package com.urgoo.business;

import android.content.ContentValues;
import android.database.Cursor;

import com.urgoo.data.BaseDBHelper;


/**
 * @ClassName: DBHelperService 
 * @Description: 数据库操作服务类 
 * @author 杨德成
 * @date 2016年2月1日 下午2:46:30 
 *  
 */
public class DBHelperService {
	private String TAG = "SystemManager";

	//private String TABLE_SYSTEM = "systems";
	
	/*private String SYSTEM_ID = "id";
	private String SYSTEM_KEY = "keycode";
	private String SYSTEM_CODE = "code";
	private String SYSTEM_STATUS = "status";
	private String SYSTEM_DESC = "description";*/
	
	//信息主表
	public String TABLE_INFO = "info";
	private String ID = "ID";
	private String SFZHM = "SFZHM";
	private String DZ = "DZ";
	private String DSWS = "DSWS";
	private String BXWS = "BXWS";
	private String KTWS = "KTWS";
	private String RSQWS = "RSQWS";
	private String XYJWS = "XYJWS";
	
	private String DFBWS = "DFBWS";
	private String DCLWS = "DCLWS";
	private String CFJWS = "CFJWS";
	private String YSJWS = "YSJWS";
	private String WBLWS = "WBLWS";
	
	private String DL = "DL";
	private String ZCRQ = "ZCRQ";
	
	//用户身份证信息
	public String TABLE_IC= "idcard";
	
	//用户地址信息
	private String TABLE_Address= "address";

	//杨德成 20160809 urgoo推荐信息表
	public String TABLE_Recommend = "Recommend";
	private String RID = "RID";
	private String RNAME = "RNAME";
	private String RTYPE = "RTYPE";


	
	
	private final static DBHelperService dbService = new DBHelperService();
	public static DBHelperService getInstance(){
		return dbService;
	}




	/**
	 *
	 * @Title: getCreateIdcardTableSQL
	 * @Description: 创建身份证表信息
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public String getCreateRecommendTableSQL(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(TABLE_IC).append("(");
		sb.append("RID").append(" integer primary key autoincrement, ");
		sb.append("RNAME").append(" varchar(64), ");
		sb.append("RTYPE").append(" varchar(64) ");
		sb.append(");");
		return sb.toString();
	}
	/**
	 * 
	* @Title: getCreateTableSQL 
	* @Description: 创建表
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getCreateTableSQL(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(TABLE_INFO).append("(");
		sb.append(ID).append(" integer primary key autoincrement, ");
		sb.append(SFZHM).append(" varchar(64), ");
		sb.append(DZ).append(" varchar(64), ");
		sb.append(DSWS).append(" varchar(64), ");
		sb.append(BXWS).append(" varchar(64), ");
		sb.append(KTWS).append(" varchar(64), ");
		sb.append(RSQWS).append(" varchar(64), ");
		
		sb.append(DFBWS).append(" varchar(64), ");
		sb.append(DCLWS).append(" varchar(64), ");
		sb.append(CFJWS).append(" varchar(64), ");
		sb.append(YSJWS).append(" varchar(64), ");
		sb.append(WBLWS).append(" varchar(64), ");
		
		sb.append(DL).append(" varchar(64), ");
		sb.append(ZCRQ).append(" varchar(64), ");
		
		sb.append(XYJWS).append(" varchar(64)");
		sb.append(");");
		return sb.toString();
	}
	
	
	/**
	 * 
	* @Title: getCreateIdcardTableSQL 
	* @Description: 创建身份证表信息
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getCreateIdcardTableSQL(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(TABLE_IC).append("(");
		sb.append("ID").append(" integer primary key autoincrement, ");
		sb.append("IDcard").append(" varchar(64), ");
		sb.append("Name").append(" varchar(64), ");
		sb.append("Sex").append(" varchar(64), ");
		sb.append("Birthdate").append(" varchar(64), ");
		sb.append("Address").append(" varchar(64) ");
		
		sb.append(");");
		return sb.toString();
	}
	
	
	/**
	 * 
	* @Title: getCreateAddressTableSQL 
	* @Description: 创建地址表信息 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getCreateAddressTableSQL(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(TABLE_IC).append("(");
		sb.append("ID").append(" integer primary key autoincrement, ");
		sb.append("SFZHM").append(" varchar(64), ");
		sb.append("DZ").append(" varchar(64) ");
		sb.append(");");
		return sb.toString();
	}
	
	/**
	 * 
	* @Title: createDB 
	* @Description: 创建数据库
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean createDB(){
		return BaseDBHelper.getInstance().isExistDB("");
	}
	
	
	/**
	 * 
	* @Title: insert 
	* @Description: 插入数据 
	* @param @param table
	* @param @param values
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	 public int insert(String table, ContentValues values){
		 return BaseDBHelper.getInstance().insert(table, values);
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
	 public  Cursor  query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit){
		
		 return BaseDBHelper.getInstance().query(table, columns, selection, selectionArgs,  orderBy,limit);
		 
	 }
	 
	 /**
	  * 
	 * @Title: query 
	 * @Description: 去除重复数据
	 * @param @param distinct
	 * @param @param table
	 * @param @param columns
	 * @param @param selection
	 * @param @param selectionArgs
	 * @param @param groupBy
	 * @param @param having
	 * @param @param orderBy
	 * @param @param limit
	 * @param @return    设定文件 
	 * @return Cursor    返回类型 
	 * @throws
	  */
	 public  Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
	    	
	      return BaseDBHelper.getInstance().query(distinct,table,columns, selection,selectionArgs, groupBy,having,orderBy,limit);
	  }
	 
	 
	 /**
	  * 
	 * @Title: delete 
	 * @Description: 数据删除
	 * @param @param table
	 * @param @param whereClause
	 * @param @param whereArgs
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	  */
	 public int delete(String table, String whereClause, String[] whereArgs){
		return BaseDBHelper.getInstance().delete(table, whereClause, whereArgs);
	 }
	 
	 
	 /**
	  * 
	 * @Title: isExistDB 
	 * @Description: TODO判断数据库是否存在
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	  */
	 public boolean isExistDB(){
		 return BaseDBHelper.getInstance().isExistDB("");
	 }
}
