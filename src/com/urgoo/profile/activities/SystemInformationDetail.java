package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.urgoo.account.activity.HomeActivity;
import com.urgoo.base.BaseActivity;
import com.urgoo.common.ZWConfig;
import com.urgoo.client.R;
import com.urgoo.domain.InformationDetailEntity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.biz.ProfileManager;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SystemInformationDetail extends BaseActivity {


    private TextView message_title;
    private TextView tv_messagecontent;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.systeminformationdetail);

        initView();
        initData();

    }

    @Override
    public void initView() {

        message_title = (TextView) findViewById(R.id.message_title);
        tv_messagecontent = (TextView) findViewById(R.id.tv_messagecontent);

        $(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemInformationDetail.this.finish();
                /*overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);*/
            }
        });
    }

    @Override
    public void initData() {
        Intent it = getIntent();
        type = it.getIntExtra("type", 0);
        String informationId = it.getStringExtra("informationId");
        String unread = it.getStringExtra("unread");
        tv_messagecontent.setText(it.getStringExtra("content"));
        if (type == 1) {
            updateMessage(informationId, unread);
        } else {
            updateUserMessage(informationId, unread);
        }
    }

    private void updateUserMessage(String informationId, String unread) {
        ProfileManager.getInstance(this).selectInformationUserDetail(informationId, unread, this);
    }

    private void updateMessage(String informationId, String unread) {
        ProfileManager.getInstance(this).selectInformationSystemDetail(informationId, unread, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeUpdateMessage:
                break;
        }
    }
}
