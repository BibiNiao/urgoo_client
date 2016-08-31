package com.urgoo;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.urgoo.client.BaseApplication;
import com.urgoo.message.activities.MainActivity;
import com.zw.express.tool.log.Log;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {
    public static final String TAG = "ScreenManager";

    private static ArrayList<Activity> activities;
    private static ScreenManager instance;

    private ScreenManager() {
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        Log.d("..." + TAG, activities == null ? "0" : activities.size() + "");
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activities.remove(activity);
        }
    }

    public void popOneActivity() {
        if (activities.size() == 0) return;
        if (activities.get(activities.size() - 1) != null) {
            popActivity(activities.get(activities.size() - 1));
        }
    }

    public void popTwoActivity() {
        Log.d("popTwoActivity000+++" + TAG, "" + activities.size());
        if (activities.size() < 2) {
            return;
        }
        if (activities.get(activities.size() - 1) != null&&
                !(activities.get(activities.size() - 1) instanceof MainActivity)) {
            popActivity(activities.get(activities.size() - 1));
        }
        Log.d("popTwoActivity1111+++" + TAG, "" + activities.size());
        if (activities.get(activities.size() - 1) != null &&
               !(activities.get(activities.size() - 1) instanceof MainActivity)) {
            popActivity(activities.get(activities.size() - 1));
        }
        Log.d("popTwoActivity222+++" + TAG, "" + activities.size());
    }

    public void popThreeActivity() {
        Log.d("popTwoActivity1+++" + TAG, "" + activities.size());
        if (activities.size() < 3) {
            return;
        }
        if (activities.get(activities.size() - 1) != null) {
            popActivity(activities.get(activities.size() - 1));
        }
        if (activities.get(activities.size() - 1) != null) {
            popActivity(activities.get(activities.size() - 1));
        }
        if (activities.get(activities.size() - 1) != null) {
            popActivity(activities.get(activities.size() - 1));
        }
        Log.d("popTwoActivity222+++" + TAG, "" + activities.size());
    }

    public void popActivity(Class<? extends Activity> cls) {
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (activities.get(i) != null) {
                if (activities.get(i).getClass().equals(cls)) {
                    popActivity(activities.get(i));
                }
            }
        }
    }

    public boolean isActivityFinishedOrInexsit(Class<? extends Activity> cls) {
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (activities.get(i) != null) {
                if (activities.get(i).getClass().equals(cls)) {
                    if (!activities.get(i).isFinishing()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void popAllActivity() {
        try {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i) != null) {
                    popActivity(activities.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Activity currentActivity() {
        if (activities == null) return null;
        if (activities.size() < 1) return null;
        Activity activity = (Activity) activities.get(activities.size() - 1);
        return activity;
    }

    public boolean isMainActivityShowing() {
        BaseApplication myApplication = BaseApplication.getInstance();
        if (myApplication != null) {
            ActivityManager manager = (ActivityManager) myApplication.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
            ComponentName component = cinfo.topActivity;
            if (TextUtils.equals(component.getClassName(), MainActivity.class.getName())) {
                return true;
            }
        }
        return false;
    }

    public void pushActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<Activity>();
        }
        activities.add(activity);
    }

    public void popExcept(Class<? extends Activity> cls) {
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (activities.get(i).getClass().equals(cls)) {
                continue;
            }
            popActivity(activities.get(i));
        }
    }
}
