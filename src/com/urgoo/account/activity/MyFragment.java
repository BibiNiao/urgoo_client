package com.urgoo.account.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/18.
 */
public class MyFragment extends BaseFragment {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_my, null);
        initViews();
        return viewContent;
    }

    private void initViews() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mToolbar = (Toolbar) viewContent.findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) viewContent.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) viewContent.findViewById(R.id.navigation_view);

        mToolbar.setTitle("我的");//设置Toolbar标题
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black_deep)); //设置标题颜色
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar, R.string.failed_to_load_data, R.string.failed_to_move_into);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent(mNavigationView);
        switchToBook();
    }

    private void switchToBook() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new RecyclerFragment()).commit();
    }

    private void switchToExample() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new DayFragment()).commit();
    }

    private void switchToBlog() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BlankFragment()).commit();
    }

    private void switchToAbout() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TimeFragment()).commit();
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.navigation_item_book:
                                switchToBook();
                                break;
                            case R.id.navigation_item_example:
                                switchToExample();
                                break;
                            case R.id.navigation_item_blog:
                                switchToBlog();
                                break;
                            case R.id.navigation_item_about:
                                switchToAbout();
                                break;

                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
