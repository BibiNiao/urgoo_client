package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.PMD5Utils;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.VerificationTool;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by lijie on 2016/4/5.
 */

public class SetPasswordActivity extends ActivityBase implements View.OnClickListener {
    private EditText mNewPwdEt1, mNewPwdEt2;
    public static final String SET_PWD_PHONE_EXTRA = "SET_PWD_PHONE_EXTRA";
    private Button mCommit;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity);
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
                changgPwd();
                break;
        }
    }

    private void changgPwd() {

        final String phone = getIntent().getStringExtra(SET_PWD_PHONE_EXTRA);
        final String pwd1 = mNewPwdEt1.getText().toString();
        final String pwd2 = mNewPwdEt2.getText().toString();

        if(pwd1.length()>=6&&pwd1.length()<18) {
            if (VerificationTool.verificationStr(pwd1)) {
                UiUtil.show(this, "密码只能由数字和字母组合");
                return;
            }
        }else {
            UiUtil.show(this, "密码不能小于6位");
            return;
        }
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            mNewPwdEt2.requestFocus();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();

        params.put("phoneNum", phone);
        params.put("password", PMD5Utils.encodeByMD5(pwd1.toLowerCase()));
        params.put("confirmPassword", PMD5Utils.encodeByMD5(pwd2.toLowerCase()));
//        params.put("password", pwd1);
//        params.put("confirmPassword", pwd2);
        Log.d("mima", "password: "+PMD5Utils.encodeByMD5(pwd1.toLowerCase()));
        Log.d("mima", "confirmPassword: "+PMD5Utils.encodeByMD5(pwd2.toLowerCase()));

        OkHttpClientManager.postAsyn(ZWConfig.ACTION_clientupdatePassword, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(response);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            //String userLoginId=new JSONObject(j.get("body").toString()).getString("userLoginId");
                            //String hxid=new JSONObject(j.get("body").toString()).getString("userHxCode");
                            if (code.equals("200")) {
                                //verify_ev.setText(text);
                                Intent i = new Intent(SetPasswordActivity.this, ChangePwdSuccessActivity.class);
                                startActivity(i);
                                SetPasswordActivity.this.finish();
                            } else if (code.equals("404")) {
                                SetPasswordActivity.this.isFinishing();
                                pd.dismiss();
                            } else {
                                Toast.makeText(SetPasswordActivity.this,
                                        new JSONObject(j.get("header").toString()).getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                    }
                }
                , params);
    }

}
