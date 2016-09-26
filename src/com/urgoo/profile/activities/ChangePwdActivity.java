package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.PMD5Utils;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.zw.express.tool.JsonUtil;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.VerificationTool;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by lijie on 2016/3/29.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText mCurrentPwdEt, mNewPwdEt1, mNewPwdEt2;
    private Button mCommit;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.change_pwd_activity);
        mCurrentPwdEt = $(R.id.current_pwd_ev);
        mNewPwdEt1 = $(R.id.new_pwd_ev);
        mNewPwdEt2 = $(R.id.confirm_password_ev);
        mCommit = $(R.id.submit_btn);
        pd = new ProgressDialog(this);
        mCommit.setOnClickListener(this);
        back(R.id.back, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                updatePwd();
                break;
        }
    }

    private void updatePwd() {
        final String currentPwd = mCurrentPwdEt.getText().toString();
        final String newPwd1 = mNewPwdEt1.getText().toString();
        final String newPwd2 = mNewPwdEt2.getText().toString();

        if (newPwd1.length() >= 6 && newPwd1.length() < 18) {
            if (VerificationTool.verificationStr(newPwd1)) {
                UiUtil.show(this, "密码只能由数字和字母组合");
                return;
            }
        } else {
            UiUtil.show(this, "密码不能小于6位");
            return;
        }

        if (!newPwd1.equals(newPwd2)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            mNewPwdEt2.requestFocus();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
//        params.put("currentPassword", currentPwd);
//        params.put("password", newPwd1);
//        params.put("confirmPassword", newPwd2);

        params.put("currentPassword", PMD5Utils.encodeByMD5(currentPwd.toLowerCase()));
        params.put("password", PMD5Utils.encodeByMD5(newPwd1.toLowerCase()));
        params.put("confirmPassword", PMD5Utils.encodeByMD5(newPwd2.toLowerCase()));

        android.util.Log.d("mima", "currentPassword: "+PMD5Utils.encodeByMD5(currentPwd.toLowerCase()));
        android.util.Log.d("mima", "password: "+PMD5Utils.encodeByMD5(newPwd1.toLowerCase()));
        android.util.Log.d("mima", "confirmPassword: "+PMD5Utils.encodeByMD5(newPwd2.toLowerCase()));

        Log.d("uuuuuu", currentPwd + newPwd1 + newPwd2);
        Log.d("uuuuuu", PMD5Utils.encodeByMD5(currentPwd.toLowerCase()) + PMD5Utils.encodeByMD5(newPwd1.toLowerCase()) + PMD5Utils.encodeByMD5(newPwd2.toLowerCase()));

        OkHttpClientManager.postAsyn(ZWConfig.ACTION_updatePassword, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                JsonUtil.success(ChangePwdActivity.this, response);

                JSONObject j;
                JSONArray ja = null;
                try {
                    j = new JSONObject(response);

                    String code = new JSONObject(j.get("header").toString()).getString("code");
                    //String userLoginId=new JSONObject(j.get("body").toString()).getString("userLoginId");
                    //String hxid=new JSONObject(j.get("body").toString()).getString("userHxCode");
                    if (code.equals("200")) {
                        //verify_ev.setText(text);
                        startActivity(new Intent(ChangePwdActivity.this, ChangePwdSuccessActivity.class));
                        ChangePwdActivity.this.finish();
                    } else if (code.equals("404")) {
                        ChangePwdActivity.this.isFinishing();
                        pd.dismiss();
                    } else {
                        Toast.makeText(ChangePwdActivity.this,
                                new JSONObject(j.get("header").toString()).getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
        }, params);


    }

}
