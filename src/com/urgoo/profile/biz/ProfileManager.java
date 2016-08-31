package com.urgoo.profile.biz;

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
public class ProfileManager {
    private static ProfileManager sInstance;
    private Context mContext;

    private ProfileManager(Context context) {
        this.mContext = context;
    }


    public static ProfileManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new ProfileManager(context);
        }
        return sInstance;
    }

    /**
     * 注册ZOOM账号
     *
     * @param email
     * @param password
     * @param callback
     */
    public void signUpZoomUser(String email, String password, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", "BcxlISZCSbeiIlaZyKtfUw");
        params.put("api_secret", "IOj0tAavRAILySyh1oM3WpYh3cUhGfBUgn3e");
        params.put("data_type", "JSON");
        params.put("email", email);
        params.put("type", "1");
        params.put("password", password);
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSignUpZoomUser, ZWConfig.URL_requestCreateZoomUser, params, callback);
    }

    /**
     * ZOOM创建邮箱账号和密码
     *
     * @param callback
     */
    public void getZoomAccount(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeZoomAccount, ZWConfig.URL_requestInsertZoomAccount, params, callback);
    }

    /**
     * ZOOM创建邮箱账号和密码
     *
     * @param hostId
     * @param callback
     */
    public void getZoomAccount(String hostId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("hostId", hostId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeZoomAccount, ZWConfig.URL_requestInsertZoomAccount, params, callback);
    }


    /**
     * 我关注的顾问
     *
     * @param callback
     */
    public void ByParaentId(StringRequestCallBack callback, int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeCounselorListByParaentId, ZWConfig.URL_getSearchCounselorListByParaentId, params, callback);
    }

    /**
     * 未开始
     *
     * @param type
     * @param termType
     * @param pageNo
     * @param callback
     */
    public void getNotDataList(String type, String termType, int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        params.put("termType", termType);
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetNotDataList, ZWConfig.URL_selectZoomLiveByParaentIdNoSatrt, params, callback);
    }

    /**
     * @param type
     * @param termType
     * @param pageNo
     * @param callback
     * @param status   1进行中 2已结束
     */
    public void getOngDataList(String status, String type, String termType, int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("status", status);
        params.put("type", type);
        params.put("termType", termType);
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetOngDataList, ZWConfig.URL_selectZoomLiveByParaentId, params, callback);
    }

}
