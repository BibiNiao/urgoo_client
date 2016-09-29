package com.urgoo.service.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.HomeFragment;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.service.biz.ServerManager;
import com.urgoo.service.model.PlanEntity;
import com.urgoo.service.model.PlanTypeEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dff on 2016/8/2.
 */
public class PlanFragment extends HomeFragment implements View.OnClickListener {

    private TextView tv_yuyan;
    private TextView tv_xueshu;
    private TextView tv_bipan;
    private TextView tv_lingdao;
    private TextView tv_ziwo;
    private TextView tv_chuangzhao;
    private android.support.v7.widget.CardView cv_yuyan;
    private android.support.v7.widget.CardView cv_xueshu;
    private android.support.v7.widget.CardView cv_bipan;
    private android.support.v7.widget.CardView cv_lingdao;
    private android.support.v7.widget.CardView cv_ziwo;
    private android.support.v7.widget.CardView cv_chuangzhao;
    private TextView tv_plan_time;
    private TextView tv_report;
    private LinearLayout ll_lv;
    private TextView tv_cont_lv;
    private TextView tv_bythe_lv;
    private TextView tv_plan_remain_lv;
    private LinearLayout ll_lan;
    private TextView tv_cont_lan;
    private TextView tv_bythe_lan;
    private TextView tv_plan_remain_lan;
    private LinearLayout ll_zi;
    private TextView tv_cont_zi;
    private TextView tv_bythe_zi;
    private TextView tv_plan_remain_zi;
    private LinearLayout ll_yellow;
    private TextView tv_cont_yellow;
    private TextView tv_bythe_yellow;
    private TextView tv_plan_remain_yellow;
    private LinearLayout ll_fen;
    private TextView tv_cont_fen;
    private TextView tv_bythe_fen;
    private TextView tv_plan_remain_fen;
    private LinearLayout ll_red;
    private TextView tv_cont_red;
    private TextView tv_bythe_red;
    private TextView tv_plan_remain_red;
    private ArrayList<PlanEntity> mEntities = new ArrayList<>();
    private ArrayList<PlanTypeEntity> listType = new ArrayList<>();

