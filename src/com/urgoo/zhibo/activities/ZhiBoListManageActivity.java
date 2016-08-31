package com.urgoo.zhibo.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urgoo.base.BaseFragment;
import com.urgoo.base.FragmentActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.FirstEvent;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ZhiBoListManageActivity extends BaseFragment {

    // 15
    public ArrayList<Fragment> fragmentslist;
    private ViewPager mPager;
    Resources resources;
    private ImageView line;
    private int one, two, three;
    private int currIndex = 0;
    private TextView dongtai, group;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.zhibolistmanage_layout, container, false);
        InitViewPager();
        resources = getResources();
        InitTextview();
        InitWidth();
        return viewContent;
    }
    // 15
    private void InitTextview() {
        dongtai = (TextView) viewContent.findViewById(R.id.dongtai);
        group = (TextView) viewContent.findViewById(R.id.group);
        // 这是类声明 要是直接是函数就没有new
        dongtai.setOnClickListener(new MyOnClickListener(0));
        group.setOnClickListener(new MyOnClickListener(1));

    }

    // 获取设备屏幕大小的方法
    private void InitWidth() {
        line = (ImageView) viewContent.findViewById(R.id.image);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenw = dm.widthPixels;
        one = (int) (screenw / 2.0);
        two = one * 2;
        three = one * 3;
    }

    // 15
    private void InitViewPager() {
        mPager = (ViewPager) viewContent.findViewById(R.id.viewpager);
        fragmentslist = new ArrayList<Fragment>();
        fragmentslist.add(NewZhiBoFragment.getInstance());
        fragmentslist.add(OldZhiBoFragment.getInstance());
        mPager.setAdapter(new MyAdapter(getFragmentManager(),
                fragmentslist));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    // 15
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
                    group.setTextColor(resources.getColor(R.color.lightwhite));
                    dongtai.setTextColor(resources.getColor(R.color.white));
                    break;
                case 1:
                    group.setTextColor(resources.getColor(R.color.white));
                    dongtai.setTextColor(resources.getColor(R.color.lightwhite));
                    break;

            }
            SetAnimation(arg0);
        }
    }

    // 动画
    private void SetAnimation(int arg0) {
        Animation animation = new TranslateAnimation(one * currIndex, one
                * arg0, 0, 0);
        currIndex = arg0;
        animation.setFillAfter(true);
        animation.setDuration(300);
        line.startAnimation(animation);
    }

    // 15
    public class MyOnClickListener implements View.OnClickListener {
        private int index;

        public MyOnClickListener(int i) {
            // Log.d("myDebug", "MyOnClickListener:" + i);
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }
}
