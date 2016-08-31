package com.urgoo.schedule.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.client.DemoHelper;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.message.activities.BaseActivity;
import com.urgoo.profile.activities.Aboutme;
import com.urgoo.profile.activities.ChangePwdActivity;

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

public class SetingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout LinLyout_schedule_back;
    private EaseSwitchButton notifiSwitch;
    private RelativeLayout rl_switch_notification;
    private RelativeLayout re_passwordChange;
    private RelativeLayout re_Aboutus;
    private Button tv_Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        initView();
        initListener();
    }

    private void initView() {
        LinLyout_schedule_back = (LinearLayout) findViewById(R.id.LinLyout_schedule_back);
        notifiSwitch = (EaseSwitchButton) findViewById(R.id.switch_notification);
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        re_passwordChange = (RelativeLayout) findViewById(R.id.re_passwordChange);
        re_Aboutus = (RelativeLayout) findViewById(R.id.re_Aboutus);
        tv_Logout = (Button) findViewById(R.id.tv_Logout);
    }

    private void initListener() {
        LinLyout_schedule_back.setOnClickListener(this);
        rl_switch_notification.setOnClickListener(this);
        re_passwordChange.setOnClickListener(this);
        re_Aboutus.setOnClickListener(this);
        tv_Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinLyout_schedule_back:
                finish();
                break;
            case R.id.rl_switch_notification:
                if (notifiSwitch.isSwitchOpen()) {
                    close();
                } else {
                    open();
                }
                break;
            case R.id.re_passwordChange:    //修改密码
                startActivity(new Intent(SetingActivity.this, ChangePwdActivity.class));
                break;
            case R.id.re_Aboutus:           //关于我们
                startActivity(new Intent(SetingActivity.this, Aboutme.class));
                break;
            case R.id.tv_Logout:            //退出
                logout();
                break;


        }
    }

    private void close() {
        notifiSwitch.closeSwitch();
        JpushUtlis.stopJpush(getApplicationContext());
    }

    private void open() {
        notifiSwitch.openSwitch();
        JpushUtlis.resumePush(getApplicationContext());
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(SetingActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        JpushUtlis.setAlias(SetingActivity.this, "", new TagAliasCallback() {
                            @Override
                            public void gotResult(int mI, String mS, Set<String> mSet) {
                                android.util.Log.d("alias", "设置alias为 :  " + mS);
                            }
                        });
                        // 重新显示登陆页面
                        SPManager.getInstance(SetingActivity.this).clearLoginInfo();
                        Intent intent = new Intent(SetingActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SetingActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
