/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.urgoo.message.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.urgoo.ScreenManager;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.account.activity.MyFragment;
import com.urgoo.business.BaseService;
import com.urgoo.client.R;
import com.urgoo.collect.activites.CollectFragment;
import com.urgoo.common.ZWConfig;
import com.urgoo.common.event.MessageEvent;
import com.urgoo.counselor.activities.FindCounselorFragment;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.live.activities.LiveFragment;
import com.urgoo.message.Constant;
import com.urgoo.message.EaseHelper;
import com.urgoo.plan.activities.PlanFragment;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.view.TabView;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyFragment.MessageFragmentCallback {
    protected static final String TAG = "MainActivity";
    public static final String EXTRA_TAB = "extra_tab";//需要跳转的tab
    public static final int TAB_COUNSELOR = 0;
    public static final int TAB_LIVE = 1;
    public static final int TAB_MY = 2;
    public static final int TAB_COLLECT = 3;
    public static final int TAB_PLAN = 4;

    /**
     * 上一次界面onSaveInstanceState之前的tab被选中的状态key和value
     */
    private static final String PRE_SELECTED = "pre_selected";
    private int indexSelected = -1;
    /**
     * Fragment的TAG 用于解决app内存被回收之后导致的fragment重叠问题
     */
    private static final String[] FRAGMENT_TAG = {
            FindCounselorFragment.class.getSimpleName(),
            LiveFragment.class.getSimpleName(),
            MyFragment.class.getSimpleName(),
            CollectFragment.class.getSimpleName(),
            PlanFragment.class.getSimpleName()
    };

    private FragmentManager fragmentManager;
    private FindCounselorFragment findCounselorFragment;
    private MyFragment myFragment;
    private LiveFragment liveFragment;
    private CollectFragment collectFragment;
    private PlanFragment planFragment;

    // --------- TabBar ---------------
    private TabView lastTab;// 上次选中的tab
    private TabView tabCounselor; // 寻找顾问
    private TabView tabLive; // 直播
    private TabView tabMy; // 我的
    private TabView tabCollect; // 收藏
    private TabView tabPlan; // 规划
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
//    private ZhiBoListManageActivity zhiBoListManageActivity;

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private View guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            EMClient.getInstance().logout(true);
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null && savedInstanceState.getInt(PRE_SELECTED, indexSelected) != -1) {
            // 读取上一次界面Save时候tab选中的状态
            indexSelected = savedInstanceState.getInt(PRE_SELECTED, indexSelected);
            findCounselorFragment = (FindCounselorFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
            liveFragment = (LiveFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
            myFragment = (MyFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[2]);
            collectFragment = (CollectFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[3]);
            planFragment = (PlanFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[4]);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (findCounselorFragment != null)
                transaction.hide(findCounselorFragment);
            if (liveFragment != null)
                transaction.hide(liveFragment);
            if (myFragment != null)
                transaction.hide(myFragment);
            if (collectFragment != null) {
                transaction.hide(collectFragment);
//                tabMy.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        collectFragment.getSelectRedCount();
//                    }
//                }, 1200);
            }
            if (planFragment != null)
                transaction.hide(planFragment);
            transaction.commitAllowingStateLoss();
        } else {
            indexSelected = getIntent().getIntExtra(EXTRA_TAB, 0);
        }
        tabPerformClick(indexSelected);
        EventBus.getDefault().register(this);

//
//        liveFragment = new LiveFragment();
//        myWebViewFragment = new MainWebViewFragment();
//        profileFragment = new ProfileFragment();
//        planFragment = new PlanFragment();

//
//        Bundle b = new Bundle();
//        b.putString(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_nosignsearchConsultant);
//        myWebViewFragment.setArguments(b);

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    /**
     * 接受消息显示红点
     *
     * @param event
     */
    public void onEventMainThread(MessageEvent event) {
        tabMy.setNewIndicator(true);
        if (myFragment.isVisible()) {
            myFragment.getSelectRedCount();
        }
    }

    @Override
    public void onUnreadMessageCallback(boolean isShow) {
        tabMy.setNewIndicator(isShow);

//        tabMy.setNewIndicator(isShow);
//        if (isShow) {
//            unreadService.setVisibility(View.VISIBLE);
//        } else {
//            unreadService.setVisibility(View.GONE);
//        }
    }

    /**
     * 初始化组件
     */
    public void initView() {
        guide = findViewById(R.id.img_guide);
        if (!SPManager.getInstance(this).getGuideBetterPostNever()) {
            guide.setVisibility(View.VISIBLE);
        }
        guide.setOnClickListener(this);
        tabCounselor = (TabView) findViewById(R.id.tab_counselor);
        tabCounselor.setTextAndDrawableTop(R.string.find_consultant, R.drawable.tab_counselor);
        tabCounselor.setOnClickListener(this);
        tabLive = (TabView) findViewById(R.id.tab_live);
        tabLive.setTextAndDrawableTop(R.string.live, R.drawable.tab_live);
        tabLive.setOnClickListener(this);
        tabMy = (TabView) findViewById(R.id.tab_my);
        tabMy.setTextAndDrawableTop(R.string.my, R.drawable.tab_my);
        tabMy.setOnClickListener(this);
        tabCollect = (TabView) findViewById(R.id.tab_collect);
        tabCollect.setTextAndDrawableTop(R.string.collect, R.drawable.tab_collect);
        tabCollect.setOnClickListener(this);
        tabPlan = (TabView) findViewById(R.id.tab_plan);
        tabPlan.setTextAndDrawableTop(R.string.plan, R.drawable.tab_plan);
        tabPlan.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getScreenManager().popActivity(this);
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
    }
//
//    /**
//     * 刷新申请与通知消息数
//     */
//    public void updateUnreadAddressLable() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
////					unreadAddressLable.setText(String.valueOf(count));
//                    //unreadAddressLable.setVisibility(View.VISIBLE);
//                    //杨德成 20160506 隐藏小秘书对应的红点
////                    unreadAddressLable.setVisibility(View.GONE);
//
//                } else {
//                    //unreadAddressLable.setVisibility(View.INVISIBLE);
//                    //杨德成 20160506 隐藏小秘书对应的红点
////                    unreadAddressLable.setVisibility(View.GONE);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 获取未读申请与通知消息
//     *
//     * @return
//     */
//    public int getUnreadAddressCountTotal() {
//        int unreadAddressCountTotal = 0;
//        return unreadAddressCountTotal;
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        setMainTab();
//        getStatus();
//        if (!isConflict && !isCurrentAccountRemoved) {
//            updateUnreadAddressLable();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        outState.putInt(PRE_SELECTED, indexSelected);
        super.onSaveInstanceState(outState);
    }


    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        JpushUtlis.setAlias(getApplicationContext(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int mI, String mS, Set<String> mSet) {
                android.util.Log.d("alias", "设置alias为 :  " + mS);
            }
        });
        isConflictDialogShow = true;
        EaseHelper.getInstance().logout(true, null);
        String st = getResources().getString(R.string.Logoff_notification);
        SPManager.getInstance(this).clearLoginInfo();
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {

            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        JpushUtlis.setAlias(getApplicationContext(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int mI, String mS, Set<String> mSet) {
                android.util.Log.d("alias", "设置alias为 :  " + mS);
            }
        });
        isAccountRemovedDialogShow = true;
        EMClient.getInstance().logout(true);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        SPManager.getInstance(this).clearLoginInfo();
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
            }

        }

    }

    /**
     * 自动点击指定的TAB
     *
     * @param indexSelected TAB下标
     */
    private void tabPerformClick(int indexSelected) {
        switch (indexSelected) {
            case TAB_COUNSELOR:
                tabCounselor.performClick();
                break;
            case TAB_LIVE:
                tabLive.performClick();
                break;
            case TAB_MY:
                tabMy.performClick();
                break;
            case TAB_COLLECT:
                tabCollect.performClick();
                break;
            case TAB_PLAN:
                tabPlan.performClick();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int tab = getIntent().getIntExtra(EXTRA_TAB, 0);
        if (tab != indexSelected) {
            tabPerformClick(tab);
        }
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "here");
//        if (profileFragment != null)
//            profileFragment.onActivityResult(requestCode, resultCode, data);
    }

    private static long backPressed;

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            backPressed = System.currentTimeMillis();
        }
    }

    private void switchTab(View v) {
        TabView tab = (TabView) v;
        tab.setTabEnable(false);
        if (lastTab != null) {
            lastTab.setTabEnable(true);
        }
        lastTab = tab;
    }

    private void delayLoadMessage() {
        if (!isFinishing()) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (isFinishing() || fragmentManager == null) {
                        return;
                    }
                    // 主动加载消息tab
                    if (myFragment == null) {
                        myFragment = new MyFragment();
                        myFragment.setMessageFragmentCallback(MainActivity.this);
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_container, myFragment, MyFragment.class.getSimpleName());
                        transaction.hide(myFragment);
                        transaction.commitAllowingStateLoss();
                    }
                }
            }, 1000);
        }
    }

    //杨德成20160801 获取ZOOM房间;接受或拒绝顾问端发起的视频邀请 0:用户未操作，1：接受，2：拒绝
    private void getZOOMInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("termType", "2");
        OkHttpClientManager.postAsyn(ZWConfig.Action_getZoomRoom,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        //UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        try {
                            JSONObject jsonObject = new JSONObject(respon);
                            NetHeaderInfoEntity hentity = BaseService.getNetHeadInfo(jsonObject);
                            if (hentity.getCode().equals("200")) {
                                String zoomId = new JSONObject(new JSONObject(jsonObject.get("body").toString()).getString("zoomInfo")).getString("zoomId");
                                String nickname = new JSONObject(new JSONObject(jsonObject.get("body").toString()).getString("zoomInfo")).getString("nickname");
                                String pic = new JSONObject(new JSONObject(jsonObject.get("body").toString()).getString("zoomInfo")).getString("pic");
                                String zoomNo = new JSONObject(new JSONObject(jsonObject.get("body").toString()).getString("zoomInfo")).getString("zoomNo");
                                String status = new JSONObject(new JSONObject(jsonObject.get("body").toString()).getString("zoomInfo")).getString("status");
                                if (!status.equals("2")) {
                                    Bundle extras = new Bundle();
                                    extras.putString("icon", pic);
                                    extras.putString("name", nickname);
                                    extras.putString("zoomId", zoomId);
                                    extras.putString("zoomNo", zoomNo);
                                    Util.openActivityWithBundle(MainActivity.this, UrgooVideoActivity.class, extras);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , params);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.img_guide:
                guide.setVisibility(View.GONE);
                SPManager.getInstance(this).setGuideBetterPostNever();
                break;
            case R.id.tab_counselor:
                switchTab(v);
                if (findCounselorFragment == null) {
                    findCounselorFragment = new FindCounselorFragment();
                    transaction.add(R.id.fragment_container, findCounselorFragment, FindCounselorFragment.class.getSimpleName());
                    if (Util.isNetworkAvailable(getApplicationContext()))
                        delayLoadMessage();
                } else {
                    transaction.show(findCounselorFragment);
                }
                if (indexSelected != TAB_COUNSELOR) {
                    // 隐藏上次展示的fragment
                    transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TAG[indexSelected]));
                }
                transaction.commitAllowingStateLoss();
                // 更新当前展示的下标
                indexSelected = TAB_COUNSELOR;
                getZOOMInfo();
                break;
            case R.id.tab_live:
                switchTab(v);
                if (liveFragment == null) {
                    liveFragment = new LiveFragment();
                    transaction.add(R.id.fragment_container, liveFragment, LiveFragment.class.getSimpleName());
                } else {
                    transaction.show(liveFragment);
                }
                if (indexSelected != TAB_LIVE) {
                    // 隐藏上次展示的fragment
                    transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TAG[indexSelected]));
                }
                transaction.commitAllowingStateLoss();
                // 更新当前展示的下标
                indexSelected = TAB_LIVE;
                getZOOMInfo();
                break;
            case R.id.tab_my:
                switchTab(v);
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    myFragment.setMessageFragmentCallback(this);
                    transaction.add(R.id.fragment_container, myFragment, MyFragment.class.getSimpleName());
                } else {
                    transaction.show(myFragment);
                }
                if (indexSelected != TAB_MY) {
                    // 隐藏上次展示的fragment
                    transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TAG[indexSelected]));
                }
                transaction.commitAllowingStateLoss();
                // 更新当前展示的下标
                indexSelected = TAB_MY;
                getZOOMInfo();
                break;
            case R.id.tab_collect:
                switchTab(v);
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    transaction.add(R.id.fragment_container, collectFragment, CollectFragment.class.getSimpleName());
                } else {
                    transaction.show(collectFragment);
                }
                if (indexSelected != TAB_COLLECT) {
                    // 隐藏上次展示的fragment
                    transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TAG[indexSelected]));
                }
                transaction.commitAllowingStateLoss();
                // 更新当前展示的下标
                indexSelected = TAB_COLLECT;
                getZOOMInfo();
                break;
            case R.id.tab_plan:
                switchTab(v);
                if (planFragment == null) {
                    planFragment = new PlanFragment();
                    transaction.add(R.id.fragment_container, planFragment, PlanFragment.class.getSimpleName());
                } else {
                    transaction.show(planFragment);
                }
                if (indexSelected != TAB_PLAN) {
                    // 隐藏上次展示的fragment
                    transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TAG[indexSelected]));
                }
                transaction.commitAllowingStateLoss();
                // 更新当前展示的下标
                indexSelected = TAB_PLAN;
                getZOOMInfo();
                break;
            default:
                break;
        }
    }
}
