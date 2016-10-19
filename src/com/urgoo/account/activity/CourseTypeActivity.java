package com.urgoo.account.activity;

import android.os.Bundle;
import android.view.View;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by bb on 2016/9/30.
 */
public class CourseTypeActivity extends NavToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected View createContentView() {
        setNavTitleText("课程类型");
        View view = inflaterViewWithLayoutID(R.layout.activity_course, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}
