package com.urgoo.counselor.activities;

import android.view.View;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

/**
 * Created by bb on 2016/7/16.
 */
public class CounselorSearchList extends NavToolBarActivity {
    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_counselor, null);
        return view;
    }
}
