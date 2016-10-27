package com.urgoo.counselor.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.collect.adapter.FollowConsultantAdapter;
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.SearchData;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/25.
 */
public class SearchResultAcivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private FollowConsultantAdapter adapter;
    private int currentPage = 0;
    private List<CounselorEntiy> counselorEntiys = new ArrayList<>();
    private SearchData searchData;
    private String name;
    private String path;
    private TextView tvEmpty;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_search_result, null);
        path = getIntent().getStringExtra("path");
        if (path.equals("name")) {
            name = getIntent().getStringExtra("name");
        } else {
            searchData = getIntent().getParcelableExtra("search");
        }
        setNavTitleText("搜索结果");
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        adapter = new FollowConsultantAdapter(this, counselorEntiys);

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
                if (path.equals("name")) {
                    getTranslateCounselor(currentPage, name, "", "", "", "", "", "", "", "");
                } else {
                    getTranslateCounselor(currentPage, "", searchData.getCountryType(), searchData.getGender(),
                            searchData.getServiceType(), searchData.getChineseLevelType(), searchData.getCounselorExperanceType(),
                            searchData.getOrganizationType(), searchData.getPositionType(), searchData.getSurpriseType());
                }
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                currentPage++;
                if (path.equals("name")) {
                    getTranslateCounselor(currentPage, name, "", "", "", "", "", "", "", "");
                } else {
                    getTranslateCounselor(currentPage, "", searchData.getCountryType(), searchData.getGender(),
                            searchData.getServiceType(), searchData.getChineseLevelType(), searchData.getCounselorExperanceType(),
                            searchData.getOrganizationType(), searchData.getPositionType(), searchData.getSurpriseType());
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(CounselorMainActivity.EXTRA_COUNSELOR_ID, adapter.getItem(position).getCounselorId());
                Util.openActivityWithBundle(SearchResultAcivity.this, CounselorMainActivity.class, extras);
            }
        });

        showLoadingDialog();
        if (path.equals("name")) {
            getTranslateCounselor(currentPage, name, "", "", "", "", "", "", "", "");
        } else {
            getTranslateCounselor(currentPage, "", searchData.getCountryType(), searchData.getGender(),
                    searchData.getServiceType(), searchData.getChineseLevelType(), searchData.getCounselorExperanceType(),
                    searchData.getOrganizationType(), searchData.getPositionType(), searchData.getSurpriseType());
        }
    }

    private void getTranslateCounselor(int page, String name, String countryType, String gender, String serviceType,
                                       String chineseLevelType, String counselorExperanceType, String organizationType, String positionType, String surpriseType) {
        CounselorManager.getInstance(this).getCounselorList(this, page, name, countryType, gender, serviceType,
                chineseLevelType, counselorExperanceType, organizationType, positionType, surpriseType);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetCounselorList:
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    List<CounselorEntiy> counselorEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<CounselorEntiy>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        if (counselorEntiys.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        } else {
                            adapter.clear();
                            adapter.addData(counselorEntiys);
                            recyclerView.setVisibility(View.VISIBLE);
                            tvEmpty.setVisibility(View.GONE);
                        }
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
