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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.shrb.hrsdk.HRSDK;
import com.urgoo.ScreenManager;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.client.Constant;
import com.urgoo.client.DemoHelper;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;

import com.urgoo.data.SPManager;
import com.urgoo.db.InviteMessgeDao;
import com.urgoo.db.UserDao;
import com.urgoo.domain.InviteMessage;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.main.activities.CounselorFragment;
import com.urgoo.profile.activities.ProfileFragment;
import com.urgoo.service.activities.PlanFragment;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.urgoo.webviewmanage.MainWebViewFragment;
import com.urgoo.zhibo.activities.ZhiBoListManageActivity;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity {
    protected static final String TAG = "MainActivity";
    public static final String EXTRA_TAB = "extra_tab";//需要跳转的tab
    // 未读通讯录textview
    private TextView unreadAddressLable;

    private Button[] mTabs;
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
    private BroadcastReceiver internalDebugReceiver;
//    private ConversationListFragment conversationListFragment;
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

    String openID;
    String personUnionID;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        SPManager.getInstance(this).setMyStatus("-1");
        ZWConfig.isLogin = DemoHelper.getInstance().isLoggedIn();
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(true, null);
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }
        setContentView(R.layout.em_activity_main);
        initView();

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            JpushUtlis.setAlias(getApplicationContext(), "", new TagAliasCallback() {
                @Override
                public void gotResult(int mI, String mS, Set<String> mSet) {
                    android.util.Log.d("alias", "设置alias为 :  " + mS);
                }
            });
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
//        conversationListFragment = new ConversationListFragment();
        zhiBoListManageActivity = new ZhiBoListManageActivity();
        counselorFragment = new CounselorFragment();
        myWebViewFragment = new MainWebViewFragment();
        profileFragment = new ProfileFragment();
        serviceFragment = new PlanFragment();

        Bundle b = new Bundle();
        b.putString(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_nosignsearchConsultant);
        myWebViewFragment.setArguments(b);
        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //内部测试方法，请忽略
        //registerInternalDebugReceiver();

//        approveDev();
        EMLog.d(TAG, "width:" + getScreenWidth(this) + "  height:" + getScreenHeight(this));
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 验证开发者
     */
    private void approveDev() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
                android.util.Log.e("responseMap:", responseMap.toString());
                getUserIDs();
            }
        };
        String random = "123456";
        HRSDK.approveDev("AFC90B23F5C533130EAC39713D1740BC", random, handler);
    }

    /**
     * 获取ID
     */
    private void getUserIDs() {
        personUnionID = "";
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
                if (responseMap.get("returnCode").equals("000000")) {
                    openID = responseMap.get("openID").toString();
                    if (responseMap.get("personUnionID") != null) {
                        personUnionID = responseMap.get("personUnionID").toString();
                        android.util.Log.e("personUnionID:", personUnionID);
                    }
                    orderPay();
                }
                android.util.Log.e("responseMap:", responseMap.toString());
                android.util.Log.e("openID:", openID);
            }
        };

        HRSDK.Users.getUserIDs("1616646", handler);
    }

    /**
     * 绑定卡
     */
    private void orderPay() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
//                payID = responseMap.get("payID").toString();
                android.util.Log.e("orderPay", responseMap.toString());
            }
        };
        Map callbackMap = new HashMap();
        Map autoFillMap = new HashMap();
//        autoFillMap.put("mobile", "18310839846");//弱实名手机号
//        autoFillMap.put("realName", "刘汉明");//真实姓名
//        autoFillMap.put("cardNo", "620904789023452324");//银行卡号
//        autoFillMap.put("identity", "42070419900611503X");//证件号码
//        autoFillMap.put("revmobile", "15692125542");//银行预留手机号
        String mchName = "手机商城";
        String mchID = "SYT002";
        String outTradeNo = "123456789123000";
        String body = "Android LE 1s 手机";
        String detail = "Android LE 1s 手机，￥1099，超薄";
        String confirmOrder = "N";
        String attach = "attach附加数据";
        String totalFee = "88";
        String paidAmount = "50";
        String unpaidAmount = "25";
        String limitPay = "01";
        String feeType = "CNY";
        String goodsTag = "WXG";
        String timeValid = "120";
        String deviceInfo = "34234234";
        String random = "123456";
        String sign = "2537C258F911DDA060F5547A32A12992";
        HRSDK.Pay.orderPay(openID, personUnionID, mchName, mchID, outTradeNo, body, detail, random, sign, attach, confirmOrder,
                totalFee, paidAmount, unpaidAmount, limitPay, feeType, goodsTag, timeValid, deviceInfo, autoFillMap, callbackMap, MainActivity.this, handler);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        mTabs = new Button[4];

        Drawable drawable;
        mTabs[0] = (Button) findViewById(R.id.btn_address_list);
        drawable = getResources().getDrawable(R.drawable.em_tab_contact_list_bg1);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[0].setCompoundDrawables(null, drawable, null, null);

        mTabs[1] = (Button) findViewById(R.id.btn_conversation);
        drawable = getResources().getDrawable(R.drawable.em_tab_chat_bg);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[1].setCompoundDrawables(null, drawable, null, null);

        mTabs[2] = (Button) findViewById(R.id.btn_service);
        drawable = getResources().getDrawable(R.drawable.urgoo_tab_service_bg);
        drawable.setBounds(0, 0, Util.dp2px(MainActivity.this, 20), Util.dp2px(MainActivity.this, 20));
        mTabs[2].setCompoundDrawables(null, drawable, null, null);

        mTabs[3] = (Button) findViewById(R.id.btn_setting);
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

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

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
    public void back(View view) {
        super.back(view);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                   /* if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }*/
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
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
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
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
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;


    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        DemoHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        updateUnreadAddressLable();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ZWConfig.isLogin = DemoHelper.getInstance().isLoggedIn();
        setMainTab();
//        getStatus();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
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
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
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
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
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
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
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
        ZWConfig.isLogin = DemoHelper.getInstance().isLoggedIn();
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            JpushUtlis.setAlias(getApplicationContext(), "", new TagAliasCallback() {
                @Override
                public void gotResult(int mI, String mS, Set<String> mSet) {
                    android.util.Log.d("alias", "设置alias为 :  " + mS);
                }
            });
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
