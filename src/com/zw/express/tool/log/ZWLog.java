package com.zw.express.tool.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.urgoo.common.ZWConfig;
import com.urgoo.updatesoft.ParseXmlService;
import com.zw.express.tool.net.HttpDownloader;

import android.R.integer;
import android.util.Log;

public class ZWLog {
	private static HashMap<String, String> mHashMap;
	private static Boolean MYLOG_SWITCH=true; // 日志文件总开关  
	private static Boolean MYLOG_WRITE_TO_FILE=true;// 日志写入文件开关  
    private static char MYLOG_TYPE='v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息  
    private static String MYLOG_PATH_SDCARD_DIR="/sdcard/ITAPLog/";// 日志文件在sdcard中的路径  
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 3;// sd卡中日志文件的最多保存天数  
    private static String MYLOGFILEName = "ITAPZWLog.txt";// 本类输出的日志文件名称  
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(  
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式  
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
	
	private static Thread mThread = null;

	public static void w(String tag, Object msg) { // 警告信息  
		log(tag, msg.toString(), 'w');  
	}  
  
    public static void e(String tag, Object msg) { // 错误信息  
        log(tag, msg.toString(), 'e');  
    }  
  
    public static void d(String tag, Object msg) {// 调试信息  
        log(tag, msg.toString(), 'd');  
    }  
  
    public static void i(String tag, Object msg) {//  
        log(tag, msg.toString(), 'i');  
    }  
  
    public static void v(String tag, Object msg) {  
        log(tag, msg.toString(), 'v');  
    }  
  
    public static void w(String tag, String text) {  
        log(tag, text, 'w');  
    }  
  
    public static void e(String tag, String text) {  
        log(tag, text, 'e');  
    }  
  
    public static void d(String tag, String text) {  
        log(tag, text, 'd');  
    }  
  
    public static void i(String tag, String text) {  
        log(tag, text, 'i');  
    }  
  
    public static void v(String tag, String text) {  
        log(tag, text, 'v');  
    }  
  
    /** 
     * 根据tag, msg和等级，输出日志 
     *  
     * @param tag 
     * @param msg 
     * @param level 
     * @return void 
     * @since v 1.0 
     */  
    private static void log(final String tag, final String msg, final char level) {  
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				
				HttpDownloader httpDownloader = new HttpDownloader();
				InputStream is = httpDownloader
						.download2(ZWConfig.versionURL);
				try {
					if (is!=null) {
						// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
						ParseXmlService service = new ParseXmlService();
						mHashMap = service.parseXml(is);
						ZWLog.MYLOG_SWITCH = Boolean.valueOf(mHashMap.get("mylogSwitch"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (MYLOG_SWITCH) {  
					if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息  
						Log.e(tag, msg);  
					} else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {  
						Log.w(tag, msg);  
					} else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {  
						Log.d(tag, msg);  
					} else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {  
						Log.i(tag, msg);  
					} else {  
						Log.v(tag, msg);  
					}  
					if (MYLOG_WRITE_TO_FILE)  
						writeLogtoFile(String.valueOf(level), tag, msg);  
				}  
			}
		}).start();
    }  
	
	public static void KeyLogToSDCard(String msg){
		
	}
	
	public static void InitThread(){
		if(mThread == null){
			mThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
				}
			});
			mThread.start();
		}
	}
	
	/** 
     * 打开日志文件并写入日志 
     *  
     * @return 
     * **/  
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件  
        Date nowtime = new Date();  
        String needWriteFiel = logfile.format(nowtime);  
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype  
                + "    " + tag + "    " + text;  
        File destDir = new File(MYLOG_PATH_SDCARD_DIR);
        if (!destDir.exists()) {
         destDir.mkdirs();
        }
        File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel  
                + MYLOGFILEName);  
        try {  
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖  
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);  
            bufWriter.write(needWriteMessage);  
            bufWriter.newLine();  
            bufWriter.close();  
            filerWriter.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 删除制定的日志文件 
     * */  
    public static void delFile() {// 删除日志文件  
    	List<String> dateList = getDateBefore();
    	File file = new File(MYLOG_PATH_SDCARD_DIR);
        File[] root = file.listFiles();
        if (null!=file && null!=root && root.length>0){
        Boolean bl = true;
        for (int i = 0; i < root.length; i++) {
			File f = root[i];
			for (String str : dateList) {
				if(f.getName().equals(str)){
					bl = false;
				}
			}
			if(bl)
				f.delete();
			bl = true;
		}
    	}
    }  
  
    /** 
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名 
     * */  
    private static List<String> getDateBefore() {  
        Calendar now = Calendar.getInstance();  
        List<String> dateList = new ArrayList<String>();
        int date = now.get(Calendar.DATE);
        for (int i = 0; i < SDCARD_LOG_FILE_SAVE_DAYS; i++) {
        	now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), date-i);
        	String str = logfile.format(now.getTime());
        	dateList.add(str+MYLOGFILEName);
		}
        
        return dateList;  
    }
	
}
