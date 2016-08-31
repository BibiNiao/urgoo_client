package com.urgoo.order.biz;

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
     * 获取订单信息
     *
     * @param callback
     */
    public void getInfo(StringRequestCallBack callback, String serviceId, String counselorId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("serviceId", serviceId);
        params.put("counselorId", counselorId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.parentOrderPay, ZWConfig.Action_confirmService, params, callback);
    }

    /**
     * 下单
     *
     * @param callback
     */
    public void insertOrder(StringRequestCallBack callback, String serviceId, String counselorId, String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("serviceId", serviceId);
        params.put("counselorId", counselorId);
        params.put("orderCode", orderCode);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.insertOrder, ZWConfig.Action_insertOrder, params, callback);
    }

    /**
     * 订单timeline
     *
     * @param callback
     * @param serviceType
     * @param grade
     */
    public void getPayTimeDetail(StringRequestCallBack callback, String serviceType, String grade) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("serviceType", serviceType);
        params.put("grade", grade);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetPayTimeDetail, ZWConfig.URL_requestGetPayTimeDetail, params, callback);
    }


    /**
     * 返回订单信息
     *
     * @param callback
     */
    public void setLinkData(StringRequestCallBack callback, HashMap<String, String> params) {
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.insertOrderProtocol, ZWConfig.URL_insertOrderProtocol, params, callback);
    }

    /**
     * 我的二维码
     *
     * @param callback
     */
    public void getQrcode(StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("role", "2");
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.createQrcode, ZWConfig.URL_createQrcode, params, callback);
    }


}
