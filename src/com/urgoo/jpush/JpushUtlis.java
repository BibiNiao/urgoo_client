package com.urgoo.jpush;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * Created by urgoo_01 on 2016/7/2.
 */
public class JpushUtlis {

    //设置别名
    public static void setAlias(Context context, String alias,
                                TagAliasCallback callback) {
        JPushInterface.setAlias(context, alias, callback);
    }


    //设置标签
    public static void setTags(Context context, String tags, TagAliasCallback callback) {
        Set<String> mStrings = new HashSet<String>();
        mStrings.add(tags);
        JPushInterface.setTags(context, mStrings, callback);
    }

    public static class localNotif {
        private JPushLocalNotification ln = new JPushLocalNotification();
        private Context context;
        private String title;
        private String content;
        private long number;
        private long broadCastTime;
        private long notificationId;
        private Map<String, Object> extras;

        public localNotif(Context mContext, String mTitle, String mContent, long mNumber, long mBroadCastTime, long mNotificationId) {
            this.context = mContext;
            this.title = mTitle;
            this.content = mContent;
            this.number = mNumber;
            this.broadCastTime = mBroadCastTime;
            this.notificationId = mNotificationId;
            init();
        }

        public localNotif(Context mContext, String mTitle, String mContent, long mNumber, long mBroadCastTime, long mNotificationId, Map<String, Object> extras) {
            this.context = mContext;
            this.title = mTitle;
            this.content = mContent;
            this.number = mNumber;
            this.broadCastTime = mBroadCastTime;
            this.notificationId = mNotificationId;
            this.extras = extras;
            init();
        }

        public void init() {
            ln.setTitle(title);
            ln.setContent(content);
            ln.setBuilderId(number);
            ln.setBroadcastTime(broadCastTime);
            ln.setNotificationId(notificationId);
            if (extras != null) {
                JSONObject json = new JSONObject(extras);
                ln.setExtras(json.toString());
            }
            create();
        }

        public void create() {
            JPushInterface.addLocalNotification(context, ln);
        }
    }

    //停止接收推送信息
    public static void stopJpush(Context context) {
        JPushInterface.stopPush(context);
    }

    //恢复接收推送信息
    public static void resumePush(Context context) {
        JPushInterface.resumePush(context);
    }

    //判断接收通知是否开启
    public static boolean isPushStopped(Context context) {
        return JPushInterface.isPushStopped(context);
    }


    //本地通知工具帮助类
    public static class localNotificationBuilder {
        private Context context;
        private JPushLocalNotification ln = new JPushLocalNotification();

        public localNotificationBuilder(Context context) {
            this.context = context;
        }

        public void create() {
            JPushInterface.addLocalNotification(context, ln);
        }


        //设置编号
        public localNotificationBuilder setBuilderId(long number) {
            ln.setBuilderId(number);
            return this;
        }

        //设置标题
        public localNotificationBuilder setTitle(String title) {
            ln.setTitle(title);
            return this;
        }

        //设置内容
        public localNotificationBuilder setContent(String content) {
            ln.setContent(content);
            return this;
        }

        //设置额外的数据信息extras为json字符串
        public localNotificationBuilder setExtras(Map<String, Object> extras) {
            JSONObject json = new JSONObject(extras);
            ln.setExtras(json.toString());
            return this;
        }

        //设置本地通知触发时间
        public localNotificationBuilder setBroadcastTime(long broadCastTime) {
            ln.setBroadcastTime(broadCastTime);
            return this;
        }

        //设置本地通知的ID
        public localNotificationBuilder setNotificationId(long notificationId) {
            ln.setNotificationId(notificationId);
            return this;
        }

    }

}
