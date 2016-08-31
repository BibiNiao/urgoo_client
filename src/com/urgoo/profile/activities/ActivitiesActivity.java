package com.urgoo.profile.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urgoo.base.FragmentActivityBase;
import com.urgoo.client.R;

import java.util.ArrayList;

public class ActivitiesActivity extends FragmentActivityBase implements View.OnClickListener {
    private LinearLayout LinLyout_myorder_back;
    public ArrayList<Fragment> fragmentslist;
    private ViewPager mPager;
    private Resources resources;
    private ImageView line;
    private int one, two, three;
    private int currIndex = 0;
    private TextView notat, ongoing, ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        initviews();
        InitViewPager();
        InitWidth();
        initData();
        initLinsent();
    }

    public void initData() {
        notat.setOnClickListener(this);
        ongoing.setOnClickListener(this);
        ended.setOnClickListener(this);
    }

    private void initLinsent() {
        LinLyout_myorder_back.setOnClickListener(this);
    }

    private void initviews() {
        LinLyout_myorder_back = (LinearLayout) findViewById(R.id.LinLyout_myorder_back);
        ended = (TextView) findViewById(R.id.ended);
        ongoing = (TextView) findViewById(R.id.ongoing);
        notat = (TextView) findViewById(R.id.notat);
        resources = getResources();
    }

    // 获取设备屏幕大小的方法
    private void InitWidth() {
        line = (ImageView) findViewById(R.id.image);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenw = dm.widthPixels;
        one = (int) (screenw / 3.0);
        two = one * 2;
        three = one * 3;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinLyout_myorder_back:
                finish();
                break;
            case R.id.ended:
                mPager.setCurrentItem(2);
                break;
            case R.id.ongoing:
                mPager.setCurrentItem(1);
                break;
            case R.id.notat:
                mPager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentslist = new ArrayList<Fragment>();
        fragmentslist.add(ActivityesNotAtFragment.getInstance());
        fragmentslist.add(ActivityesOngAtFragment.getInstance());
        fragmentslist.add(ActivityesHasAtFragment.getInstance());
        mPager.setAdapter(new MyAdapter(getSupportFragmentManager(),
                fragmentslist));

        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragmentlist;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            fragmentlist = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentlist.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    ended.setTextColor(resources.getColor(R.color.lightwhite));
                    ongoing.setTextColor(resources.getColor(R.color.lightwhite));
                    notat.setTextColor(resources.getColor(R.color.white));
                    break;
                case 1:
                    ended.setTextColor(resources.getColor(R.color.lightwhite));
                    ongoing.setTextColor(resources.getColor(R.color.white));
                    notat.setTextColor(resources.getColor(R.color.lightwhite));
                    break;
                case 2:
                    ended.setTextColor(resources.getColor(R.color.white));
                    ongoing.setTextColor(resources.getColor(R.color.lightwhite));
                    notat.setTextColor(resources.getColor(R.color.lightwhite));
                    break;

            }
            Animation animation = new TranslateAnimation(one * currIndex, one
                    * arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            line.startAnimation(animation);
        }
    }
}
