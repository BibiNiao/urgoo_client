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
import com.urgoo.collect.adapter.FollowVideoAdapter;
import com.urgoo.collect.biz.CollectManager;
import com.urgoo.collect.event.FollowEvent;
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.collect.model.Video;
import com.urgoo.live.activities.AlbumActivity;
import com.urgoo.live.activities.LiveDetailActivity;
import com.urgoo.live.activities.VideoDetailActivity;
import com.urgoo.live.model.AlbumDetail;
import com.urgoo.live.model.LiveDetail;
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
public class FollowVideoFragment extends BaseFragment implements StringRequestCallBack {
    private UltimateRecyclerView recyclerView;
    private int currentPage = 0;
    private FollowVideoAdapter adapter;
    private List<Video> videoList = new ArrayList<>();
    private boolean isOther = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_follow_video_list, container, false);
        initViews();
        EventBus.getDefault().register(this);
        getVideoList();
        return viewContent;
    }

    public void onEventMainThread(FollowEvent event) {
        isOther = true;
        currentPage = 0;
        getVideoList();
    }

    private void getVideoList() {
        CollectManager.getInstance(getActivity()).followVideos(this, currentPage);
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
                getVideoList();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                getVideoList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                switch (adapter.getItem(position).getType()) {
                    case "4":
                        extras.putInt(AlbumActivity.EXTRA_ALBUM_ID, Integer.parseInt(adapter.getItem(position).getTargetId()));
                        Util.openActivityWithBundle(getActivity(), AlbumActivity.class, extras);
                        break;
                    case "5":
                        extras.putString(VideoDetailActivity.EXTRA_VIDEO_ID, adapter.getItem(position).getTargetId());
                        Util.openActivityWithBundle(getActivity(), VideoDetailActivity.class, extras);
                        break;
                    case "6":
                        extras.putString(LiveDetailActivity.EXTRA_LIVE_ID, adapter.getItem(position).getTargetId());
                        Util.openActivityWithBundle(getActivity(), LiveDetailActivity.class, extras);
                        break;
                }
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
            case EventCodeFollowVideos:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<Video> videos = gson.fromJson(jsonObject.getJSONArray("myVideo").toString(), new TypeToken<List<Video>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(videos);
                    } else {
                        if (!videos.isEmpty()) {
                            if (isOther) {
                                adapter.clear();
                                isOther = false;
                            }
                            adapter.addData(videos);
                        }
                    }
                    if (videos.size() < 10) {
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
