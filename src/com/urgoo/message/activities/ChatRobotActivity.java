package com.urgoo.message.activities;

import android.view.View;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by bb on 2016/10/20.
 */
public class ChatRobotActivity extends NavToolBarActivity {
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_robot_chat, null);
        setNavTitleText("");
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}
