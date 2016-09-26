package com.urgoo.schedule.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.Reason;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CancellationActivity extends BaseActivity {
    private String advanceId;
    private static int HANDLER = 0;
    private String TAG = "duan";
    private EditText EditTexts;
    private Handler mHandler;
    private Button but_ppw_cancel_submit;
    private ArrayList<Reason.reasonBean> mReasonBeen = new ArrayList<>();
    private String str_EditText = "未填写";
    private LinearLayout LinLyout_schedule_back;
    private ListView search_user_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("advanceId") != null) {
            advanceId = mIntent.getStringExtra("advanceId");
        } else {
            Toast.makeText(CancellationActivity.this, "网络繁忙，请稍后2秒再试！", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "  传参错误  ");
        }
        initviews();
        getdata();

        LinLyout_schedule_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == HANDLER) {
                    Log.d(TAG, "msg" + msg.what);
                    search_user_list.setAdapter(new SearchUserAdapter());
                    getListHeight(search_user_list);

                    EditTexts.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            str_EditText = s.toString();
                            Log.d(TAG, " str_EditText : " + str_EditText);
                        }
                    });

                    but_ppw_cancel_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Cancellation();
                        }
                    });
                }
            }
        };
    }

    private void Cancellation() {
        //发送一条取消的请求
        if (str_EditText != null) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("token", spManager.getToken());
            params.put("advanceId", advanceId);
            params.put("reason", str_EditText);

            OkHttpClientManager.postAsyn(ZWConfig.URL_cancelAdvanceClient
                    , new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                Log.d(TAG, "true");
                                JSONObject j = new JSONObject(response);
                                String code = new JSONObject(j.get("header").toString()).getString("code");
                                if (code.equals("200")) {
                                    Toast.makeText(CancellationActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CancellationActivity.this, PrecontractMyOrder.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                UiUtil.show(CancellationActivity.this, "请求失败");
                            }
                        }
                    }, params);
        }
    }

    private void getdata() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());

        OkHttpClientManager.postAsyn(ZWConfig.URL_advanceReason,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            Log.d(TAG, "接收预约 true  ");
                            JSONObject j = new JSONObject(response);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                                JSONArray jsondeta = jsonObject.getJSONArray("reasonList");
                                ArrayList<Reason.reasonBean> list = (new Gson()).fromJson(jsondeta.toString(),
                                        new TypeToken<ArrayList<Reason.reasonBean>>() {
                                        }.getType());
                                mReasonBeen.addAll(list);
                                Log.d(TAG, "mReasonBeen: " + mReasonBeen.toString());

                                Message msg = new Message();
                                msg.what = HANDLER;
                                mHandler.sendMessage(msg);
                            } else if (code.equals("404")) {
                                UiUtil.show(CancellationActivity.this, message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(CancellationActivity.this, message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(CancellationActivity.this, "读取错误");
                        }
                    }
                }
                , params
        );
    }

    private void initviews() {
        EditTexts = (EditText) findViewById(R.id.EditText);
        search_user_list = (ListView) findViewById(R.id.search_user_list);
        but_ppw_cancel_submit = (Button) findViewById(R.id.but_ppw_cancel_submit);
        LinLyout_schedule_back = (LinearLayout) findViewById(R.id.LinLyout_schedule_back);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class SearchUserAdapter extends BaseAdapter {
        HashMap<String, Boolean> states = new HashMap<String, Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个

        @Override
        public int getCount() {
            return mReasonBeen.size();
        }

        @Override
        public Object getItem(int position) {
            return mReasonBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(CancellationActivity.this).inflate(R.layout.search_user_item, null);
                holder = new ViewHolder();
                holder.rdBtn = (RadioButton) convertView.findViewById(R.id.RadioButton3);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.rdBtn.setText(mReasonBeen.get(position).getReason());
            holder.rdBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for (String key : states.keySet()) {
                        states.put(key, false);
                    }
                    states.put(String.valueOf(position), holder.rdBtn.isChecked());
                    SearchUserAdapter.this.notifyDataSetChanged();
                    if (mReasonBeen.size() - 1 == position) {
                        if (str_EditText == null) {
                            str_EditText = "未填写";
                        } else {
                            if (EditTexts.getText().toString() != null) {
                                str_EditText = EditTexts.getText().toString();
                            } else
                                str_EditText = mReasonBeen.get(position).getReason() + "。";
                        }
                        EditTexts.setFocusable(true);
                        EditTexts.setFocusableInTouchMode(true);
                        EditTexts.requestFocus();
                    } else {
                        str_EditText = mReasonBeen.get(position).getReason() + "。";
                        EditTexts.clearFocus();
                        EditTexts.setFocusable(false);
                    }
                }
            });

            boolean res = false;
            if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
                res = false;
                states.put(String.valueOf(position), false);
            } else
                res = true;

            holder.rdBtn.setChecked(res);

            return convertView;
        }

        class ViewHolder {
            RadioButton rdBtn;
        }
    }

    private void getListHeight(ListView mlistview) {
        ListAdapter listAdapter = mlistview.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, mlistview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mlistview.getLayoutParams();
        params.height = totalHeight
                + (mlistview.getDividerHeight() * (listAdapter.getCount() - 1));
        mlistview.setLayoutParams(params);
    }
}
