package com.urgoo.account.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.NavToolBarActivity;
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
public class RegistActivity extends NavToolBarActivity implements View.OnClickListener {
    private Button btnNext;
    private TextView tvCode;
    private TimeCount timeCount;
    private EditText etPhone;
    private EditText etCode;

    private String phone;
    private String code;
    private String userId;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_regist, null);
        timeCount = new TimeCount(60000, 1000);
        setNavTitleText("注册");
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etCode = (EditText) view.findViewById(R.id.et_code);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        tvCode.setOnClickListener(this);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
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

    private void getVerifyCode(String phone) {
        AccountManager.getInstance(this).getVerifyCode(phone, this);
    }

    private void regist(String nickName, String phoneNum, String identifyingCode) {
        showLoadingDialog();
        AccountManager.getInstance(this).regist(nickName, phoneNum, identifyingCode, this);
    }

    private void loginUrgoo(String username, String password) {
        AccountManager.getInstance(this).loginUrgoo(username, password, this);
    }

    /**
     * 输入合法性
     *
     * @return
     */
    private boolean check() {
        phone = etPhone.getText().toString();
        code = etCode.getText().toString();
        if (Util.isEmpty(phone)) {
            showToastSafe("手机号不能为空");
            return false;
        }
        if (phone.length() != 11) {
            showToastSafe("请输入正确的手机号码");
            return false;
        }
        if (Util.isEmpty(code)) {
            showToastSafe("验证码不能为空");
            return false;
        }
        return true;
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeRegistContent:
                break;
            case EventCodeLoginUrgoo:
                dismissLoadingDialog();
                try {
                    String token = new JSONObject(result.get("body").toString()).getString("token");
                    String hxid = new JSONObject(result.get("body").toString()).getString("userHxCode");
                    APPManagerTool.setGrowingIOCS(userId, phone, phone);
                    spManager.setToken(token);
                    spManager.setUserId(userId);
                    spManager.setUserName(phone);
                    spManager.setHxCode(hxid);
                    EaseHelper.longin(hxid, ZWConfig.ACTION_HXPWD);
                    if (new JSONObject(result.get("body").toString()).getInt("question") == 0) {
                        Util.openActivity(this, SurveyActivity.class);
                        finish();
                    } else {
                        // 进入主页面
                        Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                break;
            case EventCodeGetVerifyCode:
                showToastSafe("验证码已发送，请注意查收");
                countDown();
                break;
            case EventCodeRegist:
                try {
                    String hxId = new JSONObject(result.get("body").toString()).getString("userHxCode");
                    userId = new JSONObject(result.get("body").toString()).getString("userId");
                    JpushUtlis.setAlias(getApplicationContext(), userId, new TagAliasCallback() {
                        @Override
                        public void gotResult(int mI, String mS, Set<String> mSet) {
                            android.util.Log.d("alias", "设置alias为 :  " + mS);
                        }
                    });
                    loginUrgoo(phone, code);
                    registerToHuanXinserver(hxId, ZWConfig.ACTION_HXPWD);
                } catch (JSONException e) {
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
     * @Title: registerToHuanXinserver
     * @Description: 环信服务器登录
     */
    private void registerToHuanXinserver(final String username, final String pwd) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMClient.getInstance().createAccount(username, pwd);
                    spManager.setUserName(username);
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dismissLoadingDialog();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_code:
                if (Util.isEmpty(etPhone.getText().toString())) {
                    showToastSafe("手机号码不能为空");
                    etPhone.requestFocus();
                    return;
                }
                getVerifyCode(etPhone.getText().toString());
                break;
            case R.id.btn_next:
                if (check()) {
//                    regist(phone, code);
                }
                break;
        }
    }
}
