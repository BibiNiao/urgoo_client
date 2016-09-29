package com.urgoo.account.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.account.adapter.MyActivitesAdapter;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.live.model.Live;
import com.urgoo.net.EventCode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesActivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private MyActivitesAdapter adapter;
    private int currentPage = 0;
    private List<Live> liveList = new ArrayList<>();

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_activities, null);
        initViews(view);
        getMyActivites();
        return view;
    }

    private void getMyActivites() {
        AccountManager.getInstance(this).getMyAcitivtes(currentPage ,this);
    }

    private void initViews(View view) {
        setNavTitleText("我的活动");
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new MyActivitesAdapter(this, liveList);

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
                currentPage = 0;
                getMyActivites();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                getMyActivites();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putInt("id", adapter.getItem(position).getLiveId());
//                Util.openActivityWithBundle(getActivity(), ArticleDetailActivity.class, extras);
            }
        });
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetMyAcitivites:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<Live> liveList = gson.fromJson(jsonObject.getJSONArray("liveList").toString(), new TypeToken<List<Live>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(liveList);
                    } else {
                        if (!liveList.isEmpty()) {
                            adapter.addData(liveList);
                        }
                    }
                    if (liveList.size() < 10) {
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
