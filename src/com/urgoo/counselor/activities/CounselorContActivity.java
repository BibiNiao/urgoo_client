package com.urgoo.counselor.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.GuwenInfo;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CounselorContActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private String postion;
    private String counselorId;
    private ArrayList<GuwenInfo.DetailSubListBean> mDetailSubList = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guwencont);
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("postion") != null) {
            postion = mIntent.getStringExtra("postion");
            counselorId = mIntent.getStringExtra("counselorId");
        }
        getCounselorDetailSubList();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    inidDatas();
                }
            }
        };
    }

    private void inidDatas() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < mDetailSubList.size(); i++) {
            if (mDetailSubList.get(i).getType().equals("1")) {              //TODO 如果是vidoe，就把vidoe的url传进去
                CounselorVidoeFragment mFragment = new CounselorVidoeFragment();
                mFragments.add(mFragment);
                mFragment.setUrl(mDetailSubList.get(i).getUrl());           //TODO 传入imgURL
                mFragment.setVideoPic(mDetailSubList.get(i).getVideoPic()); //TODO 传入VideoURL
            } else if (mDetailSubList.get(i).getType().equals("0")) {      //TODO 如果是img，就把img的url传进去
                CounselorimgFragment mFragment = new CounselorimgFragment();
                mFragments.add(mFragment);
                mFragment.setUrl(mDetailSubList.get(i).getUrl());            //TODO 传入imgURL
            }
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        setCurView(Integer.parseInt(postion));
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

    }

    //TODO 设置当前的图片
    private void setCurView(int position) {
        if (position < 0 || position >= mFragments.size()) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    private void getCounselorDetailSubList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("counselorId", counselorId);

        OkHttpClientManager.postAsyn(ZWConfig.URL_selectCounselorDetailSubList,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                JSONObject jsondeta = new JSONObject(response).getJSONObject("body");
                                JSONArray jsontime = jsondeta.getJSONArray("counselorDetailSubList");
                                ArrayList<GuwenInfo.DetailSubListBean> mSlist = (new Gson()).fromJson(jsontime.toString(),
                                        new TypeToken<ArrayList<GuwenInfo.DetailSubListBean>>() {
                                        }.getType());
                                mDetailSubList.addAll(mSlist);
                                Message msg = new Message();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                            } else if (code.equals("404")) {
                                UiUtil.show(CounselorContActivity.this, message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(CounselorContActivity.this, message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(CounselorContActivity.this, "服务器繁忙，请稍后再试！");
                        }
                    }
                }, params
        );
    }

}
