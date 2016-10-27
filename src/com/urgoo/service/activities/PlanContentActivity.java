package com.urgoo.service.activities;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.domain.TaskEntity;
import com.urgoo.net.EventCode;
import com.urgoo.service.biz.ServerManager;
import com.urgoo.service.model.PlanContentEntity;
import com.urgoo.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class PlanContentActivity extends NavToolBarActivity {
    private String type;
    private MyListView plan_listView;
    private ArrayList<PlanContentEntity> mContentEntities = new ArrayList<>();
    private TaskContentListAdapter mAdapter;
    private TextView myorder_message_title;
    private Boolean fis = true;
    private View views;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_plan_content, null);
        initView(view);
        setNavTitleText(getIntent().getStringExtra("title"));
        type = getIntent().getStringExtra("type");
        showLoadingDialog();
        ServerManager.getInstance(PlanContentActivity.this).getPlanContent(this, type);
        return view;
    }

    private void initView(View view) {
        plan_listView = (MyListView) view.findViewById(R.id.plan_listView);
        myorder_message_title = (TextView) view.findViewById(R.id.myorder_message_title);
        views = view.findViewById(R.id.view);
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
            case EventCodeNewTimeLine:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mContentEntities = gson.fromJson(jsonObject.getString("timeLine"), new TypeToken<ArrayList<PlanContentEntity>>() {
                    }.getType());
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
