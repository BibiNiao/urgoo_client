package com.urgoo.counselor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.GuwenInfo;
import com.urgoo.view.XListView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
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

public class WorksActivity extends ActivityBase {
    protected XListView mListView;
    protected LinearLayout LinLyout_myorder_back;
    private ArrayList<GuwenInfo.WorksBean> mWorksList = new ArrayList<>();
    private WorksAdapter mWorksAdapter;
    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;
    private String counselorId;
    private View ll;
    private String totalSize;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mListView.stopRefresh();
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日    HH:mm");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            mListView.setRefreshTime(str);
            mWorksAdapter.notifyDataSetChanged();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);

        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("counselorId") != null) {
            counselorId = mIntent.getStringExtra("counselorId");
        }
        mListView = (XListView) findViewById(R.id.works_list);
        LinLyout_myorder_back = (LinearLayout) findViewById(R.id.LinLyout_myorder_back);
        LinLyout_myorder_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNetDataForupdate();
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mWorksAdapter = new WorksAdapter();
        mListView.setAdapter(mWorksAdapter);
        ll = (View) LinearLayout.inflate(WorksActivity.this, R.layout.listview_footer, null);
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
            }

            @Override
            public void onLoadMore() {
                // 正在加载
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mWorksAdapter.notifyDataSetChanged();
                mListView.stopLoadMore();
            }
        });
    }

    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSGTYPE_QBINTERACT_SUCC:
                    mWorksList.clear();
                    getNetDataForupdate();
                    break;
                case MSGTYPE_QB_FAIL: {
                    UiUtil.show(WorksActivity.this, "请检查网络或稍后重试");
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
        if (mWorksList != null && mWorksList.size() > 0) {
            if (mWorksList != null && mWorksList.size() >= 10) {
                mListView.setVisibility(View.VISIBLE);
                mWorksAdapter.notifyDataSetChanged();
            } else {
                mListView.setVisibility(View.VISIBLE);
                mWorksAdapter.notifyDataSetChanged();
                ProgressBar pb = (ProgressBar) ll.findViewById(R.id.loading);
                TextView tv = (TextView) ll.findViewById(R.id.more);
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
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("counselorId", counselorId);

        OkHttpClientManager.postAsyn(ZWConfig.URL_selectCounselorWorksList,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(String response) {
                        mWorksList.clear();
                        stopRefresh();
                        Log.d("mWorksList  数据 ", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            totalSize = new JSONObject(j.get("body").toString()).getString("totalSize");
                            if (code.equals("200")) {
                                JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                                JSONArray jsonArray = jsonObject.getJSONArray("counselorWorks");
                                ArrayList<GuwenInfo.WorksBean> list = (new Gson()).fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<GuwenInfo.WorksBean>>() {
                                        }.getType());
                                mWorksList.addAll(list);
                                Log.d("mtags", " mWorksList  :  " + mWorksList.toString());
                                mWorksAdapter.notifyDataSetChanged();
                                if (mWorksList != null && mWorksList.size() > 0) {
                                    refreshListView();
                                    ProgressBar pb = (ProgressBar) ll.findViewById(R.id.loading);
                                    TextView tv = (TextView) ll.findViewById(R.id.more);
                                    pb.setVisibility(View.GONE);
                                    tv.setText("数据已全部加载");
                                    tv.setVisibility(View.GONE);
                                } else {
                                    ProgressBar pb = (ProgressBar) ll.findViewById(R.id.loading);
                                    TextView tv = (TextView) ll.findViewById(R.id.more);
                                    pb.setVisibility(View.GONE);
                                    tv.setText("数据已全部加载");
                                    tv.setVisibility(View.GONE);
                                }
                            } else if (code.equals("404")) {
                                UiUtil.show(WorksActivity.this, message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(WorksActivity.this, message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(WorksActivity.this, "服务器繁忙，请稍后再试！");
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
        return new SimpleDateFormat("MM-dd HH:m", Locale.CHINA).format(new Date());
    }

    public class WorksAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mWorksList.size();
        }

        @Override
        public Object getItem(int position) {
            return mWorksList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(WorksActivity.this).inflate(R.layout.works_item, parent, false);
                viewHolder.works_title = (TextView) convertView.findViewById(R.id.works_title);
                viewHolder.works_time = (TextView) convertView.findViewById(R.id.works_time);
                viewHolder.works_RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.works_RelativeLayout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.works_title.setText(mWorksList.get(position).getTitle());
            viewHolder.works_time.setText(mWorksList.get(position).getInsertDateTime());
            viewHolder.works_RelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WorksActivity.this, BaseWebViewActivity.class)
                            .putExtra(BaseWebViewActivity.EXTRA_URL,
                                    ZWConfig.URGOOURL_BASE + "001/001/attention/workDtl?workId=" + mWorksList.get(position).getWorkId()));
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView works_title;
            private RelativeLayout works_RelativeLayout;
            private TextView works_time;
        }
    }

}
