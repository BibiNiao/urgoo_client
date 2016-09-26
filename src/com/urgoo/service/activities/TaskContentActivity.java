package com.urgoo.service.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.domain.TaskEntity;
import com.urgoo.net.EventCode;
import com.urgoo.service.biz.ServerManager;
import com.urgoo.service.model.PlanTaskEntity;
import com.urgoo.view.MyListView;
import com.urgoo.view.MyScrollView;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class TaskContentActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_break;
    private MyListView mListView;
    private ArrayList<PlanTaskEntity> mContentEntities = new ArrayList<>();
    private ArrayList<PlanTaskEntity.AttachedFile> mFiles = new ArrayList<>();
    private String taskId;
    private TaskContentListAdapter MyAdapter;
    private TextView tv_task_titele;
    private TextView tv_task_content;
    private MyScrollView sw_pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_content);
        if (!Util.isEmpty(getIntent().getStringExtra("taskId"))) {
            taskId = getIntent().getStringExtra("taskId");
            ServerManager.getInstance(TaskContentActivity.this).getTaskContent(this, taskId);
        } else {
            Toast.makeText(TaskContentActivity.this, "网络链接超时，请稍后再试!", Toast.LENGTH_SHORT).show();
            finish();
        }
        initviews();
    }

    private void initviews() {
        ll_break = (LinearLayout) findViewById(R.id.ll_breakss);
        ll_break.setOnClickListener(this);
        mListView = (MyListView) findViewById(R.id.tasak_cont_listView);
        tv_task_titele = (TextView) findViewById(R.id.tv_task_titele);
        tv_task_content = (TextView) findViewById(R.id.tv_task_content);
        sw_pl = (MyScrollView) findViewById(R.id.sw_pl);
        MyAdapter = new TaskContentListAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_breakss:
                finish();
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
                view = LayoutInflater.from(TaskContentActivity.this).inflate(R.layout.task_cont_item, parent, false);
                viewHolder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
                viewHolder.tv_shijian = (TextView) view.findViewById(R.id.tv_shijian);
                viewHolder.tv_info = (TextView) view.findViewById(R.id.tv_info);
                viewHolder.ll_fujian = (LinearLayout) view.findViewById(R.id.ll_fujian);
                viewHolder.tv_cont_fujian = (TextView) view.findViewById(R.id.tv_cont_fujian);
                viewHolder.img_touxiang = (com.urgoo.view.CircleImageView) view.findViewById(R.id.img_touxiang);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            if (mContentEntities.get(position).getAttachedFile().size() > 0) {
                viewHolder.ll_fujian.setVisibility(View.VISIBLE);
                viewHolder.tv_cont_fujian.setText(mContentEntities.get(position).getAttachedFile().get(0).getFileName());
            } else {
                viewHolder.ll_fujian.setVisibility(View.INVISIBLE);
            }

            viewHolder.tv_nickname.setText(mContentEntities.get(position).getUserName());
            viewHolder.tv_shijian.setText(mContentEntities.get(position).getInsertDatetime());
            viewHolder.tv_info.setText(mContentEntities.get(position).getReplyContent());
            new imageLoadBusiness().imageLoadByURL(mContentEntities.get(position).getUserIcon(), viewHolder.img_touxiang);
            return view;
        }

        private class ViewHolder {
            private TextView tv_nickname;
            private TextView tv_shijian;
            private TextView tv_info;
            private TextView tv_cont_fujian;
            private LinearLayout ll_fujian;
            private com.urgoo.view.CircleImageView img_touxiang;
        }

    }

    //  计算list实际高度
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
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case newTaskDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    JSONObject jsonObjects = jsonObject.getJSONObject("taskDetail");
                    String taskContent = jsonObjects.getString("taskContent");
                    String subject = jsonObjects.getString("subject");
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mContentEntities = gson.fromJson(jsonObject.getString("taskComment"), new TypeToken<ArrayList<PlanTaskEntity>>() {
                    }.getType());

                    Log.d("zzzzz", "mContentEntities  " + mContentEntities.toString());
                    mListView.setAdapter(MyAdapter);
                    getListHeight(mListView);
                    tv_task_titele.setText(subject);
                    tv_task_content.setText(taskContent);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }

}
