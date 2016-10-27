package com.urgoo.service.activities;

import android.view.View;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

public class ReportActivity extends NavToolBarActivity{

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_report, null);
        setNavTitleText("顾问报告模板");
        return view;
    }
}
