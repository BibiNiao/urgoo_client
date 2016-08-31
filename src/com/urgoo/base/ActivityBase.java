package com.urgoo.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.urgoo.Interface.ActivityInterface;
import com.urgoo.client.R;
import com.urgoo.common.ScreenManager;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.view.LoadingDialog;
import com.zw.express.tool.image.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

public class ActivityBase extends Activity implements ActivityInterface, StringRequestCallBack {

    public ImageLoader imgLoader;
    /**
     * 加载中进度框
     */
    protected LoadingDialog loadingDialog;
    protected int mUIThreadId;
    /**
     * Activity 管理器对象
     */
    protected ScreenManager mScreenManager;
    /**
     * app信息缓存管理器对象
     */
    protected SPManager spManager;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mUIThreadId = Process.myTid();
        super.onCreate(savedInstanceState);
        spManager = SPManager.getInstance(this);
        mScreenManager = ScreenManager.getInstance();
        mScreenManager.pushActivity(this);
//        initView();
        initTool();
        //initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mUIThreadId = Process.myTid();
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        super.onDestroy();
    }

    //*******************华丽的分隔线***************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    //*******************华丽的分隔线***************

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        onFinish();
        super.onBackPressed();
    }

    public void onFinish() {
        overridePendingTransition(R.anim.stay_remain, R.anim.out_to_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftInput(this.getCurrentFocus());
        return super.onTouchEvent(event);
    }

    public void hideSoftInput(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //*******************华丽的分隔线***************

    @Override
    public void initView() {

    }


    @Override
    public void initData() {

    }

    @Override
    public void initTool() {
        imgLoader = new ImageLoader(this);
    }

    @Override
    public void refreshView() {

    }

    //***********************华丽的分隔线*************************
    private void startActivityAnim() {
        overridePendingTransition(R.anim.in_from_right, R.anim.stay_remain);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startActivityAnim();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startActivityAnim();
    }

    protected void back(@IdRes int id, final Activity activity) {
        $(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    protected <T extends View> T $(@IdRes int viewId) {
        return (T) super.findViewById(viewId);
    }

    public void post(Runnable r) {
        mHandler.post(r);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }
    };

    protected void onHandleMessage(Message msg) {
        // TODO Auto-generated method stub
    }

    /**
     * 显示Toast
     *
     * @param msg      消息内容
     * @param duration 显示时长
     */
    private void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
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
     * 显示加载中对话框【默认显示为：加载中......】 如果传递Null或者空字符串，则显示默认的文字
     *
     * @param tipMessage 加载框提示文字
     */
    public void showLoadingDialog(String tipMessage) {
        if (!this.isFinishing()) {
            loadingDialog = LoadingDialog.show(this);
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

    @Override
    public void onSuccess(EventCode eventCode, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("header")) {
                JSONObject jsonCode = new JSONObject(jsonObject.get("header").toString());
                if (jsonCode.getString("code").equals("200")) {
                    onResponseSuccess(eventCode, jsonObject);
                } else {
                    dismissLoadingDialog();
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

    }

    /**
     * 请求响应成功获得数据
     *
     * @param eventCode 请求码
     * @param result    响应的JSON对象
     */
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        // TODO Auto-generated method stub
    }
}
