package com.urgoo.message.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorMainActivity;
import com.urgoo.live.activities.LiveDetailActivity;
import com.urgoo.live.activities.VideoDetailActivity;
import com.urgoo.message.adapter.SysMessageAdapter;
import com.urgoo.message.biz.MessageManager;
import com.urgoo.message.event.SysMessageListEvent;
import com.urgoo.message.model.SysMessage;
import com.urgoo.net.EventCode;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/10/26.
 */
public class SysMessageDetailActivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private TextView tvEmpty;
    private int flag;
    public static final String EXTRA_FLAG = "flag";
    public static final String EXTRA_TITLE = "title";
    private List<SysMessage> sysMessageList = new ArrayList<>();
    private SysMessageAdapter adapter;
    private int page;

    @Override
    protected View createContentView() {
        flag = getIntent().getIntExtra(EXTRA_FLAG, 0);
        setNavTitleText(getIntent().getStringExtra(EXTRA_TITLE));
        setSwipeBackEnable(false);
        View view = inflaterViewWithLayoutID(R.layout.activity_sysmessage, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        adapter = new SysMessageAdapter(this, sysMessageList, flag);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.enableLoadmore();
        adapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.bottom_progressbar, null));

        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getSysMessageDetail();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getSysMessageDetail();
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle;
                switch (flag) {
                    case 1:
                        bundle = new Bundle();
                        bundle.putString(LiveDetailActivity.EXTRA_LIVE_ID, adapter.getItem(position).getTargetId());
                        bundle.putBoolean(LiveDetailActivity.EXTRA_FROM, false);
                        Util.openActivityWithBundle(SysMessageDetailActivity.this, LiveDetailActivity.class, bundle);
                        break;
                    case 2:
                        bundle = new Bundle();
                        bundle.putString(CounselorMainActivity.EXTRA_COUNSELOR_ID, adapter.getItem(position).getTargetId());
                        Util.openActivityWithBundle(SysMessageDetailActivity.this, CounselorMainActivity.class, bundle);
                        break;
                    case 4:
                        bundle = new Bundle();
                        bundle.putString(VideoDetailActivity.EXTRA_VIDEO_ID, adapter.getItem(position).getTargetId());
                        Util.openActivityWithBundle(SysMessageDetailActivity.this, VideoDetailActivity.class, bundle);
                        break;
                    case 5:
                        String strURL = ZWConfig.URL_requestUrgooActivity + "?targetId=" + adapter.getItem(position).getTargetId();
                        startActivity(new Intent(SysMessageDetailActivity.this, BaseWebViewActivity.class).putExtra(BaseWebViewActivity.EXTRA_URL, strURL));
                        break;
                }
                updateSysMessage(adapter.getItem(position).getInformationId(), adapter.getItem(position).getUnread());
                if (adapter.getItem(position).getUnread().equals("2")) {
                    adapter.getItem(position).setUnread("1");
                    adapter.notifyDataSetChanged();
                }
            }
        });
        showLoadingDialog();
        getSysMessageDetail();
    }

    private void getSysMessageDetail() {
        MessageManager.getInstance(this).getSysMessageDetail(page, String.valueOf(flag), this);
    }

    private void updateSysMessage(String informationId, String unread) {
        MessageManager.getInstance(this).selectInformationSystemDetail(informationId, unread, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeSysMessageDetailList:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    sysMessageList = gson.fromJson(jsonObject.getJSONArray("information").toString(), new TypeToken<List<SysMessage>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        if (sysMessageList.size() > 0) {
                            adapter.clear();
                            adapter.addData(sysMessageList);
                            tvEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (!sysMessageList.isEmpty()) {
                            adapter.addData(sysMessageList);
                        }
                    }
                    if (sysMessageList.size() < 10) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                } catch (Exception e) {
                    showToastSafe("解析数据信息时出错，请稍后再试~");
                }
                break;
        }
    }


    @Override
    protected void onNavLeftClick(View v) {
        EventBus.getDefault().post(new SysMessageListEvent());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new SysMessageListEvent());
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
