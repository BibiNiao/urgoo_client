package com.urgoo.collect.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.collect.adapter.FollowConsultantAdapter;
import com.urgoo.collect.biz.CollectManager;
import com.urgoo.collect.event.FollowCounselorEvent;
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.counselor.activities.CounselorMainActivity;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.UserMessageActivity;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/23.
 */
public class FollowCounselorFragment extends BaseFragment implements StringRequestCallBack {
    private UltimateRecyclerView recyclerView;
    private FollowConsultantAdapter adapter;
    private TextView tvEmpty;
    private TextView tvFollow;
    private int currentPage = 0;
    private List<CounselorEntiy> counselorEntiys = new ArrayList<>();
    private boolean isOther = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_follow_consultant_list, container, false);
        initViews();
        EventBus.getDefault().register(this);
        getCounselorList();
        return viewContent;
    }

    private void getCounselorList() {
        CollectManager.getInstance(getActivity()).followConsultants(this, currentPage);
    }

    public void onEventMainThread(FollowCounselorEvent event) {
        isOther = true;
        currentPage = 0;
        getCounselorList();
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        tvEmpty = (TextView) viewContent.findViewById(R.id.tv_empty);
        tvFollow = (TextView) viewContent.findViewById(R.id.tv_follow);
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putInt(MainActivity.EXTRA_TAB, 0);
                Util.openActivityWithBundle(getActivity(), MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
        });
        adapter = new FollowConsultantAdapter(getActivity(), counselorEntiys);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.enableLoadmore();
        adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.bottom_progressbar, null));

        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                getCounselorList();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                getCounselorList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(CounselorMainActivity.EXTRA_COUNSELOR_ID, adapter.getItem(position).getCounselorId());
                Util.openActivityWithBundle(getActivity(), CounselorMainActivity.class, extras);
            }
        });
    }

    public void setScrollTop() {
        if (recyclerView != null) {
            recyclerView.scrollVerticallyToPosition(0);
        }
    }

    public void setRefreshEnabled(boolean enabled) {
        if (recyclerView != null) {
            recyclerView.enableDefaultSwipeRefresh(enabled);
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeFollowConsultants:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<CounselorEntiy> counselorEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<CounselorEntiy>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        if (counselorEntiys.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvFollow.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            adapter.clear();
                            adapter.addData(counselorEntiys);
                            tvEmpty.setVisibility(View.GONE);
                            tvFollow.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (!counselorEntiys.isEmpty()) {
                            if (isOther) {
                                adapter.clear();
                                isOther = false;
                            }
                            adapter.addData(counselorEntiys);
                        }
                    }
                    if (counselorEntiys.size() < 10) {
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
}
