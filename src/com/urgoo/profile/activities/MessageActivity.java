package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.biz.ProfileManager;
import com.urgoo.profile.model.MessageEntity;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bb on 2016/9/7.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvSys;
    private TextView tvMy;
    private MessageEntity messageEntity;
    public final static String MESSAGE_TYPE = "message_type";
    public static final int REQUEST_CODE_MESSAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        getMessageList("", 0);
    }

    @Override
    public void initView() {
        tvSys = (TextView) findViewById(R.id.tv_num_sys);
        tvMy = (TextView) findViewById(R.id.tv_num_my);
        findViewById(R.id.ll_assistant_sys).setOnClickListener(this);
        findViewById(R.id.ll_assistant_my).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void getMessageList(String type, int page) {
        ProfileManager.getInstance(this).getSelectInformationPerson(type, page, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeMessageList:
                try {
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    messageEntity = gson.fromJson(result.get("body").toString(), new TypeToken<MessageEntity>() {
                    }.getType());
                    if (messageEntity.getSystemCount() > 0) {
                        tvSys.setVisibility(View.VISIBLE);
                    } else {
                        tvSys.setVisibility(View.GONE);
                    }
                    if (messageEntity.getPersonCount() > 0) {
                        tvMy.setVisibility(View.VISIBLE);
                    } else {
                        tvMy.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Bundle extras = new Bundle();
        switch (v.getId()) {
            case R.id.ll_assistant_sys:
                extras.putInt(MESSAGE_TYPE, 1);
                Util.openActivityForResultWithBundle(MessageActivity.this, MessageListActivity.class, extras, REQUEST_CODE_MESSAGE);
                break;
            case R.id.ll_assistant_my:
                extras.putInt(MESSAGE_TYPE, 2);
                Util.openActivityForResultWithBundle(MessageActivity.this, MessageListActivity.class, extras, REQUEST_CODE_MESSAGE);
                break;
            case R.id.back:
                if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                    extras.putInt(MainActivity.EXTRA_TAB, 3);
                    Util.openActivityWithBundle(MessageActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getMessageList("", 0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                Bundle extras = new Bundle();
                extras.putInt(MainActivity.EXTRA_TAB, 3);
                Util.openActivityWithBundle(MessageActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
