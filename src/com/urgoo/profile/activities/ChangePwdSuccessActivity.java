package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.urgoo.account.activity.LoginActivity;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.message.EaseHelper;

/**
 * Created by lijie on 2016/4/5.
 */
public class ChangePwdSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd_success);
        Button button = $(R.id.commit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        logout();
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(ChangePwdSuccessActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        EaseHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                ChangePwdSuccessActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        // ChangePwdSuccessActivity.this.finish();
                        spManager.clearLoginInfo();
                        Intent intent = new Intent(ChangePwdSuccessActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });
    }


}
