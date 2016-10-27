package com.urgoo.plan.activities;

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
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.plan.adapter.WorkListAdapter;
import com.urgoo.plan.biz.PlanManager;
import com.urgoo.plan.model.Plan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/21.
 */
public class WorkTemplateFragment extends BaseFragment {
    private UltimateRecyclerView recyclerView;
    private WorkListAdapter adapter;
    private List<Plan> planList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_work, container, false);
        initViews();
        getPlan();
        return viewContent;
    }

    private void getPlan() {
        PlanManager.getInstance(getActivity()).getPlan(this);
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        adapter = new WorkListAdapter(getActivity(), planList);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlan();
            }
        });
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetTaskList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    planList = gson.fromJson(jsonObject.getString("listTask"), new TypeToken<List<Plan>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(planList);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }
}
