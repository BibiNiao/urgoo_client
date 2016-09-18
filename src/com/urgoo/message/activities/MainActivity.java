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

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.shrb.hrsdk.HRSDK;
import com.urgoo.ScreenManager;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.common.event.MessageEvent;
import com.urgoo.data.SPManager;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.main.activities.CounselorFragment;
import com.urgoo.message.Constant;
import com.urgoo.message.EaseHelper;
import com.urgoo.profile.activities.ProfileFragment;
import com.urgoo.service.activities.PlanFragment;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.urgoo.webviewmanage.MainWebViewFragment;
import com.urgoo.zhibo.activities.ZhiBoListManageActivity;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity implements ProfileFragment.MessageFragmentCallback {
    protected static final String TAG = "MainActivity";
    public static final String EXTRA_TAB = "extra_tab";//需要跳转的tab
    // 未读通讯录textview
    private TextView unreadAddressLable;

    private TextView[] mTabs;
    private CounselorFragment counselorFragment;

    //    private CounselorInfoFragment counselorInfoFragment;
    private ZhiBoListManageActivity zhiBoListManageActivity;
    private MainWebViewFragment myWebViewFragment;
    private ProfileFragment profileFragment;
    private PlanFragment serviceFragment;
    //    private AssiatantFragment assiatantFragment;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    int status = 0;
    //  private Fragment[] fragments;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    //    private boolean isLogin = false;
//    private boolean isThree = false;
    private ImageView unreadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        SPManager.getInstance(this).setMyStatus("-1");

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
        setContentView(R.layout.em_activity_main);
        initView();
        EventBus.getDefault().register(this);

        zhiBoListManageActivity = new ZhiBoListManageActivity();
        counselorFragment = new CounselorFragment();
        myWebViewFragment = new MainWebViewFragment();
        profileFragment = new ProfileFragment();
        serviceFragment = new PlanFragment();

        profileFragment.setMessageFragmentCallback(this);

        Bundle b = new Bundle();
        b.putString(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_nosignsearchConsultant);
        myWebViewFragment.setArguments(b);

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        //华润银行
//        approveDev();
    }

    /**
     * 接受消息显示红点
     *
     * @param event
     */
    public void onEventMainThread(MessageEvent event) {
        unreadService.setVisibility(View.VISIBLE);
        if (profileFragment.isVisible()) {
            profileFragment.getSelectRedCount();
        }
    }

    @Override
    public void onUnreadMessageCallback(boolean isShow) {
        if (isShow) {
            unreadService.setVisibility(View.VISIBLE);
        } else {
            unreadService.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化组件
     */
    public void initView() {
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        unreadService = (ImageView) findViewById(R.id.unread_service);
        mTabs = new TextView[4];

        Drawable drawable;
        mTabs[0] = (TextView) findViewById(R.id.btn_address_list);
        drawable = getResources().getDrawable(R.drawable.em_tab_contact_list_bg1);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[0].setCompoundDrawables(null, drawable, null, null);

        mTabs[1] = (TextView) findViewById(R.id.btn_conversation);
        drawable = getResources().getDrawable(R.drawable.em_tab_chat_bg);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[1].setCompoundDrawables(null, drawable, null, null);

        mTabs[2] = (TextView) findViewById(R.id.btn_service);
        drawable = getResources().getDrawable(R.drawable.urgoo_tab_service_bg);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[2].setCompoundDrawables(null, drawable, null, null);

        mTabs[3] = (TextView) findViewById(R.id.btn_setting);
        drawable = getResources().getDrawable(R.drawable.em_tab_profile_bg);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[3].setCompoundDrawables(null, drawable, null, null);
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        if (fragments.size() == 0) {
            return;
        }
        switch (view.getId()) {

            case R.id.btn_address_list:
                index = 0;
                break;
            case R.id.btn_conversation:
                index = 1;
                break;

            case R.id.btn_service:
                index = 2;
                break;
            case R.id.btn_setting:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                if (!fragments.get(i).isAdded()) {
                    trx.add(R.id.fragment_container, fragments.get(i));
                }
                trx.hide(fragments.get(i));
            }
//            if (!fragments[index].isAdded()) {
//                trx.add(R.id.fragment_container, fragments[index]);
//            }
            trx.show(fragments.get(index)).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPManager.getInstance(this).setMyStatus("-1");
        ScreenManager.getScreenManager().popActivity(this);
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
                    //unreadAddressLable.setVisibility(View.VISIBLE);
                    //杨德成 20160506 隐藏小秘书对应的红点
                    unreadAddressLable.setVisibility(View.GONE);

                } else {
                    //unreadAddressLable.setVisibility(View.INVISIBLE);
                    //杨德成 20160506 隐藏小秘书对应的红点
                    unreadAddressLable.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        return unreadAddressCountTotal;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMainTab();
//        getStatus();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadAddressLable();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int tab = getIntent().getIntExtra(EXTRA_TAB, 0);
        if (tab != currentTabIndex) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments.get(currentTabIndex));
            trx.show(fragments.get(tab)).commit();
            mTabs[currentTabIndex].setSelected(false);
            mTabs[tab].setSelected(true);
            currentTabIndex = tab;
        }

        SPManager.getInstance(this).setMyStatus("-1");
        setMainTab();
//        getStatus();
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    /**
     * 设置首页
     */
    private void setMainTab() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!counselorFragment.isAdded() || !zhiBoListManageActivity.isAdded() || !profileFragment.isAdded() || !serviceFragment.isAdded()) {
            ft.add(R.id.fragment_container, profileFragment)
                    .add(R.id.fragment_container, zhiBoListManageActivity)
                    .add(R.id.fragment_container, counselorFragment)
                    .add(R.id.fragment_container, serviceFragment)
                    .hide(counselorFragment).hide(zhiBoListManageActivity).hide(profileFragment).hide(serviceFragment);
            ft.show(counselorFragment).commit();
        }
        fragments.add(counselorFragment);
        fragments.add(zhiBoListManageActivity);
        fragments.add(serviceFragment);
        fragments.add(profileFragment);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //getMenuInflater().inflate(R.menu.context_tab_contact, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "here");
        if (profileFragment != null)
            profileFragment.onActivityResult(requestCode, resultCode, data);
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
}
