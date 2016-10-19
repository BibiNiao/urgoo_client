package com.urgoo.account.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by bb on 2016/9/29.
 */
public class SchoolActivity extends NavToolBarActivity {
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected View createContentView() {
        setNavTitleText("就读学校");
        View view = inflaterViewWithLayoutID(R.layout.activity_school, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        etContent = (EditText) view.findViewById(R.id.et_content);
    }
}
