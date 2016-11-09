package com.urgoo.message.activities;

import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.message.adapter.ChatRobotAdapter;
import com.urgoo.message.biz.MessageManager;
import com.urgoo.message.biz.Robot;
import com.urgoo.net.EventCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/20.
 */
public class ChatRobotActivity extends NavToolBarActivity {
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";
    private ListView mListView;
    //    private ScrollView scrollView;
    private String counselorId;
    private ChatRobotAdapter adapter;
    private List<Robot> robots = new ArrayList<>();

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_robot_chat, null);
        setSwipeBackEnable(false);
        setNavTitleText("优优");
        counselorId = getIntent().getStringExtra(EXTRA_COUNSELOR_ID);
        initViews(view);
        return view;
    }

    @Override
    protected int getOptionMenuRes() {
        return R.menu.text_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            onMenuItemClick(item.getActionView());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.save);
        item.setTitle("联系客服");
        return super.onPrepareOptionsMenu(menu);
    }

    private void onMenuItemClick(View v) {
        startActivity(new Intent(this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
    }

    public void getRobotAll(String counselorId, String level, String target, String text, String type) {
        MessageManager.getInstance(this).getRobotAll(counselorId, level, target, text, type, this);
    }

    private void initViews(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
//        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        adapter = new ChatRobotAdapter(this, counselorId, robots);
        mListView.setAdapter(adapter);
        getRobotAll(counselorId, "", "", "", "");
    }

    /**
     * 发送消息
     *
     * @param text
     */
    public void sendMessage(String text) {
        Robot robotRight = new Robot();
        robotRight.setMyself(true);
        robotRight.setText(text);
        robotRight.setStyle("3");
        adapter.addRobot(robotRight);
//        mListView.setSelection(mListView.getAdapter()
//                .getCount() - 1);
//        refreshToTail();
    }



    private Handler handler = new Handler();

    public void refreshToTail() {
        if (adapter != null) {
            if (mListView.getLastVisiblePosition()
                    - mListView.getFirstVisiblePosition() <= mListView.getCount())
                mListView.setStackFromBottom(false);
            else
                mListView.setStackFromBottom(true);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(mListView.getAdapter().getCount() - 1);
                    // This seems to work
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListView.clearFocus();
                            mListView.setSelection(mListView.getAdapter()
                                    .getCount() - 1);
                        }
                    });
                }
            }, 300);
            mListView.setSelection(adapter.getCount()
                    + mListView.getHeaderViewsCount() - 1);
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetRobotAll:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    Robot robot = gson.fromJson(jsonObject.toString(), new TypeToken<Robot>() {
                    }.getType());
                    robot.setMyself(false);
                    robot.setShow(true);
                    adapter.addRobot(robot);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
