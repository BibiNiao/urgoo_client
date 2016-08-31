package com.urgoo.net;

import android.content.Context;
import android.util.Log;

import com.zw.express.tool.net.OkHttpClientManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by bb on 2016/7/7.
 */
public class HttpEngine {
    private volatile static HttpEngine sInstance;
    private Context mContext;

    private HttpEngine(Context context) {
        this.mContext = context;
    }

    public synchronized static HttpEngine getInstance(Context context) {
        if (null == sInstance) {
            synchronized (HttpEngine.class) {
                if (null == sInstance) sInstance = new HttpEngine(context);
            }
        }
        return sInstance;
    }

    /**
     * 发送POST协议的Http请求
     *
     * @param eventCode
     *            EventCode 事件编码
     * @param url
     *            请求URL
     * @param params
     *            HashMap<String, Object> 请求参数
     * @param callback
     *            StringRequestCallBack 请求的回调对象
     */
    public void sendPostRequest(final EventCode eventCode, final String url,
                                HashMap<String, String> params, final StringRequestCallBack callback) {
        Log.d("===> url:", url);
        Log.d("===> request params:", String.valueOf(params));
        sendRequest(eventCode, url, params, callback);
    }

    /**
     * 发送字符串协议的Http请求
     *
     * @param eventCode
     *            EventCode 事件编码
     * @param url
     *            请求URL
     * @param params
     *            请求参数
     * @param callback
     *            StringRequestCallBack 请求的回调对象
     */
    public void sendRequest(final EventCode eventCode, final String url,
                            Map<String, String> params, final StringRequestCallBack callback) {
        OkHttpClientManager.getInstance().postAsyn(url, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {
                Log.d("===> onError eventCode:", eventCode + ", onError:" + call);
                if (callback != null) {
                    callback.onFailure(eventCode, call);
                }
            }

            @Override
            public void onResponse(String response) {
                Log.d("===> onSuccess eventCode:", eventCode + ", onSuccess:" + response);
                if (callback != null) {
                    callback.onSuccess(eventCode, response);
                }
            }
        }, params);
    }
}
