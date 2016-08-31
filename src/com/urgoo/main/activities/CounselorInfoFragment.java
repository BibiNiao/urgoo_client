package com.urgoo.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.urgoo.adapter.CounselorAdapter;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.CounselorInfo;
import com.urgoo.view.XListView;
import com.zw.express.tool.image.ImageLoaderUtil;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by lijie on 2016/5/27.
 */
public class CounselorInfoFragment extends Fragment {
    private View rootView;
    private ArrayList<CounselorInfo> mCounselorInfos = new ArrayList<CounselorInfo>();
    private XListView mListView;
    private int index = 0;
    private SearchFilter searchFilter;
    private CounselorAdapter counselorAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.counselor, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (XListView) rootView.findViewById(R.id.listView);
        getCounselorInfo("0", "", "", "", "");
        searchFilter = new SearchFilter();
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        counselorAdapter = new CounselorAdapter(getActivity(), mCounselorInfos);
        mListView.setAdapter(counselorAdapter);
        ImageView imageView = new ImageView(getActivity());
        imageView.setClickable(true);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.banner, imageView);
        imageView.setLayoutParams(
                new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mListView.addHeaderView(imageView);
        mListView.setXListViewListener(ixListViewListener);
        mListView.setRefreshTime(getTime());
        View ll = (View) LinearLayout.inflate(getActivity(), R.layout.listview_footer, null);
        mListView.addFooterView(ll);//设置可以上拉加载
        EventBus.getDefault().register(this);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUrgoo.class));
            }
        });
        ((LinearLayout) rootView.findViewById(R.id.activity_selectimg_send)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CounselorFilter.class));
            }
        });

    }


    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener() {
        int flag = 0;

        @Override
        public void onRefresh() {
            Log.d("..", "" + flag++);
            index = 0;
            mCounselorInfos.clear();
            //mListView.setPullLoadEnable(true);
            getCounselorInfo("0", "", "", "", "");
            searchFilter = new SearchFilter();
        }

        @Override
        public void onLoadMore() {
            getCounselorInfo(String.valueOf(index),
                    searchFilter.getCountryType(),
                    searchFilter.getIntgender(),
                    searchFilter.getServiceMode(),
                    searchFilter.getLableId());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(SearchFilter event) {
        Log.d("xxx", "event" + event.toString());
        searchFilter = event;
        mCounselorInfos.clear();
        index = 0;
        getCounselorInfo("0",
                searchFilter.getCountryType(),
                searchFilter.getIntgender(),
                searchFilter.getServiceMode(),
                searchFilter.getLableId());
    }

    private void getCounselorInfo(final String pageNum,
                                  String countryType,
                                  String intgender,
                                  String serviceMode,
                                  String lableId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", pageNum);
        params.put("token", SPManager.getInstance(getActivity()).getToken() == null ? "" :
                        SPManager.getInstance(getActivity()).getToken());
        params.put("countryType", countryType);
        params.put("gender", intgender);
        params.put("serviceMode", serviceMode);
        params.put("lableId", lableId);
        OkHttpClientManager.postAsyn(ZWConfig.Action_searchCounselorList, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                index++;
                stopRefresh();
                Log.d("cc", response);
                try {
                    org.json.JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                    org.json.JSONArray jsonArray = jsonObject.getJSONArray("counselorListInfoList");
                    ArrayList<CounselorInfo> list = (new Gson()).fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<CounselorInfo>>() {
                            }.getType());
                    mCounselorInfos.addAll(list);
                    Log.d("list...", list.toString());
                    counselorAdapter.notifyDataSetChanged();
                    android.util.Log.d("list.size : ", "" + list.size());
                    if (list == null || list.size() == 0) {
                        ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                        TextView tv = (TextView) rootView.findViewById(R.id.more);
                        pb.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                    } else {
                        if(index>2){
                            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                            TextView tv = (TextView) rootView.findViewById(R.id.more);
                            pb.setVisibility(View.VISIBLE);
                            tv.setText("加载中");
                            tv.setVisibility(View.VISIBLE);
                        }else {
                            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                            TextView tv = (TextView) rootView.findViewById(R.id.more);
                            pb.setVisibility(View.GONE);
                            tv.setVisibility(View.GONE);
                        }

                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    private void stopRefresh() {
        mListView.setRefreshTime(getTime());
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
