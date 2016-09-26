package com.urgoo.counselor.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.urgoo.base.BaseActivity;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by bb on 2016/9/26.
 */
public class CounselorDetailActivity extends NavToolBarActivity {
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";
    public static final String EXTRA_TITLE = "title";
    private String counselorId;

    @Override
    protected View createContentView() {
        setNavTitleText(getIntent().getStringExtra(EXTRA_TITLE));
        View mRootView = inflaterViewWithLayoutID(R.layout.activity_counselor, null);
        counselorId = getIntent().getStringExtra(EXTRA_COUNSELOR_ID);
        return mRootView;
    }

    protected int getOptionMenuRes() {
        return R.menu.client_menu;
    }
}
