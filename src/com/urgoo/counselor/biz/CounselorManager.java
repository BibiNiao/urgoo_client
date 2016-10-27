package com.urgoo.counselor.biz;

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
public class CounselorManager {
    private static CounselorManager sInstance;
    private Context mContext;

    private CounselorManager(Context context) {
        this.mContext = context;
    }


    public static CounselorManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new CounselorManager(context);
        }
        return sInstance;
    }

    /**
     * 获取顾问列表
     *
     * @param callback
     * @param page
     */
    public void getCounselorList(StringRequestCallBack callback, int page, String name, String countryType, String gender,
                                 String serviceType, String chineseLevelType, String counselorExperanceType, String organizationType, String positionType, String surpriseType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("countryType", countryType);
        params.put("gender", gender);
        params.put("serviceType", serviceType);
        params.put("chineseLevelType", chineseLevelType);
        params.put("counselorExperanceType", counselorExperanceType);
        params.put("organizationType", organizationType);
        params.put("positionType", positionType);
        params.put("surpriseType", surpriseType);
        params.put("page", String.valueOf(page));
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetCounselorList, ZWConfig.URL_requestGetCounselorList, params, callback);
    }


    /**
     * 筛选条件
     *
     * @param callback
     */
    public void getCounselorFilter(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetCounselorFilter, ZWConfig.URL_requestGetCounselorFilter, params, callback);
    }


    /**
     * 热门搜索
     *
     * @param callback
     */
    public void getHotFilter(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetHotFilter, ZWConfig.URL_requestGetHotFilter, params, callback);
    }

    /**
     * 获取所有顾问
     *
     * @param callback
     */
    public void getMyCounselorList(int page, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetMyCounselorList, ZWConfig.URL_requestGetMyCounselorList, params, callback);
    }

    /**
     * 顾问详情 被查看调用
     */
    public void getStatCounselorCount(StringRequestCallBack callback, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeStatCounselorCount, ZWConfig.URL_statCounselorCount, params, callback);
    }

    /**
     * 顾问详情 BANNA
     */
    public void getCounselorDetailSubList(StringRequestCallBack callback, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSelectCounselorDetailSubList, ZWConfig.URL_selectCounselorDetailSubList, params, callback);
    }

    /**
     * 顾问详情 基本信息
     */
    public void getCounselorInfo(StringRequestCallBack callback, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeFindCounselorDetail, ZWConfig.URL_findCounselorDetail, params, callback);
    }

    /**
     * 顾问详情 服务项目
     */
    public void getCounselorServer(StringRequestCallBack callback, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSelectCounselorServiceList, ZWConfig.URL_selectCounselorServiceList, params, callback);
    }

    /**
     * 顾问详情 关注
     * 1 顾问  4：收藏专辑 5：收藏专辑视频 6.收藏直播
     */
    public void getAddFollow(StringRequestCallBack callback, String targetId, String followType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("targetId", targetId);
        params.put("followType", followType);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeAddFollow, ZWConfig.URL_addFollow, params, callback);
    }

    /**
     * 顾问详情 取消关注
     * 1 顾问  4：收藏专辑 5：收藏专辑视频 6.收藏直播
     */
    public void getCancleFollow(StringRequestCallBack callback, String targetId, String followType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("targetId", targetId);
        params.put("followType", followType);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeCancleFollow, ZWConfig.URL_cancleFollow, params, callback);
    }

    /**
     * 学生评价
     *
     * @param callBack
     * @param counselorId
     * @param page
     */
    public void getStuEvaluation(StringRequestCallBack callBack, String counselorId, int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetStuEvaluation, ZWConfig.URL_requestGetStuEvaluation, params, callBack);
    }

    /**
     * 顾问详情 基本信息(非首页)
     */
    public void getCounselorInfoMain(StringRequestCallBack callback, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeMyCounselor, ZWConfig.URL_requestMyCounselorDetail, params, callback);
    }
}
