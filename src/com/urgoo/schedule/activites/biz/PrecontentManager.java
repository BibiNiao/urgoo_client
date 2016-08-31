package com.urgoo.schedule.activites.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;

import java.util.HashMap;

/**
 * Created by bb on 2016/7/19.
 */
public class PrecontentManager {
    private static PrecontentManager sInstance;
    private Context mContext;

    private PrecontentManager(Context context) {
        this.mContext = context;
    }


    public static PrecontentManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new PrecontentManager(context);
        }
        return sInstance;
    }


    /**
     * 已确定的详情
     */
    public void getPreContent2(PrecontractMyOrderContent callback, String advanceId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("advanceId", advanceId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EvetCodeAdvanceConfirmeDetailClient2, ZWConfig.URL_advanceConfirmeDetailClient2, params, callback);
    }

    /**
     * dai确定的详情
     */
    public void getPreContent1(PrecontractMyOrderContent callback, String advanceId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("advanceId", advanceId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeAdvanceConfirmeDetailClient1, ZWConfig.URL_advanceDetailClient, params, callback);
    }

    /**
     * dai确定的详情
     */
    public void getPreContent3(PrecontractMyOrderContent callback, String advanceId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("advanceId", advanceId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeAdvanceDetailClosedClient3, ZWConfig.URL_advanceDetailClosedClient2, params, callback);
    }

    /**
     * 接收预约
     */
    public void reAdvanceAccept(PrecontractMyOrderContent callback, String advanceId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("advanceId", advanceId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeReAdvanceAccept, ZWConfig.URL_reAdvanceAccept, params, callback);
    }


    /**
     * 接收预约
     */
    public void getNetDataForupdate(PrecontractMyOrderContent callback, String advanceId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("advanceId", advanceId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeReAdvanceAccept, ZWConfig.URL_reAdvanceAccept, params, callback);
    }
}
