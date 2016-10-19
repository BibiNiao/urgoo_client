package com.urgoo.live.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.urgoo.live.adapter.AlbumAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.model.Album;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/9/20.
 */
public class AlbumFragment extends BaseFragment implements StringRequestCallBack {
    private UltimateRecyclerView recyclerView;
    private AlbumAdapter adapter;
    private List<Album> albumList = new ArrayList<>();

    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_album_list, container, false);
        initViews();
        getAlbumList();
        return viewContent;
    }

    private void getAlbumList() {
        LiveManager.getInstance(getActivity()).getAlbumList(currentPage, this);
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        adapter = new AlbumAdapter(getActivity(), albumList);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.enableLoadmore();
        adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.bottom_progressbar, null));

        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                getAlbumList();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                getAlbumList();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putInt(AlbumActivity.EXTRA_ALBUM_ID, adapter.getItem(position).getAlbumTypeId());
                Util.openActivityWithBundle(getActivity(), AlbumActivity.class, extras);
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
            case EventCodeGetAlbumList:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<Album> albumList = gson.fromJson(jsonObject.getJSONArray("albumList").toString(), new TypeToken<List<Album>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(albumList);
                    } else {
                        if (!albumList.isEmpty()) {
                            adapter.addData(albumList);
                        }
                    }
                    if (albumList.size() < 10) {
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
