/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.urgoo.message.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.urgoo.common.ScreenManager;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

public class BaseActivity extends EaseBaseActivity implements
        StringRequestCallBack {
    /**
     * 加载中进度框
     */
    protected LoadingDialog loadingDialog;
    /**
     * Activity 管理器对象
     */
    protected ScreenManager mScreenManager;
    /**
     * app信息缓存管理器对象
     */
    protected SPManager spManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        mScreenManager = ScreenManager.getInstance();
        mScreenManager.pushActivity(this);
        spManager = SPManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
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
            onResponseSuccess(eventCode, jsonObject);
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
