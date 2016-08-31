package com.urgoo.service.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;

public class ReportActivity extends ActivityBase implements View.OnClickListener {

    private LinearLayout ll_break_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ll_break_report = (LinearLayout) findViewById(R.id.ll_break_report);
        ll_break_report.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_break_report:
                finish();
                break;
        }
    }

    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
