package com.urgoo.collect.activites;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.collect.adapter.FollowVideoAdapter;
import com.urgoo.collect.model.Video;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/9/23.
 */
public class FollowVideoFragment extends BaseFragment implements StringRequestCallBack {
    private UltimateRecyclerView recyclerView;
    private int currentPage = 0;
    private FollowVideoAdapter adapter;
    private List<Video> videoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_follow_video_list, container, false);
        initViews();
        return viewContent;
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        adapter = new FollowVideoAdapter(getActivity(), videoList);

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
//                getLiveList();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
//                getLiveList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
//                extras.putInt("id", adapter.getItem(position).getLiveId());
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
        super.onResponseSuccess(eventCode, result);
    }
}
