package com.urgoo.message.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/8/18.
 */
public class MessageManager {
    private static MessageManager sInstance;
    private Context mContext;

    private MessageManager(Context context) {
        this.mContext = context;
    }


    public static MessageManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new MessageManager(context);
        }
        return sInstance;
    }

    /**
     * 系统消息列表
     *
     * @param callback
     */
    public void getSysMessageList(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("termType", "2");
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSysMessageList, ZWConfig.URL_requestGetMessageList, params, callback);
    }

    /**
     * 系统消息详情
     *
     * @param callback
     */
    public void getSysMessageDetail(int page, String type, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeSysMessageDetailList, ZWConfig.URL_requestGetSysMessageList, params, callback);
    }

    /**
     * 更新系统消息
     *
     * @param informationId
     * @param unread
     * @param callback
     */
    public void selectInformationSystemDetail(String informationId, String unread, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("termType", "2");
        params.put("informationId", informationId);
        params.put("unread",unread);
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeUpdateMessage, ZWConfig.Action_updateInformation, params, callback);
    }

    /**
     * 更新系统消息
     *
     * @param informationId
     * @param unread
     * @param callback
     */
    public void selectInformationUserDetail(String informationId, String unread, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("termType", "2");
        params.put("informationId", informationId);
        params.put("unread",unread);
        params.put("token", SPManager.getInstance(mContext).getToken());
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeUpdateUserMessage, ZWConfig.Action_updateUserInformation, params, callback);
    }
}