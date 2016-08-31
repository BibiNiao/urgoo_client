package com.urgoo.schedule.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.ScreenManager;
import com.urgoo.base.FragmentActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.event.CounselorEvent;
import com.urgoo.data.SPManager;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by duanfei on 2016/6/14.
 */

public class PrecontractMyOrder extends FragmentActivityBase {
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private static final int[] ORDER_TAB_NAMES =
            new int[]{
                    R.string.to_be_confirmed,
                    R.string.confirmed,
                    R.string.complete};
    private TabHost mMyorderTabHost;
    private ViewPager mMyorderViewPage;
    private LinearLayout LinLyout_myorder_back;
    private String systemCount = "0";
    private String TaskCount = "0";
    private String AccountCount = "0";
    private MyFragmentPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_myorder);
        ScreenManager.getScreenManager().pushActivity(this);
        // 注册EventBus

        getisshow();
        initView();
        mMyorderTabHost.setup();
        mFragments.add(new tobeconfirmedmyorderFragment());
        mFragments.add(new confirmedmyorderFragment());
        mFragments.add(new completemyorderFragment());
        LinLyout_myorder_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CounselorEvent());
                finish();
            }
        });
        mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), mFragments);
        mMyorderViewPage.setAdapter(mAdapter);
        TabHost.TabContentFactory factory = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return new View(PrecontractMyOrder.this);
            }
        };

        for (int i = 0; i < ORDER_TAB_NAMES.length; ++i) {
            TabHost.TabSpec tabSpec = mMyorderTabHost.newTabSpec(getResources().getString(ORDER_TAB_NAMES[i]));
            tabSpec.setIndicator(createTabView(ORDER_TAB_NAMES[i]));
            tabSpec.setContent(factory);
            mMyorderTabHost.addTab(tabSpec);
        }
        //getNetDataForiconSwitch();

        mMyorderTabHost.setOnTabChangedListener(new MyOnTabChangeListener());
        mMyorderViewPage.addOnPageChangeListener(new MyOnPageChangeListener());
        mMyorderTabHost.setCurrentTab(1);
        mMyorderTabHost.setCurrentTab(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenManager.getScreenManager().popActivity(BaseWebViewActivity.class);
        mAdapter.notifyDataSetChanged();
    }

    public void initView() {
        mMyorderViewPage = (ViewPager) findViewById(R.id.myorder_viewPage);
        mMyorderTabHost = (TabHost) findViewById(R.id.myorder_tab_host);
        LinLyout_myorder_back = (LinearLayout) findViewById(R.id.LinLyout_myorder_back);
    }


    private void getisshow() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());

        OkHttpClientManager.postAsyn(ZWConfig.Action_cleanRedShow,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PrecontractMyOrder.this,
                                "Network Operation failed, please try again later", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String respon) {
                        //mTv.setText(u);// 注意这里是UI线程
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            if (code.equals("200")) {
                                Log.d("hongdian ", "200: ");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(PrecontractMyOrder.this,
                                    "Network Operation failed, please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                , params);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getScreenManager().popActivity(this);
    }

    private View createTabView(int resId) {
        View vTab = getLayoutInflater().inflate(R.layout.tab_item_view, null);
        TextView vc = (TextView) vTab.findViewById(R.id.content_vt);
        vc.setText(resId);
        return vTab;
    }


    class MyFragmentPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mList;

        public MyFragmentPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size() == 0 ? 0 : mList.size();
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mMyorderTabHost.setCurrentTab(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

    class MyOnTabChangeListener implements TabHost.OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {
            for (int i = 0; i < ORDER_TAB_NAMES.length; ++i) {
                TextView tvTabTitle = (TextView) mMyorderTabHost.getTabWidget().getChildAt(i).findViewById(R.id.content_vt);
                int nColorText = getResources().getColor(R.color.btn_green_noraml);
                if (getResources().getString(ORDER_TAB_NAMES[i]).equals(tabId)) {
                    nColorText = getResources().getColor(R.color.holo_red_light);
                    mMyorderViewPage.setCurrentItem(i);
                    tvTabTitle.setTextColor(0xff26bdab);
                } else {
                    tvTabTitle.setTextColor(0xffa5a5a5);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new CounselorEvent());
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}


