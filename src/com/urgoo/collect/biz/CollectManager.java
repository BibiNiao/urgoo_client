package com.urgoo.collect.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/9/23.
 */
public class CollectManager {
    private static CollectManager sInstance;
    private Context mContext;

    private CollectManager(Context context) {
        this.mContext = context;
    }

    public static CollectManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new CollectManager(context);
        }
        return sInstance;
    }

    /**
     * 我关注的顾问
     *
     * @param callback
     */
    public void followConsultants(StringRequestCallBack callback, int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeFollowConsultants, ZWConfig.URL_requestFollowConsultants, params, callback);
    }

    /**
     * 我收藏的视频
     *
     * @param callback
     * @param page
     */
    public void followVideos(StringRequestCallBack callback, int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeFollowVideos, ZWConfig.URL_requestFollowVideos, params, callback);
    }
}
