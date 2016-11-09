package com.urgoo.profile.activities;

import android.view.View;
import android.widget.TextView;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SystemInformationDetail extends NavToolBarActivity {
    private TextView tv_messagecontent;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.systeminformationdetail, null);
        setNavTitleText("消息详情");
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        tv_messagecontent = (TextView) view.findViewById(R.id.tv_messagecontent);
        tv_messagecontent.setText(getIntent().getStringExtra("content"));
    }
}
