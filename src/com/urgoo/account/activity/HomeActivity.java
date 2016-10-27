package com.urgoo.account.activity;

import android.os.Bundle;
import android.view.View;

import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bb on 2016/8/15.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    public void initView() {
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_regist).setOnClickListener(this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeQuestionList:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body").toString());
                    Bundle bundle = new Bundle();
                    bundle.putString(SurveyActivity.SurveyInfo, jsonObject.getString("questionList"));
                    Util.openActivityWithBundle(this, SurveyActivity.class, bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Util.openActivity(this, LoginActivity.class);
                break;
            case R.id.btn_regist:
                Util.openActivity(this, RegistActivity.class);

//                getQuestionList();
                break;
        }
    }
}
