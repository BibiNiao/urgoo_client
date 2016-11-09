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
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.live.activities.LiveDetailActivity;
import com.urgoo.live.activities.VideoDetailActivity;
import com.urgoo.message.adapter.UserMessageAdapter;
import com.urgoo.message.biz.MessageManager;
import com.urgoo.message.model.UserMessage;
import com.urgoo.net.EventCode;
import com.urgoo.profile.activities.MessageListActivity;
import com.urgoo.profile.activities.SystemInformationDetail;
import com.urgoo.profile.biz.ProfileManager;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/27.
 */
public class UserMessageActivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private TextView tvEmpty;
    private UserMessageAdapter adapter;
    private List<UserMessage> userMessageList = new ArrayList<>();
    private int page;
    private int count;
    private EMConversation conversation;
    private EMMessageListener msgListener = null;

    @Override
    protected View createContentView() {
        setSwipeBackEnable(false);
        setNavTitleText("个人消息");
        View view = inflaterViewWithLayoutID(R.layout.activity_sysmessage, null);
        initViews(view);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void initViews(View view) {
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                new Thread() {
                    public void run() {
                        //这儿是耗时操作，完成之后更新UI；
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                adapter.getItem(0).setUnread(2);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        conversation = EMClient.getInstance().chatManager().getConversation(ZWConfig.ACTION_CustomerService);
        if (conversation != null) {
            count = conversation.getUnreadMsgCount();
        }
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        adapter = new UserMessageAdapter(this, userMessageList);
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
                getMessageList();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getMessageList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle;
                if (position == 0) {
                    startActivity(new Intent(UserMessageActivity.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
                } else {
                    updateSysMessage(adapter.getItem(position).getInformationId(), adapter.getItem(position).getUnread());
                    switch (adapter.getItem(position).getType()) {
                        case 5:
                            bundle = new Bundle();
                            bundle.putString(LiveDetailActivity.EXTRA_LIVE_ID, String.valueOf(adapter.getItem(position).getTargetId()));
                            Util.openActivityWithBundle(UserMessageActivity.this, LiveDetailActivity.class, bundle);
                            break;
                        case 6:
                            bundle = new Bundle();
                            bundle.putString(VideoDetailActivity.EXTRA_VIDEO_ID, String.valueOf(adapter.getItem(position).getTargetId()));
                            Util.openActivityWithBundle(UserMessageActivity.this, VideoDetailActivity.class, bundle);
                            break;
                        default:
                            Bundle extras = new Bundle();
                            extras.putString("content", adapter.getItem(position).getContent());
                            Util.openActivityWithBundle(UserMessageActivity.this, SystemInformationDetail.class, extras);
                            break;
                    }
                }

                if (adapter.getItem(position).getUnread() == 2) {
                    adapter.getItem(position).setUnread(1);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        showLoadingDialog();
        getMessageList();
    }

    private void updateSysMessage(int informationId, int unread) {
        MessageManager.getInstance(this).selectInformationUserDetail(informationId, unread, this);
    }

    private void getMessageList() {
        ProfileManager.getInstance(this).getSelectInformationPerson("10", page, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeMessageList:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<UserMessage> userMessageList = gson.fromJson(jsonObject.getJSONArray("information").toString(), new TypeToken<List<UserMessage>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        if (userMessageList.size() > 0) {
                            if (count > 0) {
                                userMessageList.get(0).setUnread(2);
                            } else {
                                userMessageList.get(0).setUnread(1);
                            }
                            adapter.clear();
                            adapter.addData(userMessageList);
                            tvEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (!userMessageList.isEmpty()) {
                            if (count > 0) {
                                userMessageList.get(0).setUnread(2);
                            } else {
                                userMessageList.get(0).setUnread(1);
                            }
                            adapter.addData(userMessageList);
                        }
                    }
                    if (userMessageList.size() < 10) {
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
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
            Bundle extras = new Bundle();
            extras.putInt(MainActivity.EXTRA_TAB, 2);
            Util.openActivityWithBundle(UserMessageActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
