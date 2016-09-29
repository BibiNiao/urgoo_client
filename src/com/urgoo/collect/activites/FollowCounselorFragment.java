package com.urgoo.collect.activites;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.counselor.activities.CounselorDetailActivity;
import com.urgoo.counselor.event.FocusEvent;
import com.urgoo.counselor.model.CounselorDetail;
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
    private int currentPage = 0;
    private List<CounselorEntiy> counselorEntiys = new ArrayList<>();

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

    public void onEventMainThread(FocusEvent event) {
        getCounselorList();
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
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
                extras.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, adapter.getItem(position).getCounselorId());
                extras.putString(CounselorDetailActivity.EXTRA_TITLE, adapter.getItem(position).getCnName());
                Util.openActivityWithBundle(getActivity(), CounselorDetailActivity.class, extras);
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
                        adapter.clear();
                        adapter.addData(counselorEntiys);
                    } else {
                        if (!counselorEntiys.isEmpty()) {
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
