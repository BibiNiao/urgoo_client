package com.urgoo.message.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.event.MessageEvent;
import com.urgoo.live.event.FollowVideoEvent;
import com.urgoo.message.adapter.MessageAdapter;
import com.urgoo.message.biz.MessageManager;
import com.urgoo.message.event.SysMessageListEvent;
import com.urgoo.message.model.MessageItem;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;
import com.zw.express.tool.image.ImageWorker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/10/26.
 */
public class SysMessageActivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageItem> messageItemList = new ArrayList<>();

    @Override
    protected View createContentView() {
        setNavTitleText("系统消息");
        EventBus.getDefault().register(this);
        View view = inflaterViewWithLayoutID(R.layout.activity_sysmessage, null);
        initViews(view);
        return view;
    }

    public void onEventMainThread(SysMessageListEvent event) {
        getSysMessageList();
    }

    private void getSysMessageList() {
        MessageManager.getInstance(this).getSysMessageList(this);
    }

    private void initViews(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new MessageAdapter(this, messageItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSysMessageList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(SysMessageDetailActivity.EXTRA_FLAG, adapter.getItem(position).getFlag());
                bundle.putString(SysMessageDetailActivity.EXTRA_TITLE, adapter.getItem(position).getTypeName());
                Util.openActivityWithBundle(SysMessageActivity.this, SysMessageDetailActivity.class, bundle);
            }
        });
        getSysMessageList();
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeSysMessageList:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    messageItemList = gson.fromJson(jsonObject.getJSONArray("information").toString(), new TypeToken<List<MessageItem>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(messageItemList);
                    }
                } catch (Exception e) {
                    showToastSafe("解析数据信息时出错，请稍后再试~");
                }
                break;
        }
    }
}
