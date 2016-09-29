package com.urgoo.profile.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.profile.adapter.MessageListAdapter;
import com.urgoo.profile.biz.ProfileManager;
import com.urgoo.profile.model.InformationEntity;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by bb on 2016/9/7.
 */
public class MessageListActivity extends NavToolBarActivity {
    private int page;
    private int type;
    private UltimateRecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    public final static String MESSAGE_TYPE = "message_type";
    public static final int REQUEST_CODE_MESSAGE = 1;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_message_list, null);
        type = getIntent().getIntExtra(MessageActivity.MESSAGE_TYPE, 0);
        initViews(view);
        if (type == 1) {
            setNavTitleText("系统消息");
            getMessageList("", 0);
        } else {
            setNavTitleText("个人消息");
            getMessageList("0", 0);
        }
        return view;
    }

    public void initViews(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        messageListAdapter = new MessageListAdapter(this, type);

        recyclerView.setAdapter(messageListAdapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        recyclerView.enableLoadmore();
        messageListAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.bottom_progressbar, null));
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                if (type == 1) {
                    getMessageList("", page);
                } else {
                    getMessageList("0", page);
                }
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                if (messageListAdapter.getAdapterItemCount() != 0) {
                    if (type == 1) {
                        getMessageList("", page);
                    } else {
                        getMessageList("0", page);
                    }
                }
            }
        });
        messageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                v.findViewById(R.id.iv_red).setVisibility(View.GONE);
                Bundle extras = new Bundle();
                extras.putString("informationId", String.valueOf(messageListAdapter.getItem(position).getInformationId()));
                extras.putString("unread", String.valueOf(messageListAdapter.getItem(position).getUnread()));
                extras.putString("content", messageListAdapter.getItem(position).getContent());
                extras.putInt("type", type);
                Util.openActivityWithBundle(MessageListActivity.this, SystemInformationDetail.class, extras);
            }
        });
    }

    private void getMessageList(String type, int page) {
        ProfileManager.getInstance(this).getSelectInformationPerson(type, page, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeMessageList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    List<InformationEntity> informationEntities = gson.fromJson(jsonObject.getJSONArray("information").toString(), new TypeToken<List<InformationEntity>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        messageListAdapter.clear();
                        if (!informationEntities.isEmpty()) {
                            messageListAdapter.addData(informationEntities);
                        }
                    } else {
                        messageListAdapter.addData(informationEntities);
                    }

                    if (informationEntities.size() < 10) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onNavLeftClick(View v) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
