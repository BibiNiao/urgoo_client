package com.urgoo.collect.activites;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.live.adapter.ViewPagerFragmentAdapter;

/**
 * Created by bb on 2016/9/23.
 */
public class CollectFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    private FollowCounselorFragment followCounselorFragment;
    private FollowVideoFragment followVideoFragment;
    private AppBarLayout appBar;
    private final int TAB_CONSULTANT = 0;
    private final int TAB_VIDEO = 1;
    private int index, offset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_collect, container, false);
        initViews();
        setHasOptionsMenu(true);
        return viewContent;
    }

    private void initViews() {
        appBar = (AppBarLayout) viewContent.findViewById(R.id.appbar);
        setTabFragment();
        setupViewPager();
    }

    private void setTabFragment() {
        followCounselorFragment = new FollowCounselorFragment();
        followVideoFragment = new FollowVideoFragment();
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) viewContent.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) viewContent.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case TAB_CONSULTANT:
                        followCounselorFragment.setRefreshEnabled(offset == 0);
                        break;
                    case TAB_VIDEO:
                        followVideoFragment.setRefreshEnabled(offset == 0);
                        break;
                }
                index = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_CONSULTANT:
                        followCounselorFragment.setScrollTop();
                        break;
                    case TAB_VIDEO:
                        followVideoFragment.setScrollTop();
                        break;
                }
                index = tab.getPosition();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        adapter.addFrag(followCounselorFragment, "收藏的顾问");
        adapter.addFrag(followVideoFragment, "收藏的视频");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        offset = i;
        switch (index) {
            case TAB_CONSULTANT:
                followCounselorFragment.setRefreshEnabled(offset == 0);
                break;
            case TAB_VIDEO:
                followVideoFragment.setRefreshEnabled(offset == 0);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        appBar.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        appBar.addOnOffsetChangedListener(this);
    }
}
