package com.urgoo.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity 管理类
 * 
 * @author 51offer
 *
 */
public class ScreenManager {
    private static Stack<Activity> activityStack;
    private static ScreenManager sInstance;

    private ScreenManager() {
    }

    public static synchronized ScreenManager getInstance() {
        if (sInstance == null) {
            sInstance = new ScreenManager();
            if (activityStack == null) {
                activityStack = new Stack<>();
            }
        }
        return sInstance;
    }

    public int size() {
        return activityStack.size();
    }

    public void popActivity() {
        if (size() > 0) {
            Activity activity = activityStack.lastElement();
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class<?> cls) {
        if (!activityStack.isEmpty()) {
            int stockLen = activityStack.size() - 1;
            for (int i = stockLen; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (null != activity) {
                    if (cls != null && activity.getClass().equals(cls)) {
                        continue;
                    } else {
                        popActivity(activity);
                    }
                }
            }
        }
    }

    public void popAllActivitys(String... clsStr) {
        int count = 0;
        if (!activityStack.isEmpty()) {
            int stockLen = activityStack.size() - 1;
            for (int i = stockLen; i > 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity == null) {
                    break;
                }
                int len = clsStr.length;
                for (String aClsStr : clsStr) {
                    if (activity.getClass().getSimpleName().equals(aClsStr)) {
                        popActivity(activity);
                        count++;
                    }
                }
                if (count == len) {
                    break;
                }
            }
        }
    }

    public void popAllActivitys() {
        popAllActivityExceptOne(null);
    }
}