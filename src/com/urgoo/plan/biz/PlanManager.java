package com.urgoo.plan.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/8/9.
 */
public class PlanManager {
    private static PlanManager sInstance;
    private Context mContext;

    private PlanManager(Context context) {
        this.mContext = context;
    }


    public static PlanManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new PlanManager(context);
        }
        return sInstance;
    }

    /**
     * 任务列表
     *
     * @param callback
     */
    public void getStudentTaskListNewest(StringRequestCallBack callback, String termType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("termType", termType);
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetStudentTaskListNewest, ZWConfig.URL_getStudentTaskListNewest, params, callback);
    }

    /**
     * 规划列表
     *
     * @param callback
     */
    public void getPlan(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetTaskList, ZWConfig.URL_requestGetTaskList, params, callback);
    }

}
