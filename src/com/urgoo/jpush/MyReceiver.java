package com.urgoo.jpush;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.urgoo.client.R;
import com.urgoo.common.event.MessageEvent;
import com.urgoo.message.activities.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private String type;
    private String hxCode;
    private Bundle bundle;
    private String EXTRA_EXTRA;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        String EXTRA_TITLE = bundle.getString(JPushInterface.EXTRA_TITLE);
        String EXTRA_MESSAGE = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        // 附加的键值对信息
        EXTRA_EXTRA = bundle.getString(JPushInterface.EXTRA_EXTRA);
        EventBus.getDefault().post(new MessageEvent(EXTRA_EXTRA));

        //接收到消息后跳转到聊天界面


        String EXTRA_CONTENT_TYPE = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        String EXTRA_RICHPUSH_FILE_PATH = bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH);
        String EXTRA_NOTIFICATION_TITLE = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String EXTRA_ALERT = bundle.getString(JPushInterface.EXTRA_ALERT);

        Log.d(TAG, "EXTRA_TITLE: " + EXTRA_TITLE);
        Log.d(TAG, "EXTRA_MESSAGE: " + EXTRA_MESSAGE);
        Log.d(TAG, "EXTRA_EXTRA: " + EXTRA_EXTRA);

        Log.d(TAG, "EXTRA_CONTENT_TYPE: " + EXTRA_CONTENT_TYPE);
        Log.d(TAG, "EXTRA_RICHPUSH_FILE_PATH: " + EXTRA_RICHPUSH_FILE_PATH);
        Log.d(TAG, "EXTRA_NOTIFICATION_TITLE: " + EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, "EXTRA_ALERT: " + EXTRA_ALERT);


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
            if (EXTRA_EXTRA != null) {
                try {
                    JSONObject mObject = new JSONObject(EXTRA_EXTRA.toString());
                    JSONObject mObject2 = new JSONObject(mObject.getString("extra"));
                    int type = Integer.parseInt(mObject2.getString("type"));

                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                    Intent notificationIntent = new Intent();
                    Bundle extras = new Bundle();
                    switch (type) {
                        case 1001:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_APPOINTMENT);
                            break;
                        case 2001:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_LIVE_DETAIL);
                            break;
                        case 3001:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_MESSAGE);
                            break;
                        case 4001:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_COUNSELOR);
                            break;
                        case 5001:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_VIDEO_DETAIL);
                            break;
//                        case 3:
//                            //打开自定义的Activity
//                            Intent notificationIntent = new Intent();
//                            notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
//                            notificationIntent.setClass(context.getApplicationContext(), MainActivity.class);
//                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.getApplicationContext().startActivity(notificationIntent);
//                            break;
                        case 2:
                            extras.putString(SplashActivity.EXTRA_JSON, mObject2.toString());
                            extras.putInt(SplashActivity.EXTRA_TARGET, SplashActivity.TARGET_VIDEO);
                            break;
                    }
                    notificationIntent.putExtras(extras);
                    notificationIntent.setClass(context.getApplicationContext(), SplashActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mBuilder.setContentIntent(PendingIntent.getActivity(context, type, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                            .setTicker(context.getString(R.string.app_name))
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
                    nm.notify(type, mBuilder.build());
                    context.getApplicationContext().startActivity(notificationIntent);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    //杨德成  20160801 判断Activity是否在运行
    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {

            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
