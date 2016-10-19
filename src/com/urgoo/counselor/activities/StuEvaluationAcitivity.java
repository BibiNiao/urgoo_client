package com.urgoo.counselor.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.collect.adapter.FollowVideoAdapter;
import com.urgoo.counselor.adapter.StuEvaluationAdpater;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.CounselorDetail;
import com.urgoo.counselor.model.Evaluation;
import com.urgoo.net.EventCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/11.
 */
public class StuEvaluationAcitivity extends NavToolBarActivity {
    private UltimateRecyclerView recyclerView;
    private StuEvaluationAdpater adapter;
    private List<Evaluation> evaluationList = new ArrayList<>();
    private int currentPage = 0;
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";
    private String counselorId;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_evaluation, null);
        initViews(view);
        counselorId = getIntent().getStringExtra(EXTRA_COUNSELOR_ID);
        getStuEvaluation();
        return view;
    }

    private void initViews(View view) {
        setNavTitleText("学生评价");
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new StuEvaluationAdpater(this, evaluationList);
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
                getStuEvaluation();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                currentPage++;
                getStuEvaluation();
            }
        });
    }

    private void getStuEvaluation() {
        CounselorManager.getInstance(this).getStuEvaluation(this, counselorId, currentPage);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetStuEvaluation:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    List<Evaluation> evaluationList = gson.fromJson(jsonObject.getJSONArray("words").toString(), new TypeToken<List<Evaluation>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(evaluationList);
                    } else {
                        if (!evaluationList.isEmpty()) {
                            adapter.addData(evaluationList);
                        }
                    }
                    if (evaluationList.size() < 10) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
