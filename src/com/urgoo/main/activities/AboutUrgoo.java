package com.urgoo.main.activities;

import android.view.View;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by lijie on 2016/5/30.
 */
public class AboutUrgoo extends NavToolBarActivity {

    @Override
    protected View createContentView() {
        setNavTitleText("关于我们");
        View view = inflaterViewWithLayoutID(R.layout.about_me, null);
        return view;
    }
}
