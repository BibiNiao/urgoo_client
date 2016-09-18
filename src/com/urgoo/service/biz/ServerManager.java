package com.urgoo.service.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/7/19.
 */
public class ServerManager {
    private static ServerManager sInstance;
    private Context mContext;

    private ServerManager(Context context) {
        this.mContext = context;
    }


    public static ServerManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new ServerManager(context);
        }
        return sInstance;
    }

    /**
     * 规划列表
     *
     * @param callback
     */
    public void getPlan(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.newTaskList, ZWConfig.URL_newTaskList, params, callback);
    }

    /**
     * 规划列表详情
     *
     * @param callback
     */
    public void getTaskContent(StringRequestCallBack callback,String taskId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("taskId", taskId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.newTaskDetail, ZWConfig.URL_newTaskDetail, params, callback);
    }

    /**
     * 规划详情
     *
     * @param callback
     */
    public void getPlanContent(StringRequestCallBack callback,String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeNewTimeLine, ZWConfig.URL_newTimeLine, params, callback);
    }



}
