package com.urgoo.schedule.activites;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.adapter.TobeconfirAdapter;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.tobeconfirmedmyorderBean;
import com.urgoo.view.XListView;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by duanfei on 2016/6/14.
 */
public class tobeconfirmedmyorderFragment extends Fragment {
    protected View mRootView;
    protected XListView mListView;
    private ArrayList<tobeconfirmedmyorderBean> mTobeconfir = new ArrayList<tobeconfirmedmyorderBean>();
    private TobeconfirAdapter mTobeconfirAdapter;
    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mListView.stopRefresh();
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日    HH:mm");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            mListView.setRefreshTime(str);
            mTobeconfirAdapter.notifyDataSetChanged();
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mine_myroder_tobe, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (XListView) mRootView.findViewById(R.id.myroder_tobe_ListView);
        getNetDataForupdate();
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mTobeconfirAdapter = new TobeconfirAdapter(getActivity(), mTobeconfir, true);
        mListView.setAdapter(mTobeconfirAdapter);
        View ll = (View) LinearLayout.inflate(getActivity(), R.layout.listview_footer, null);
        mListView.addFooterView(ll);//设置可以上拉加载
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 正在刷新
                        try {
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                ;
            }

            @Override
            public void onLoadMore() {
                // 正在加载
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTobeconfirAdapter.notifyDataSetChanged();
                mListView.stopLoadMore();
            }
        });

    }

    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSGTYPE_QBINTERACT_SUCC:
                    mTobeconfir.clear();
                    getNetDataForupdate();
                    break;
                case MSGTYPE_QB_FAIL: {
                    UiUtil.show(getActivity(), "请检查网络或稍后重试");
                    break;
                }
                default: {
                    super.handleMessage(msg);
                    break;
                }
            }
        }
    };

    private void refreshListView() {
        if (mTobeconfir != null && mTobeconfir.size() > 0) {
            if (mTobeconfir != null && mTobeconfir.size() >= 10) {
                mListView.setVisibility(View.VISIBLE);
                mTobeconfirAdapter.notifyDataSetChanged();
            } else {
                mListView.setVisibility(View.VISIBLE);
                mTobeconfirAdapter.notifyDataSetChanged();
                ProgressBar pb = (ProgressBar) mRootView.findViewById(R.id.loading);
                TextView tv = (TextView) mRootView.findViewById(R.id.more);
                pb.setVisibility(View.GONE);
                tv.setText("数据已全部加载");
                tv.setVisibility(View.GONE);
            }
        } else {
            mListView.setVisibility(View.GONE);

        }
    }

    private void getNetDataForupdate() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(getActivity()).getToken());
        OkHttpClientManager.postAsyn(ZWConfig.URL_advanceUnconfirmeListClient, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(String response) {
                        mTobeconfir.clear();
                        stopRefresh();
                        Log.d("tobe json 数据 ", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");

                            if (code.equals("200")) {

                                JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                                JSONArray jsonArray = jsonObject.getJSONArray("advanceList");
                                ArrayList<tobeconfirmedmyorderBean> list = (new Gson()).fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<tobeconfirmedmyorderBean>>() {
                                        }.getType());
                                mTobeconfir.addAll(list);
                                com.zw.express.tool.log.Log.d("mtags", "tobe list :  " + list.toString());
                                mTobeconfirAdapter.notifyDataSetChanged();
                                if (mTobeconfir != null && mTobeconfir.size() > 0) {
                                    refreshListView();
                                    ProgressBar pb = (ProgressBar) mRootView.findViewById(R.id.loading);
                                    TextView tv = (TextView) mRootView.findViewById(R.id.more);
                                    pb.setVisibility(View.GONE);
                                    tv.setText("数据已全部加载");
                                    tv.setVisibility(View.GONE);
                                } else {
                                    ProgressBar pb = (ProgressBar) mRootView.findViewById(R.id.loading);
                                    TextView tv = (TextView) mRootView.findViewById(R.id.more);
                                    pb.setVisibility(View.GONE);
                                    tv.setText("数据已全部加载");
                                    tv.setVisibility(View.GONE);
                                }
                            } else if (code.equals("404")) {
                                UiUtil.show(getActivity(), message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(getActivity(), message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(getActivity(), "服务器繁忙，请稍后再试！");
                        }
                    }
                },
                params);
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
