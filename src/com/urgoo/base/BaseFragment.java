package com.urgoo.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.view.LoadingDialog;
import com.zw.express.tool.Util;

import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by bb on 2016/7/11.
 */
public class BaseFragment extends Fragment implements StringRequestCallBack {
    /**
     * Fragment 内容
     */
    protected View viewContent;
    /**
     * 加载中进度框
     */
    protected LoadingDialog loadingDialog;
    private int mUIThreadId;

    @Override
    public void onAttach(Activity activity) {
        mUIThreadId = Process.myTid();
        super.onAttach(activity);
    }

    public void post(Runnable r) {
        mHandler.post(r);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }
    };

    protected void onHandleMessage(Message msg) {
        // TODO Auto-generated method stub
    }

    /**
     * 默认显示3s
     *
     * @param text
     */
    public void showToastSafe(final String text) {
        showToastSafe(text, 3 * 1000);
    }

    /**
     * 默认显示3s
     *
     * @param resId
     */
    public void showToastSafe(final int resId) {
        showToastSafe(getString(resId), 3 * 1000);
    }

    public void showToastSafe(final String text, final int duration) {
        if (Process.myTid() == mUIThreadId) {
            showToast(text, duration);
        } else {
            post(new Runnable() {

                @Override
                public void run() {
                    showToast(text, duration);
                }
            });
        }
    }

    public void showToastSafe(final int resId, final int duration) {
        showToastSafe(getString(resId), duration);
    }

    /**
     * 显示Toast
     *
     * @param msg      消息内容
     * @param duration 显示时长
     */
    private void showToast(String msg, int duration) {
        if (isAdded())
            Toast.makeText(getActivity(), msg, duration).show();
    }

    @Override
    public void onSuccess(EventCode eventCode, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("header")) {
                JSONObject jsonCode = new JSONObject(jsonObject.get("header").toString());
                String code = jsonCode.getString("code");
                if (code.equals("200")) {
                    onResponseSuccess(eventCode, jsonObject);
                } else if (code.equals("602")) { //token过期
                    dismissLoadingDialog();
                    Util.onFreezeAccount(getActivity(), jsonCode.getString("message"));
                } else {
                    showToastSafe(jsonCode.getString("message"));
                }
            } else {
                onResponseSuccess(eventCode, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(EventCode eventCode, Call call) {
        dismissLoadingDialog();
    }

    /**
     * 请求响应成功获得数据
     *
     * @param eventCode 请求码
     * @param result    JSONObject 响应对象
     */
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        // TODO Auto-generated method stub
    }


    /**
     * 显示加载中对话框【默认显示为：加载中......】 如果传递Null或者空字符串，则显示默认的文字
     *
     * @param tipMessage 加载框提示文字
     */
    protected void showLoadingDialog(String tipMessage) {
        if (!getActivity().isFinishing()) {
            loadingDialog = LoadingDialog.show(getActivity());
            loadingDialog.setCancelable(false);
            loadingDialog.setTipMessage(tipMessage);
        }
    }

    /**
     * 显示加载中对话框【默认显示为：加载中......】 如果传递Null或者空字符串，则显示默认的文字
     *
     * @param resId 加载框提示文字资源ID
     */
    public void showLoadingDialog(int resId) {
        showLoadingDialog(getString(resId));
    }

    /**
     * 显示默认的对话框【加载中......】
     */
    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    /**
     * 隐藏加载中对话框
     */
    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
