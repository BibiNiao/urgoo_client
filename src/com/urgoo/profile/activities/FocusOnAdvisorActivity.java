package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.adapter.CounselorListAdapter;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.activities.CounselorList;
import com.urgoo.counselor.event.FocusEvent;
import com.urgoo.domain.TranslateCounselorEntiy;
import com.urgoo.net.EventCode;
import com.urgoo.profile.biz.ProfileManager;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class FocusOnAdvisorActivity extends ActivityBase implements View.OnClickListener {

    private RelativeLayout rlNoFollow;
    private LinearLayout llBack;
    private TextView myorder_message_title;
    private UltimateRecyclerView recyclerView;
    private CounselorListAdapter counselorListAdapter;
    private List<TranslateCounselorEntiy> tcEntiys = new ArrayList<>();
    private Button btnChoose;
    private int page = 0;
    private String totalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_on_advisor);
        initviews();
        initDatas();
        initLinsent();
    }

    private void initDatas() {
        counselorListAdapter = new CounselorListAdapter(FocusOnAdvisorActivity.this, tcEntiys);
        counselorListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extra = new Bundle();
                extra.putString(CounselorActivity.COUNSELOR_ID, tcEntiys.get(position).getCounselorId());
                Util.openActivityWithBundle(FocusOnAdvisorActivity.this, CounselorActivity.class, extra);
            }
        });
        recyclerView.setAdapter(counselorListAdapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FocusOnAdvisorActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.enableLoadmore();
        counselorListAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.bottom_progressbar, null));
        recyclerView.setRefreshing(true);
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getCounselorList();
            }
        });
        getCounselorList();
    }

    private void initLinsent() {
        llBack.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
    }

    private void initviews() {
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        rlNoFollow = (RelativeLayout) findViewById(R.id.rl_nofollow);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        myorder_message_title = (TextView) findViewById(R.id.myorder_message_title);
        btnChoose = (Button) findViewById(R.id.btn_choose);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_choose:
                startActivity(new Intent(FocusOnAdvisorActivity.this, CounselorList.class));
                break;
        }
    }


    private void getCounselorList() {
        showLoadingDialog();
        ProfileManager.getInstance(this).ByParaentId(this, page);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeCounselorListByParaentId:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    totalSize = jsonObject.getString("totalSize");
                    String code = new JSONObject(result.get("header").toString()).getString("code");
                    String message = new JSONObject(result.get("header").toString()).getString("message");
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    tcEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<TranslateCounselorEntiy>>() {
                    }.getType());
                    if (code.equals("200")) {
                        if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                            if (tcEntiys.size() > 0) {
                                myorder_message_title.setText("我关注的顾问 (" + totalSize + ")");
                                counselorListAdapter.clear();
                                counselorListAdapter.addData(tcEntiys);
                                recyclerView.setVisibility(View.VISIBLE);
                                rlNoFollow.setVisibility(View.GONE);
                            } else {
                                myorder_message_title.setText("我关注的顾问");
                                rlNoFollow.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            if (tcEntiys.size() > 0) {
                                myorder_message_title.setText("我关注的顾问 (" + totalSize + ")");
                                counselorListAdapter.addData(tcEntiys);
                                recyclerView.setVisibility(View.VISIBLE);
                                rlNoFollow.setVisibility(View.GONE);
                            } else {
                                myorder_message_title.setText("我关注的顾问");
                                rlNoFollow.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                        if (tcEntiys.size() < ZWConfig.pageSize) {
                            recyclerView.disableLoadmore();
                        } else {
                            recyclerView.enableLoadmore();
                        }
                    } else if (code.equals("404")) {
                        UiUtil.show(FocusOnAdvisorActivity.this, message);
                    }
                    if (code.equals("400")) {
                        UiUtil.show(FocusOnAdvisorActivity.this, message);
                    }
                } catch (JSONException e) {
                    UiUtil.show(FocusOnAdvisorActivity.this, "服务器繁忙，请稍后再试！");
                }
                break;
        }
    }

    public void onEvent(FocusEvent event) {
        page = 0;
        counselorListAdapter.clear();
        getCounselorList();
    }
}
