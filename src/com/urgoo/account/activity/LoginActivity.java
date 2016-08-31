package com.urgoo.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.BaseApplication;
import com.urgoo.client.DemoHelper;
import com.urgoo.client.R;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.PMD5Utils;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.db.DemoDBManager;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.activities.RetrievePwdActivity;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by bb on 2016/8/15.
 */
public class LoginActivity extends ActivityBase implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.setOnKeyListener(onKey);
        findViewById(R.id.tv_forgetpw).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    private void login() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToastSafe(R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToastSafe(R.string.Password_cannot_be_empty);
            return;
        }
        password = PMD5Utils.encodeByMD5(password.toLowerCase());
        loginUrgoo(username, password);
    }

    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                login();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        }

    };

    private void loginUrgoo(String username, String password) {
        showLoadingDialog();
        AccountManager.getInstance(this).loginUrgoo(username, password, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeLoginUrgoo:
                try {
                    String code = new JSONObject(result.get("header").toString()).getString("code");
                    if (code.equals("200")) {
                        String token = new JSONObject(result.get("body").toString()).getString("token");
                        String hxid = new JSONObject(result.get("body").toString()).getString("userHxCode");
                        String UserId = new JSONObject(result.get("body").toString()).getString("userId");
                        String nickName = new JSONObject(result.get("body").toString()).getString("nickName");
                        APPManagerTool.setGrowingIOCS(UserId, nickName, username);
                        //TODO      设置alias标识为 UserId
                        JpushUtlis.setAlias(getApplicationContext(), UserId, new TagAliasCallback() {
                            @Override
                            public void gotResult(int mI, String mS, Set<String> mSet) {
                                android.util.Log.d("alias", "设置alias为 :  " + mS);
                            }
                        });
                        spManager.setToken(token);
                        spManager.setUserId(UserId);
                        spManager.setNickName(nickName);
                        spManager.setUserName(username);
                        longinToHuanXinserver(hxid, ZWConfig.ACTION_HXPWD);
                        // 进入主页面
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * @param @param username
     * @param @param pwd    设定文件
     * @return void    返回类型
     * @throws
     * @Title: longinToHuanXinserver
     * @Description: 登录环信服务器
     */
    private void longinToHuanXinserver(final String username, final String pwd) {
        DemoDBManager.getInstance().closeDB();
        DemoHelper.getInstance().setCurrentUserName(username);
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMClient.getInstance().login(username, pwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                }
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_forgetpw:
                Util.openActivity(this, RetrievePwdActivity.class);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }
}
