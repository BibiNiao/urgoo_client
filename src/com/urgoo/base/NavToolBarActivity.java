package com.urgoo.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.urgoo.client.R;

/**
 * Created by bb.
 */
public abstract class NavToolBarActivity extends BaseActivity {
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        RelativeLayout mRoot = (RelativeLayout) inflaterViewWithLayoutID(
                R.layout.nav_toolbar, null);
        mToolbar = (Toolbar) mRoot.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavLeftClick(v);
            }
        });

        View view = LayoutInflater.from(this).inflate(R.layout.abc_action_bar_title_item, null);
        mToolbar.addView(view);
        /** 内容区父容器 */
        LinearLayout mContent = (LinearLayout) mRoot.findViewById(R.id.content);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContent.addView(createContentView(), mParams);
        setContentView(mRoot);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuRes = getOptionMenuRes();
        if (menuRes != -1) {
            getMenuInflater().inflate(menuRes, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 获取创建OptionMenu的资源ID
     */
    protected int getOptionMenuRes() {
        return -1;
    }

    /**
     * 导航栏左侧点击事件回调【默认处理方式为返回】
     *
     * @param v
     */
    protected void onNavLeftClick(View v) {
        onBackPressed();
    }


    /**
     * 设置通用导航栏标题文字
     *
     * @param str
     */
    public void setNavTitleText(String str) {
        if (null != str) {
            mToolbar.setTitle(str);
        }
    }

    /**
     * 设置通用导航栏标题文字
     *
     * @param resId
     */
    public void setNavTitleText(int resId) {
        mToolbar.setTitle(resId);
    }

    /**
     * 隐藏导航栏icon
     */
    public void setNavGone() {
        mToolbar.setNavigationIcon(null);
    }

    /**
     * 设置左侧导航栏icon
     */
    public void setNavIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    /**
     * 创建内容区视图组件
     *
     * @return View 内容区视图
     */
    protected abstract View createContentView();

}
