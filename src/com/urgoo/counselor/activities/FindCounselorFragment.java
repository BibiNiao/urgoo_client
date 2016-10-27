package com.urgoo.counselor.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.collect.event.FollowCounselorEvent;
import com.urgoo.counselor.adapter.ViewPaperAdapter;
import com.urgoo.counselor.model.Counselor;
import com.urgoo.main.biz.MainManager;
import com.urgoo.net.EventCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/10/8.
 */
public class FindCounselorFragment extends BaseFragment{
    private ViewPager viewPager;
    private List<Counselor> counselorList = new ArrayList<>();
    private ViewPaperAdapter viewPaperAdapter;
    private int currentPage = 0;
    private CounselorMoreInterface counselorMoreInterface;
    private boolean isLoad = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_find_counselor, container, false);
        initViews();
        EventBus.getDefault().register(this);
        getMyCounselorListTop();
        setHasOptionsMenu(true);
        return viewContent;
    }

    public void getMyCounselorListTop() {
        MainManager.getInstance(getActivity()).getMyCounselorListTop(this, currentPage);
    }

    private void initViews() {
        viewPager = (ViewPager) viewContent.findViewById(R.id.viewPager);
        counselorMoreInterface = new CounselorMoreInterface() {
            @Override
            public void loadMore() {
                currentPage ++;
                getMyCounselorListTop();
            }
        };
        viewPaperAdapter = new ViewPaperAdapter(getActivity(), counselorList, counselorMoreInterface, currentPage, isLoad);
        viewPager.setAdapter(viewPaperAdapter);
    }

    public void onEventMainThread(FollowCounselorEvent event) {
        for (int i = 0; i < counselorList.size(); i++) {
            if (counselorList.get(i).getCounselorId().equals(event.getCounselorId())) {
                counselorList.get(i).setIsAttention(event.getIsAttention());
                setCollectStatus((ImageView) viewPager.findViewWithTag(i).findViewById(R.id.iv_collect), event.getIsAttention());
            }
        }
    }

    /**
     * 设置按钮状态
     *
     * @param v
     * @param isAttention
     */
    private void setCollectStatus(ImageView v, String isAttention) {
        if (isAttention.equals("1")) {
            v.setImageResource(R.drawable.ic_find_collected);
        } else {
            v.setImageResource(R.drawable.ic_find_collect);
        }
        v.setEnabled(true);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetMyCounselorListTop:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    List<Counselor> newData = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<Counselor>>() {
                    }.getType());
                    if (newData.size() > 0) {
                        if (newData.size() > 9) {
                            isLoad = true;
                        }
                        viewPaperAdapter.addData(newData, isLoad, currentPage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
