package com.urgoo.pay.biz;

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
public class PayManager {
    private static PayManager sInstance;
    private Context mContext;

    private PayManager(Context context) {
        this.mContext = context;
    }


    public static PayManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new PayManager(context);
        }
        return sInstance;
    }

    /**
     * 支付宝支付详情
     *
     * @param subject
     * @param body
     * @param price
     * @param orderId
     * @param callback
     */
    public void getAliPayDetail(String subject, String body, String price, String orderId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("subject", subject);
        params.put("body", body);
        params.put("total_fee", price);
        params.put("orderId", orderId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeAliPayDetail, ZWConfig.ACTION_requestLaunchAliPay, params, callback);
    }

    /**
     * 微信支付详情
     *
     * @param body
     * @param price
     * @param orderId
     * @param callback
     */
    public void getWechatDetail(String body, String price, String orderId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("body", body);
        params.put("totalFee", price);
        params.put("payRequestOrderId", orderId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeWechatPayDetail, ZWConfig.ACTION_requestLaunchWechat, params, callback);
    }

    /**
     * 银行卡支付详情
     *
     * @param body
     * @param price
     * @param orderId
     * @param callback
     */
    public void getHuaRuiPayLaunch(String body, String price, String orderId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("body", body);
        params.put("totalFee", price);
        params.put("orderId", orderId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeHuaRuiPay, ZWConfig.ACTION_requestHuaRuiPay, params, callback);
    }

    /**
     * 请求订单详情
     *
     * @param orderId
     * @param callback
     */
    public void getOrder(String orderId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("orderId", orderId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetPayOrder, ZWConfig.ACTION_getOrderBalanceMoney, params, callback);
    }


    /**
     * 更新订单
     *
     * @param orderId
     * @param pice
     * @param payStatus
     * @param payRequestOrderId
     * @param callback
     */
    public void getUpdateUser(String orderId, String pice, String payStatus, String payRequestOrderId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("orderId", orderId);
        params.put("totalFee", pice);
        params.put("payStatus", payStatus);
        params.put("payRequestOrderId", payRequestOrderId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeUpdateUser, ZWConfig.ACTION_insertSubOrderDetail, params, callback);
    }
}
