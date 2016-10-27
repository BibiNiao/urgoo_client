package com.urgoo.account.biz;

import android.content.Context;

import com.urgoo.common.PMD5Utils;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/8/15.
 */
public class AccountManager {
    private static AccountManager sInstance;
    private Context mContext;

    private AccountManager(Context context) {
        this.mContext = context;
    }


    public static AccountManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new AccountManager(context);
        }
        return sInstance;
    }

    /**
     * 登录自己服务器
     *
     * @param username
     * @param password
     * @param callback
     */
    public void loginUrgoo(String username, String password, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeLoginUrgoo, ZWConfig.ACTION_Login, params, callback);
    }

    /**
     * 问卷调查
     *
     * @param callback
     */
    public void selectQuestionListAll(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeQuestionList, ZWConfig.URL_requestGetQuestionList, params, callback);
    }


    /**
     * 提交问卷
     *
     * @param userId
     * @param questionJson
     * @param callback
     */
    public void setRegistContent(String userId, String questionJson, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("questionJson", questionJson);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeRegistContent, ZWConfig.URL_requestRegistContent, params, callback);
    }

    /**
     * 获取登录验证码
     *
     * @param phoneNum
     * @param callback
     */
    public void getVerifyCodeLogin(String phoneNum, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetVerifyCodeLogin, ZWConfig.URL_requestGetLoginCode, params, callback);
    }

    /**
     * 获取验证码
     *
     * @param phoneNum
     * @param callback
     */
    public void getVerifyCode(String phoneNum, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetVerifyCode, ZWConfig.ACTION_ClientVerifycode, params, callback);
    }

    /**
     * 注册
     *
     * @param nickName
     * @param phoneNum
     * @param identifyingCode
     * @param callback
     */
    public void regist(String nickName, String phoneNum, String identifyingCode, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("phoneNum", phoneNum);
        params.put("identifyingCode", identifyingCode);
        params.put("password", PMD5Utils.encodeByMD5("12345"));
        params.put("confirmPassword", PMD5Utils.encodeByMD5("12345"));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeRegist, ZWConfig.ACTION_ClientRegist, params, callback);
    }

    /**
     * 获取用户信息
     *
     * @param callback
     */
    public void getUserInfo(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetUserInfo, ZWConfig.URL_requestGetUserInfo, params, callback);
    }

    /**
     * 活动页面
     *
     * @param callback
     */
    public void getMyAcitivtes(int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetMyAcitivites, ZWConfig.URL_requestMyActivites, params, callback);
    }

    /**
     * 获取用户资料
     *
     * @param callback
     */
    public void getUserData(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetUserData, ZWConfig.URL_requestGetUserData, params, callback);
    }


    /**
     * 修改昵称
     *
     * @param callback
     */
    public void updateNickName(String nickName, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeUpdateNickName, ZWConfig.URL_requestUpdateNickName, params, callback);
    }
}
