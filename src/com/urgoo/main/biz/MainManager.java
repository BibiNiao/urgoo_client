package com.urgoo.main.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/7/7.
 */
public class MainManager {
    private static MainManager sInstance;
    private Context mContext;

    private MainManager(Context context) {
        this.mContext = context;
    }


    public static MainManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new MainManager(context);
        }
        return sInstance;
    }

    /**
     * 首页顾问类型图标选择（圆圈图标）
     *
     * @param callback
     */
    public void selectCounselorBannerList(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSelectCounselor, ZWConfig.URL_requestSelectCounselorBannerList, params, callback);
    }


    /**
     * 首页直播LIVE列表
     *
     * @param callback
     */
    public void selectZoomLiveList(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSelectZoomLive, ZWConfig.URL_requestSelectZoomLiveList, params, callback);
    }

    /**
     * 首页顾问推荐列表
     *
     * @param callback
     */
    public void getMyCounselorListTop(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetMyCounselorListTop, ZWConfig.URL_requestGetMyCounselorListTop, params, callback);
    }
}
