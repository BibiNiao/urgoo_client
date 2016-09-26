package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urgoo.adapter.NewZhiBoListAdapter;
import com.urgoo.base.HomeFragment;
import com.urgoo.client.R;
import com.urgoo.domain.ZhiBoEntity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.biz.ProfileManager;
import com.urgoo.view.XListView;
import com.urgoo.live.activities.ZhiBodDetailActivity;
import com.zw.express.tool.GsonTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by urgoo_01 on 2016/7/20.
 */
public class ActivityesOngAtFragment extends HomeFragment {
    private View rootView;
    private XListView mListView;
    private RelativeLayout rl_nofollow;
    private String totalSize;
    private NewZhiBoListAdapter mAdapter;
    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;
    private List<Object> mList = new ArrayList<Object>();
    private int pageNo = 0;
    private int pageSize = 18; // 3的倍数
    private int index = 0;
    private boolean isMore = true;
    private boolean isLoading = false;
    private long doubleClick = 0l;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            refreshData();
            mListView.stopRefresh();
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日    HH:mm");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            mListView.setRefreshTime(str);
            mAdapter.notifyDataSetChanged();
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sign, container, false);
        initView();
        refreshData();
        return rootView;
    }

    private void refreshData() {
        pageNo = 0;
        pageSize = 10;
        index = 0;
        isLoading = true;
        getOngDataList();
    }

    private void getOngDataList() {
        ProfileManager.getInstance(getActivity()).getOngDataList("1", "", "2", pageNo, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetOngDataList:
                try {
                    if (pageNo == 0) {
                        mList.clear();
                    }
                    totalSize = result.getJSONObject("body").getString("totalSize");
                    if (totalSize.equals("0")) {
                        rl_nofollow.setVisibility(View.VISIBLE);
                    } else {
                        rl_nofollow.setVisibility(View.GONE);
                    }
                    JSONObject jo2 = result.getJSONObject("body");
                    JSONArray ja = jo2.getJSONArray("liveList");
                    Log.d("mytest718", "liveList->ja->" + ja.toString());
                    if (ja != null && ja.length() > 0) {
                        for (int i = 0; i < ja.length(); i++) {
                            //JSONObject j = ja.getJSONObject(i);
                            ZhiBoEntity entity = GsonTools.getTargetClass(ja.getJSONObject(i).toString(), ZhiBoEntity.class);
                            if (entity != null) {
                                mList.add(entity);
                            }
                        }
                        refreshListView();
                        pageNo++;
                        ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                        TextView tv = (TextView) rootView.findViewById(R.id.more);
                        pb.setVisibility(View.GONE);
                        tv.setText("数据已全部加载");
                        tv.setVisibility(View.GONE);
                    } else {
                        ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                        TextView tv = (TextView) rootView.findViewById(R.id.more);
                        pb.setVisibility(View.GONE);
                        tv.setText("数据已全部加载");
                        tv.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoading = false;
                break;
        }
    }

    protected void initView() {
        mListView = (XListView) rootView.findViewById(R.id.listView);
        rl_nofollow = (RelativeLayout) rootView.findViewById(R.id.rl_nofollow);
        mAdapter = new NewZhiBoListAdapter(getActivity(), mList, 0);
        mListView.setAdapter(mAdapter);
        View ll = (View) LinearLayout.inflate(getActivity(), R.layout.listview_footer, null);
        mListView.addFooterView(ll);//设置可以上拉加载
        mListView.setPullRefreshEnable(true);//设置可以下拉刷新  默认可以下拉刷新
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                // 正在加载
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                mListView.stopLoadMore();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (System.currentTimeMillis() - doubleClick < 1000) {
                    doubleClick = System.currentTimeMillis();
                    return;
                }
                if (position <= mList.size()) {
                    ZhiBoEntity entity = (ZhiBoEntity) mList.get(position - 1);
                    Intent intent = new Intent(getActivity(), ZhiBodDetailActivity.class);
                    intent.putExtra("liveId", entity.getLiveId());
                    startActivity(intent);
                }
            }
        });
        // 自动加载更多
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() { //
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            getOngDataList();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void refreshListView() {
        if (mList != null && mList.size() > 0) {
            if (mList != null && mList.size() > 0) {
                mListView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            } else {
                mListView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.loading);
                TextView tv = (TextView) rootView.findViewById(R.id.more);
                pb.setVisibility(View.GONE);
                tv.setText("数据已全部加载");
                tv.setVisibility(View.GONE);
            }
        } else {
            mListView.setVisibility(View.GONE);
        }
    }

    public static Fragment getInstance() {
        ActivityesOngAtFragment fragment = new ActivityesOngAtFragment();
        return fragment;
    }

}
