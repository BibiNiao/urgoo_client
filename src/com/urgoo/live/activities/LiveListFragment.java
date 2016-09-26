package com.urgoo.live.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.urgoo.live.adapter.LiveListAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.model.Live;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/9/20.
 */
public class LiveListFragment extends BaseFragment implements StringRequestCallBack {
    private UltimateRecyclerView recyclerView;
    private LiveListAdapter adapter;
    private int currentPage = 0;
    private List<Live> liveList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_live_list, container, false);
        initViews();
        getLiveList();
        return viewContent;
    }

    private void getLiveList() {
        LiveManager.getInstance(getActivity()).getLiveList(currentPage, this);
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        adapter = new LiveListAdapter(getActivity(), liveList);

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
                getLiveList();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                getLiveList();
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
            case EventCodeGetLiveList:
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
