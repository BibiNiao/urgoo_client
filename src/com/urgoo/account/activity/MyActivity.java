package com.urgoo.account.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.client.R;

/**
 * Created by bb on 2016/9/18.
 */
public class MyActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private TextView tvToolTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.failed_to_load_data, R.string.failed_to_move_into);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);
        switchToBook();

    }


    private void switchToBook() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new RecyclerFragment()).commit();
        tvToolTitle.setText("测试");
    }

    private void switchToExample() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new DayFragment()).commit();
        tvToolTitle.setText("测试");
    }

    private void switchToBlog() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BlankFragment()).commit();
        tvToolTitle.setText("测试");
    }

    private void switchToAbout() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TimeFragment()).commit();
        tvToolTitle.setText("测试");
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
