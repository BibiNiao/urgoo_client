package com.urgoo.live.activities;

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
 * Created by bb on 2016/9/20.
 */
public class LiveFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    private LiveListFragment liveFragment;
    private AlbumFragment albumFragment;
    private AppBarLayout appBar;
    private final int TAB_LIVE = 0;
    private final int TAB_ALBUM = 1;
    private int index, offset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_live, container, false);
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
        liveFragment = new LiveListFragment();
        albumFragment = new AlbumFragment();
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
                    case TAB_LIVE:
                        liveFragment.setRefreshEnabled(offset == 0);
                        break;
                    case TAB_ALBUM:
                        albumFragment.setRefreshEnabled(offset == 0);
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
                    case TAB_LIVE:
                        liveFragment.setScrollTop();
                        break;
                    case TAB_ALBUM:
                        albumFragment.setScrollTop();
                        break;
                }
                index = tab.getPosition();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        adapter.addFrag(liveFragment, "直播");
        adapter.addFrag(albumFragment, "专辑");

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        offset = i;
        switch (index) {
            case TAB_LIVE:
                liveFragment.setRefreshEnabled(offset == 0);
                break;
            case TAB_ALBUM:
                albumFragment.setRefreshEnabled(offset == 0);
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
