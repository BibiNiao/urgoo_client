package com.urgoo.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
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
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Button btnNext;
    private TextView tvCode;
    private TextView tvRegionCode;
    private TimeCount timeCount;
    private EditText etPhone;
    private EditText etCode;

    private String phone;
    private String code;
    private String userId;
    private String regionCode;
    /**
     * 1:登录 2:注册
     */
    private String flag;
    /**
     * 选择国家
     */
    public static final int REQUEST_CODE_SELECT_COUNTRY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_home);
        timeCount = new TimeCount(60000, 1000);
        setSwipeBackEnable(false);
        initView();
    }

    private void setTranslucentStatus() {
        Window window = getWindow();
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);

        //首先使 ChildView 不预留空间
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

        int statusBarHeight = Util.getStatusBarHeight(this);
        //需要设置这个 flag 才能设置状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //避免多次调用该方法时,多次移除了 View
        if (mChildView != null && mChildView.getLayoutParams() != null && mChildView.getLayoutParams().height == statusBarHeight) {
            //移除假的 View.
            mContentView.removeView(mChildView);
            mChildView = mContentView.getChildAt(0);
        }
        if (mChildView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
            //清除 ChildView 的 marginTop 属性
            if (lp != null && lp.topMargin >= statusBarHeight) {
                lp.topMargin -= statusBarHeight;
                mChildView.setLayoutParams(lp);
            }
        }
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

    private void getVerifyCode(String regionCode, String phone) {
        AccountManager.getInstance(this).getCode(regionCode, phone, this);
    }

    private void loginUrgoo(String username, String password) {
        showLoadingDialog();
        AccountManager.getInstance(this).loginUrgoo(username, password, this);
    }

    private void regist(String nickName, String phoneNum, String identifyingCode) {
        showLoadingDialog();
        AccountManager.getInstance(this).regist(nickName, phoneNum, identifyingCode, this);
    }

    @Override
    public void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvCode.setOnClickListener(this);
        tvRegionCode = (TextView) findViewById(R.id.tv_region_code);
        tvRegionCode.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        regionCode = tvRegionCode.getText().toString();
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetVerifyCode:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body").toString());
                    flag = jsonObject.getString("flag");
                    showToastSafe("验证码已发送，请注意查收");
                    countDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeLoginUrgoo:
                try {
                    String code = new JSONObject(result.get("header").toString()).getString("code");
                    if (code.equals("200")) {
                        String token = new JSONObject(result.get("body").toString()).getString("token");
                        String hxid = new JSONObject(result.get("body").toString()).getString("userHxCode");
                        String UserId = new JSONObject(result.get("body").toString()).getString("userId");
                        String nickName = new JSONObject(result.get("body").toString()).getString("nickName");
                        APPManagerTool.setGrowingIOCS(UserId, nickName, phone);
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
                        spManager.setUserName(phone);
                        spManager.setHxCode(hxid);
                        EaseHelper.longin(hxid, ZWConfig.ACTION_HXPWD);
                        // 进入主页面
                        if (new JSONObject(result.get("body").toString()).getInt("question") == 0) {
                            Util.openActivity(this, SurveyActivity.class);
                        } else {
                            Intent intent = new Intent(HomeActivity.this,
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
                getVerifyCode(regionCode.replaceFirst("\\+", ""), etPhone.getText().toString());
                break;
            case R.id.tv_region_code:
                Util.openActivityForResult(this, SelectCountryCodeActivity.class, REQUEST_CODE_SELECT_COUNTRY);
                break;
            case R.id.btn_next:
                if (loginOrRegister()) {
                    if (flag.equals("1")) {
                        loginUrgoo(phone, code);
                    } else if (flag.equals("2")) {
                        regist(phone, phone, code);
                    }
                }
                break;
//            case R.id.btn_regist:
//                Util.openActivity(this, RegistActivity.class);
//                break;
        }
    }

    private boolean loginOrRegister() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_COUNTRY:
                    Bundle bundle = data.getExtras();
                    regionCode = bundle.getString("code");
                    tvRegionCode.setText(regionCode);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
