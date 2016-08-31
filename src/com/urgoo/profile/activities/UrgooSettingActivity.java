package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.DemoHelper;
import com.urgoo.client.DemoModel;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;

public class UrgooSettingActivity extends ActivityBase implements OnClickListener {
    private LinearLayout back;

    /**
     * 设置新消息通知布局
     */
    private RelativeLayout rl_switch_notification;

    /**
     * 退出按钮
     */
    private Button logoutBtn;

    private EaseSwitchButton notifiSwitch;
    private DemoModel settingsModel;
    private EMOptions chatOptions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urgoosettingactivity_layout);
        initView();

    }

    @Override
    public void initView() {
        $(R.id.re_wode_yyue).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrgooSettingActivity.this, ChangePwdActivity.class));
            }
        });

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UrgooSettingActivity.this.finish();

            }
        });
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);

        notifiSwitch = (EaseSwitchButton) findViewById(R.id.switch_notification);


        logoutBtn = (Button) findViewById(R.id.btn_logout);
        settingsModel = DemoHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();
        rl_switch_notification.setOnClickListener(this);

        logoutBtn.setOnClickListener(this);

        // 震动和声音总开关，来消息时，是否允许此开关打开
        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifiSwitch.openSwitch();
        } else {
            notifiSwitch.closeSwitch();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_switch_notification:
                if (notifiSwitch.isSwitchOpen()) {
                    notifiSwitch.closeSwitch();


                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifiSwitch.openSwitch();

                    settingsModel.setSettingMsgNotification(true);
                }
                break;

            case R.id.btn_logout: //退出登陆
                logout();
                break;

            default:
                break;
        }

    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(UrgooSettingActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                UrgooSettingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        SPManager.getInstance(UrgooSettingActivity.this).clearLoginInfo();
                        Intent intent = new Intent(UrgooSettingActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        UrgooSettingActivity.this.finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                UrgooSettingActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(UrgooSettingActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }


}
