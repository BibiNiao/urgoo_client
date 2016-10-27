package com.urgoo.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.ZWConfig;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.message.EaseHelper;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by bb on 2016/8/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etCode;
    private TextView tvCode;
    private String username;
    private String code;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        timeCount = new TimeCount(60000, 1000);
        initView();
    }

    /**
     * 倒计时
     */
    private void countDown() {
        tvCode.setClickable(false);
        timeCount.start();
    }

    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCode.setText(String.valueOf(millisUntilFinished / 1000 + "秒"));
        }

        @Override
        public void onFinish() {
            tvCode.setText("获取验证码");
            tvCode.setClickable(true);
        }
    }

    @Override
    public void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.et_username);
        etCode = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvCode.setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    private void login() {
        username = etUsername.getText().toString();
        code = etCode.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToastSafe(R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToastSafe("验证码不能为空");
            return;
        }
        loginUrgoo(username, code);
    }

    private void loginUrgoo(String username, String password) {
        showLoadingDialog();
        AccountManager.getInstance(this).loginUrgoo(username, password, this);
    }

    private void getVerifyCodeLogin(String username) {
        AccountManager.getInstance(this).getVerifyCodeLogin(username, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetVerifyCodeLogin:
                showToastSafe("验证码已发送，请注意查收");
                countDown();
                break;
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
                        EaseHelper.longin(hxid, ZWConfig.ACTION_HXPWD);
                        // 进入主页面
                        if (new JSONObject(result.get("body").toString()).getInt("question") == 0) {
                            Util.openActivity(this, SurveyActivity.class);
                        } else {
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                if (Util.isEmpty(etUsername.getText().toString())) {
                    showToastSafe("手机号码不能为空");
                    etUsername.requestFocus();
                    return;
                }
                getVerifyCodeLogin(etUsername.getText().toString());
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }
}
