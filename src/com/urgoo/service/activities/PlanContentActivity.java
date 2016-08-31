package com.urgoo.service.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.domain.TaskEntity;
import com.urgoo.net.EventCode;
import com.urgoo.service.biz.ServerManager;
import com.urgoo.service.model.PlanContentEntity;
import com.urgoo.view.MyListView;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class PlanContentActivity extends ActivityBase implements View.OnClickListener {

    private LinearLayout ll_breakss;
    private String type;
    private MyListView plan_listView;
    private ArrayList<PlanContentEntity> mContentEntities = new ArrayList<>();
    private TaskContentListAdapter mAdapter;
    private TextView myorder_message_title;
    private Boolean fis = true;
    private View views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_content);
        if (!Util.isEmpty(getIntent().getStringExtra("type"))) {
            type = getIntent().getStringExtra("type");
            showLoadingDialog();
            ServerManager.getInstance(PlanContentActivity.this).getPlanContent(this, type);
        } else {
            Toast.makeText(PlanContentActivity.this, "网络链接超时，请稍后再试!", Toast.LENGTH_SHORT).show();
            finish();
        }

        initview();
        switch (Integer.valueOf(type)) {
            case 1:
                myorder_message_title.setText("语言能力");
                break;
            case 2:
                myorder_message_title.setText("学术能力");
                break;
            case 3:
                myorder_message_title.setText("批判思维");
                break;
            case 4:
                myorder_message_title.setText("领导力");
                break;
            case 5:
                myorder_message_title.setText("自我认知");
                break;
            case 6:
                myorder_message_title.setText("创造力");
                break;
        }
    }

    private void initview() {
        ll_breakss = (LinearLayout) findViewById(R.id.ll_breakss);
        plan_listView = (MyListView) findViewById(R.id.plan_listView);
        myorder_message_title = (TextView) findViewById(R.id.myorder_message_title);
        views = (View) findViewById(R.id.view);
        ll_breakss.setOnClickListener(this);
    }

    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new TaskEntity());
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_breakss:
                finish();
                break;
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeNewTimeLine:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mContentEntities = gson.fromJson(jsonObject.getString("timeLine"), new TypeToken<ArrayList<PlanContentEntity>>() {
                    }.getType());
                    Log.d("uuu ", "mContentEntities: " + mContentEntities.toString());
                    views.setVisibility(View.VISIBLE);
                    mAdapter = new TaskContentListAdapter();
                    plan_listView.setAdapter(mAdapter);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }

    public class TaskContentListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mContentEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return mContentEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(PlanContentActivity.this).inflate(R.layout.plan_cont_itme, parent, false);
                viewHolder.tv_nickname = (TextView) view.findViewById(R.id.tv_plan_item_cont);
                viewHolder.view1 = (View) view.findViewById(R.id.view1);
                viewHolder.view2 = (View) view.findViewById(R.id.view2);
                viewHolder.img_dian = (ImageView) view.findViewById(R.id.img_dian);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (("" + mContentEntities.get(position).getStatus()).equals("3")) {
                viewHolder.tv_nickname.setBackgroundDrawable(getResources().getDrawable(R.drawable.wancheng));
                viewHolder.view1.setBackgroundColor(getResources().getColor(R.color.tv26bdab));
                viewHolder.view2.setBackgroundColor(getResources().getColor(R.color.tv26bdab));
                viewHolder.img_dian.setBackgroundDrawable(getResources().getDrawable(R.drawable.dian_lv));
                if (("" + mContentEntities.get(position + 1).getStatus()).equals("1")) {
                    viewHolder.view2.setBackgroundColor(getResources().getColor(R.color.tvc9c9c9));
                }
                fis = false;
            } else {
                if (fis && position == 0) {
                    views.setBackgroundColor(getResources().getColor(R.color.tvc9c9c9));
                }
                viewHolder.view1.setBackgroundColor(getResources().getColor(R.color.tvc9c9c9));
                viewHolder.view2.setBackgroundColor(getResources().getColor(R.color.tvc9c9c9));
                viewHolder.img_dian.setBackgroundDrawable(getResources().getDrawable(R.drawable.dian_hui));
                viewHolder.tv_nickname.setBackgroundDrawable(getResources().getDrawable(R.drawable.nowan));
            }
            viewHolder.tv_nickname.setText(mContentEntities.get(position).getSubjectCn());
            return view;
        }

        private class ViewHolder {
            private TextView tv_nickname;
            private View view1;
            private View view2;
            private ImageView img_dian;
        }
    }

}