    protected Toolbar mToolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.plan_layout, null);
        ServerManager.getInstance(getActivity()).getPlan(this);
        initView();
        initLiener();
        return view;
    }

    private void initLiener() {
        cv_yuyan.setOnClickListener(this);
        cv_xueshu.setOnClickListener(this);
        cv_bipan.setOnClickListener(this);
        cv_lingdao.setOnClickListener(this);
        cv_ziwo.setOnClickListener(this);
        cv_chuangzhao.setOnClickListener(this);
        ll_lv.setOnClickListener(this);
        ll_lan.setOnClickListener(this);
        ll_zi.setOnClickListener(this);
        ll_yellow.setOnClickListener(this);
        ll_fen.setOnClickListener(this);
        ll_red.setOnClickListener(this);
        tv_report.setOnClickListener(this);
    }

    protected void initView() {
        cv_yuyan = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_yuyan);
        cv_xueshu = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_xueshu);
        cv_bipan = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_bipan);
        cv_lingdao = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_lingdao);
        cv_ziwo = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_ziwo);
        cv_chuangzhao = (android.support.v7.widget.CardView) view.findViewById(R.id.cv_chuangzhao);
        tv_yuyan = (TextView) view.findViewById(R.id.tv_yuyan);
        tv_xueshu = (TextView) view.findViewById(R.id.tv_xueshu);
        tv_bipan = (TextView) view.findViewById(R.id.tv_bipan);
        tv_lingdao = (TextView) view.findViewById(R.id.tv_lingdao);
        tv_ziwo = (TextView) view.findViewById(R.id.tv_ziwo);
        tv_chuangzhao = (TextView) view.findViewById(R.id.tv_chuangzhao);

        tv_plan_time = (TextView) view.findViewById(R.id.tv_plan_time);
        tv_report = (TextView) view.findViewById(R.id.tv_report);

        ll_lv = (LinearLayout) view.findViewById(R.id.ll_lv);
        tv_cont_lv = (TextView) view.findViewById(R.id.tv_cont_lv);
        tv_bythe_lv = (TextView) view.findViewById(R.id.tv_bythe_lv);
        tv_plan_remain_lv = (TextView) view.findViewById(R.id.tv_plan_remain_lv);
        ll_lan = (LinearLayout) view.findViewById(R.id.ll_lan);
        tv_cont_lan = (TextView) view.findViewById(R.id.tv_cont_lan);
        tv_bythe_lan = (TextView) view.findViewById(R.id.tv_bythe_lan);
        tv_plan_remain_lan = (TextView) view.findViewById(R.id.tv_plan_remain_lan);
        ll_zi = (LinearLayout) view.findViewById(R.id.ll_zi);
        tv_cont_zi = (TextView) view.findViewById(R.id.tv_cont_zi);
        tv_bythe_zi = (TextView) view.findViewById(R.id.tv_bythe_zi);
        tv_plan_remain_zi = (TextView) view.findViewById(R.id.tv_plan_remain_zi);
        ll_yellow = (LinearLayout) view.findViewById(R.id.ll_yellow);
        tv_cont_yellow = (TextView) view.findViewById(R.id.tv_cont_yellow);
        tv_bythe_yellow = (TextView) view.findViewById(R.id.tv_bythe_yellow);
        tv_plan_remain_yellow = (TextView) view.findViewById(R.id.tv_plan_remain_yellow);
        ll_fen = (LinearLayout) view.findViewById(R.id.ll_fen);
        tv_cont_fen = (TextView) view.findViewById(R.id.tv_cont_fen);
        tv_bythe_fen = (TextView) view.findViewById(R.id.tv_bythe_fen);
        tv_plan_remain_fen = (TextView) view.findViewById(R.id.tv_plan_remain_fen);
        ll_red = (LinearLayout) view.findViewById(R.id.ll_red);
        tv_cont_red = (TextView) view.findViewById(R.id.tv_cont_red);
        tv_bythe_red = (TextView) view.findViewById(R.id.tv_bythe_red);
        tv_plan_remain_red = (TextView) view.findViewById(R.id.tv_plan_remain_red);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);
        mToolbar.setTitle("规划模板");
    }

    @Override
    public void onClick(View v) {
        if (mEntities.size() > 0) {
            switch (v.getId()) {
                case R.id.cv_yuyan:
                    startPlanContent(listType.get(0).getType());
                    break;
                case R.id.cv_xueshu:
                    startPlanContent(listType.get(1).getType());
                    break;
                case R.id.cv_bipan:
                    startPlanContent(listType.get(2).getType());
                    break;
                case R.id.cv_lingdao:
                    startPlanContent(listType.get(3).getType());
                    break;
                case R.id.cv_ziwo:
                    startPlanContent(listType.get(4).getType());
                    break;
                case R.id.cv_chuangzhao:
                    startPlanContent(listType.get(5).getType());
                    break;
                case R.id.ll_lv:
                    startContent(mEntities.get(0).getTaskId());
                    break;
                case R.id.ll_lan:
                    startContent(mEntities.get(1).getTaskId());
                    break;
                case R.id.ll_zi:
                    startContent(mEntities.get(2).getTaskId());
                    break;
                case R.id.ll_yellow:
                    startContent(mEntities.get(3).getTaskId());
                    break;
                case R.id.ll_fen:
                    startContent(mEntities.get(4).getTaskId());
                    break;
                case R.id.ll_red:
                    startContent(mEntities.get(5).getTaskId());
                    break;
                case R.id.tv_report:
                    startActivity(new Intent(getActivity(), ReportActivity.class));
                    break;
            }
        }
    }

    private void startContent(String string) {
        startActivity(new Intent(getActivity(), TaskContentActivity.class).putExtra("taskId", string));
    }

    private void startPlanContent(String string) {
        startActivity(new Intent(getActivity(), PlanContentActivity.class).putExtra("type", string));
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case newTaskList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mEntities = gson.fromJson(jsonObject.getString("listTask"), new TypeToken<ArrayList<PlanEntity>>() {
                    }.getType());
                    listType = gson.fromJson(jsonObject.getString("listType"), new TypeToken<ArrayList<PlanTypeEntity>>() {
                    }.getType());

                    String nextTime = jsonObject.getString("nextTime");
                    tv_plan_time.setText("距离下一次的视频见面还有" + nextTime + "天");
                    Log.d("uuu", "mEntities: " + mEntities.toString());
                    Log.d("uuu", "listType: " + listType.toString());
                    initDatas();
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }

    private void initDatas() {
        tv_cont_lv.setText(mEntities.get(0).getTitle());
        tv_bythe_lv.setText("截止时间 " + mEntities.get(0).getDeadLine());
        tv_plan_remain_lv.setText("剩余 " + mEntities.get(0).getDeadDay() + " 天");
        tv_cont_lan.setText(mEntities.get(1).getTitle());
        tv_bythe_lan.setText("截止时间 " + mEntities.get(1).getDeadLine());
        tv_plan_remain_lan.setText("剩余 " + mEntities.get(1).getDeadDay() + " 天");
        tv_cont_zi.setText(mEntities.get(2).getTitle());
        tv_bythe_zi.setText("截止时间 " + mEntities.get(2).getDeadLine());
        tv_plan_remain_zi.setText("剩余 " + mEntities.get(2).getDeadDay() + " 天");
        tv_cont_yellow.setText(mEntities.get(3).getTitle());
        tv_bythe_yellow.setText("截止时间 " + mEntities.get(3).getDeadLine());
        tv_plan_remain_yellow.setText("剩余 " + mEntities.get(3).getDeadDay() + " 天");
        tv_cont_fen.setText(mEntities.get(4).getTitle());
        tv_bythe_fen.setText("截止时间 " + mEntities.get(4).getDeadLine());
        tv_plan_remain_fen.setText("剩余 " + mEntities.get(4).getDeadDay() + " 天");
        tv_cont_red.setText(mEntities.get(5).getTitle());
        tv_bythe_red.setText("截止时间 " + mEntities.get(5).getDeadLine());
        tv_plan_remain_red.setText("剩余 " + mEntities.get(5).getDeadDay() + " 天");

        tv_yuyan.setText("已完成" + listType.get(0).getDone() + "项");
        tv_xueshu.setText("已完成" + listType.get(1).getDone() + "项");
        tv_bipan.setText("已完成" + listType.get(2).getDone() + "项");
        tv_lingdao.setText("已完成" + listType.get(3).getDone() + "项");
        tv_ziwo.setText("已完成" + listType.get(4).getDone() + "项");
        tv_chuangzhao.setText("已完成" + listType.get(5).getDone() + "项");
    }
}
