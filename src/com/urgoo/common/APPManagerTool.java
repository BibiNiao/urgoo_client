package com.urgoo.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.growingio.android.sdk.collection.GrowingIO;

import java.util.List;

/**
 * Created by admin on 2016/8/2.
 */
public class APPManagerTool {

    //杨德成  20160801 判断Activity是否在运行
    public static boolean isActivityRunning(Context mContext, String activityClassName){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if(info != null && info.size() > 0){

            ComponentName component = info.get(0).topActivity;
            if(activityClassName.equals(component.getClassName())){
                return true;
            }
        }
        return false;
    }


    //杨德成 20160804 growingio用户信息基础
    public static void setGrowingIOCS(String user_id,String user_name,String user_phone) {

        GrowingIO growingIO = GrowingIO.getInstance();
        growingIO.setCS1("user_id", user_id);
        growingIO.setCS2("user_name", user_name);
        growingIO.setCS3("user_phone", user_phone);

    }


}
