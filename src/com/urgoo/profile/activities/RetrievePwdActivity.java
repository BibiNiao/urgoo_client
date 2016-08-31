package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
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
public class RetrievePwdActivity extends ActivityBase implements View.OnClickListener {
    private static final String TAG = RetrievePwdActivity.class.getName();
    private EditText mPhoneTv, mVerifyTv;
    private Button mVerifyBtn, mCommitBtn;
    private ProgressDialog pd;
    private TimeCount timeCount;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.retrieve_pwd_activity);
        mPhoneTv = $(R.id.phone_ev);
        mVerifyTv = $(R.id.validate_tv);
        mVerifyBtn = $(R.id.validate_btn);
        mCommitBtn = $(R.id.submit_btn);
        pd = new ProgressDialog(this);
        timeCount = new TimeCount(60000, 1000);
        mVerifyBtn.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        back(R.id.back, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                essureVerify();
                break;
            case R.id.validate_btn:
                requestVerifyCode();
                break;
        }

    }


    private void essureVerify() {
        final String phone = mPhoneTv.getText().toString();
        final String verifyCode = mVerifyTv.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
            mPhoneTv.requestFocus();
            return;
        }
        if (verifyCode.length() != 4) {
            Toast.makeText(this, "验证码格式不正确", Toast.LENGTH_SHORT).show();
            mVerifyTv.requestFocus();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum", phone);
        params.put("activationCode", verifyCode);
        OkHttpClientManager.postAsyn(ZWConfig.ACTION_clientFindPassword,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(response);
                            Log.d(TAG, "uuuuuu: "+j);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            //String userLoginId=new JSONObject(j.get("body").toString()).getString("userLoginId");
                            //String hxid=new JSONObject(j.get("body").toString()).getString("userHxCode");
                            if (code.equals("200")) {
                                //verify_ev.setText(text);
                                Intent i = new Intent(RetrievePwdActivity.this, SetPasswordActivity.class);
                                i.putExtra(SetPasswordActivity.SET_PWD_PHONE_EXTRA, phone);
                                startActivity(i);
                                RetrievePwdActivity.this.finish();
                            } else if (code.equals("404")) {
                                RetrievePwdActivity.this.isFinishing();
                                pd.dismiss();
                            } else {
                                Toast.makeText(RetrievePwdActivity.this,
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

    private void requestVerifyCode() {
        final String phone = mPhoneTv.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
            mPhoneTv.requestFocus();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();

        //params.put("phoneNum", "13701891776");

        params.put("phoneNum", phone);
        /*if (TextUtils.isEmpty(phoneNum)) {
			phont_ev.requestFocus();
			return;
		}*/
        countDown();
        OkHttpClientManager.postAsyn(ZWConfig.ACTION_IdentifyingCode,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {
                        //mTv.setText(u);// 注意这里是UI线程

                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            //String userLoginId=new JSONObject(j.get("body").toString()).getString("userLoginId");
                            //String hxid=new JSONObject(j.get("body").toString()).getString("userHxCode");
                            if (code.equals("200")) {
                                //verify_ev.setText(text);
                            } else if (code.equals("404")) {
                                RetrievePwdActivity.this.isFinishing();
                                pd.dismiss();
                            } else {
                                Toast.makeText(RetrievePwdActivity.this,
                                        new JSONObject(j.get("header").toString()).getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                    }
                }, params);
    }


    private void countDown() {
        mVerifyBtn.setClickable(false);
        timeCount.start();

    }

    @Override
    public void onFailure(EventCode eventCode, Call call) {

    }

    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mVerifyBtn.setText(String.valueOf(millisUntilFinished / 1000 + "秒"));

        }

        @Override
        public void onFinish() {
            mVerifyBtn.setText("验证码");
            mVerifyBtn.setClickable(true);
        }
    }

}
