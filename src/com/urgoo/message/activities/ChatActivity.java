package com.urgoo.message.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.util.EasyUtils;
import com.urgoo.Interface.Constants;
import com.urgoo.base.ActivityBase;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.DataUtil;
import com.urgoo.common.FirstEvent;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.AdvanceRelationEntity;
import com.urgoo.domain.UserInfoEntity;
import com.urgoo.message.ExpansionInterface;
import com.urgoo.net.EventCode;
import com.urgoo.schedule.activites.Precontract;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;
import com.urgoo.schedule.activites.PrecontractPingjia;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

/**
 * 聊天页面，需要fragment的使用{@link EaseChatFragment}
 */
public class ChatActivity extends FragmentActivity implements ExpansionInterface, Constants, ZoomSDKInitializeListener, MeetingServiceListener {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;


    //杨德成 20160622
    private AdvanceRelationEntity entity;
    private int type = 0;
    CountdownUtil c;

    private String show;


    //杨德成 20160623

    private String[] Reason = new String[]{"很适合，希望进一步了解", "再看看其他顾问", "不适合"};
    private String reasonStr = Reason[0];
    private String prarentHXCode = "";
    private String advanceId = "";
    private String counselorId = "";

    ProgressDialog pd;

    String toZoomChatUsername;
    ZoomSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        pd = new ProgressDialog(ChatActivity.this);
        sdk = ZoomSDK.getInstance();
        if (savedInstanceState == null) {
            sdk.initialize(ChatActivity.this, APP_KEY, APP_SECRET, WEB_DOMAIN, ChatActivity.this);
            sdk.setDropBoxAppKeyPair(ChatActivity.this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(ChatActivity.this, ONEDRIVE_CLIENT_ID);
            sdk.setGoogleDriveClientId(ChatActivity.this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString("userId");
        //可以直接new EaseChatFratFragment使用
        chatFragment = new ChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        chatFragment.setCallBackInterface(ChatActivity.this);

        // 杨德成 20160617 注册EventBus
        EventBus.getDefault().register(this);

        //isAdvanceRelationCoun(toChatUsername);


    }

    public void onEventMainThread(FirstEvent event) {
        //UiUtil.show(ChatActivity.this,  "视频结束");
        showEvaluationDialog();
    }

    /**
     * ZOOM创建聊天室
     */
    private void createZoomChat(final String host_id, String userHxCode, final String zoomNo) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("userHxCode", userHxCode);
        params.put("zoomNo", zoomNo);
        if (!TextUtils.isEmpty(SPManager.getInstance(this).getZoomId())) {
            params.put("zoomChatId", SPManager.getInstance(this).getZoomId());
        }

        OkHttpClientManager.postAsyn(ZWConfig.URL_requestCreateZoomChat, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {
                pd.dismiss();
                UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
            }

            @Override
            public void onResponse(String response) {
                android.util.Log.d("聊天室ID", response);
                try {
                    String code = new JSONObject(new JSONObject(response).get("header").toString()).getString("code");
                    String message = new JSONObject(new JSONObject(response).get("header").toString()).getString("message");
                    if (code.equals("200")) {
                        JSONObject jsonObject = new JSONObject(new JSONObject(response).getString("body"));
                        SPManager.getInstance(ChatActivity.this).setZoomId(jsonObject.getString("zoomChatId"));
                        startMeeting(host_id, zoomNo, "99");
                    } else if (code.equals("404")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                    if (code.equals("400")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                }
            }
        }, params);
    }

    /**
     * 开启会议
     *
     * @param usetid
     * @param roomNo
     * @param userType
     */
    public void startMeeting(String usetid, String roomNo, String userType) {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        //int  ret=meetingService.startMeeting(this,"56e9k_riRXGYoxEYTaak9g", Constants.ZOOM_TOKEN,Integer.parseInt(userType),roomNo, "ydc");
        int ret = meetingService.startMeeting(this, usetid, Constants.ZOOM_TOKEN, Integer.parseInt(userType), roomNo, toZoomChatUsername);
        pd.dismiss();

    }

    /**
     * 创建聊天室
     *
     * @param host_id
     */
    private void creatMeeting(final String host_id) {
        pd.setTitle("正在开启视频聊天....");
        pd.show();
        Map<String, String> params = new HashMap<>();
//        params.put("api_key", Constants.APP_KEY);
//        params.put("api_secret", Constants.APP_SECRET);
        params.put("api_key", "BcxlISZCSbeiIlaZyKtfUw");
        params.put("api_secret", "IOj0tAavRAILySyh1oM3WpYh3cUhGfBUgn3e");
        params.put("host_id", host_id);
        params.put("topic", "meet123");
        params.put("type", "2");

        OkHttpClientManager.postAsyn(ZWConfig.URL_requestCreateMeeting,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {
                        pd.dismiss();
                        UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        android.util.Log.d("获取房间号", respon);
                        try {
                            JSONObject jsonObject = new JSONObject(respon);
                            String roomId = jsonObject.getString("id");
                            createZoomChat(host_id, toChatUsername, roomId);

                        } catch (JSONException e) {
                            pd.dismiss();
                            UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                        }
                    }
                }, params);
    }

    @Override
    protected void onDestroy() {

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);
        }
        super.onDestroy();
        activityInstance = null;


    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void SettingAvatarAndnickname(String mgs, TextView name, ImageView avatar, EMConversation conversation) {

    }

    @Override
    public void SettingChatnickname(String mgs, final TextView name, String conversationId) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", SPManager.getInstance(this).getToken());
        maps.put("userHxCodeString", conversationId);
        maps.put("termType", "1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_getAvatarAndnickname, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {
                UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");

            }

            @Override
            public void onResponse(String response) {
                //d("ProfileFragment", response);
                JSONObject jsonObject = null;
                JSONArray ja = null;
                Map<String, String> maps = new HashMap<String, String>();
                try {
                    String code = new JSONObject(new JSONObject(response).get("header").toString()).getString("code");
                    String message = new JSONObject(new JSONObject(response).get("header").toString()).getString("message");
                    if (code.equals("200")) {
                        jsonObject = new JSONObject(response).getJSONObject("body");
                        ja = jsonObject.getJSONArray("userInfoList");
                        UserInfoEntity user = GsonTools.getTargetClass(ja.getJSONObject(0).toString(), UserInfoEntity.class);
                        name.setText(user.getEnName());
                        //ImageLoaderUtil.displayImage(ZWConfig.URGOOURL_BASE + user.getUserIcon(),avatar);

                        //getIsAdvanceRelation("",toChatUsername);
                    } else if (code.equals("404")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                    if (code.equals("400")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                }
            }
        }, maps);
    }

    @Override
    public void SettingChatAvatar(String mgs, TextView name, final ImageView avatar, String conversationId) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", SPManager.getInstance(this).getToken());
        maps.put("userHxCodeString", conversationId);
        maps.put("termType", "1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_getAvatarAndnickname, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {
                UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
            }

            @Override
            public void onResponse(String response) {
                // d("ProfileFragment", response);
                JSONObject jsonObject = null;
                JSONArray ja = null;
                Map<String, String> maps = new HashMap<String, String>();
                try {
                    String code = new JSONObject(new JSONObject(response).get("header").toString()).getString("code");
                    String message = new JSONObject(new JSONObject(response).get("header").toString()).getString("message");
                    if (code.equals("200")) {
                        jsonObject = new JSONObject(response).getJSONObject("body");
                        ja = jsonObject.getJSONArray("userInfoList");
                        UserInfoEntity user = GsonTools.getTargetClass(ja.getJSONObject(0).toString(), UserInfoEntity.class);
                        //name.setText(user.getEnName());
                        //ImageLoaderUtil.displayImage(ZWConfig.URGOOURL_BASE + user.getUserIcon(), avatar);
                        if (user.getUserIcon() != null) {
                            imageLoadBusiness.imageLoadByNewURL(user.getUserIcon(), avatar);
                        }
                    } else if (code.equals("404")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                    if (code.equals("400")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                }
            }
        }, maps);
    }

//    @Override
//    public void SettingChatTitle(final EaseTitleBar titleBar, final String conversationId) {
//        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("token", SPManager.getInstance(this).getToken());
//        maps.put("userHxCodeString", conversationId);
//        maps.put("termType", "1");
//
//        OkHttpClientManager.postAsyn(ZWConfig.Action_getAvatarAndnickname, new OkHttpClientManager.ResultCallback<String>() {
//
//            @Override
//            public void onError(Call call, Exception e) {
//                UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
//            }
//
//            @Override
//            public void onResponse(String response) {
//                //d("ProfileFragment", response);
//                JSONObject jsonObject = null;
//                JSONArray ja = null;
//                Map<String, String> maps = new HashMap<String, String>();
//                try {
//                    String code = new JSONObject(new JSONObject(response).get("header").toString()).getString("code");
//                    String message = new JSONObject(new JSONObject(response).get("header").toString()).getString("message");
//                    if (code.equals("200")) {
//                        jsonObject = new JSONObject(response).getJSONObject("body");
//                        android.util.Log.d("me008", jsonObject.toString());
//                        ja = jsonObject.getJSONArray("userInfoList");
//                        UserInfoEntity user = GsonTools.getTargetClass(ja.getJSONObject(0).toString(), UserInfoEntity.class);
//                        toZoomChatUsername = user.getEnName();
//                        //titleBar.setTitle(user.getEnName());
//                        if (conversationId.equals(ZWConfig.ACTION_CustomerService)) {
//                            // UiUtil.show(ChatActivity.this,  "客服id: "+conversationId);
//                            //titleBar.setTitle(ZWConfig.ACTION_CustomerServiceNickname);
//                            chatFragment.tv_message_title.setText(ZWConfig.ACTION_CustomerServiceNickname);
////                            chatFragment.ll_youbao.setVisibility(View.GONE);
//                        } else {
//                            //UiUtil.show(ChatActivity.this,  "对方环信id: "+conversationId);
//                            if (user.getRoleTyp().equals("1")) {
//                                //UiUtil.show(ChatActivity.this,  "正和顾问聊天");
//                                //titleBar.setTitle(user.getEnName());
//                                // titleBar.setTitle(user.getEnName());
//                                chatFragment.tv_message_title.setText(user.getEnName());
//                            } else if (user.getRoleTyp().equals("2")) {
//                                //UiUtil.show(ChatActivity.this,  "正和家长聊天");
//                                chatFragment.tv_message_title.setText(user.getNickName());
//                                //titleBar.setTitle(user.getNickName());
//                            } else if (user.getRoleTyp().equals("3")) {
//                                // titleBar.setTitle(user.getCnName());
//                                chatFragment.tv_message_title.setText(user.getCnName());
//                            }
//                            //titleBar.setTitle(user.getEnName());
//                            chatFragment.tv_message_title.setText(user.getEnName());
//
////                            chatFragment.ll_youbao.setVisibility(View.VISIBLE);
//
//                            chatFragment.ll_youbao.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//                                    //startActivity(new Intent(ChatActivity.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
//
//                                    Intent intent = new Intent(ChatActivity.this, ChatActivity.class);
//                                    intent.putExtra("userId", ZWConfig.ACTION_CustomerService);
//
//                                    overridePendingTransition(0, 0);
//                                    finish();
//                                    overridePendingTransition(0, 0);
//
//                                    startActivity(intent);
//
//                                }
//                            });
//                        }
//                        //ImageLoaderUtil.displayImage(ZWConfig.URGOOURL_BASE + user.getUserIcon(),avatar);
//
//                        setTime();
//                        getIsAdvanceRelation("", toChatUsername);
//
//
//                    } else if (code.equals("404")) {
//                        UiUtil.show(ChatActivity.this, message);
//                    }
//                    if (code.equals("400")) {
//                        UiUtil.show(ChatActivity.this, message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
//                }
//            }
//        }, maps);
//    }

    public void SettingChatAvatarClick(String conversationId) {
        //UiUtil.show(ChatActivity.this,  "测试点击");
        //UiUtil.show(ChatActivity.this,  "对方环信id: "+conversationId);
        getUserInfo(conversationId);
    }

    /**
     * 杨德成 20160516 根据环信id获取个人信息
     *
     * @param conversationId
     */
    private void getUserInfo(final String conversationId) {

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", SPManager.getInstance(this).getToken());
        maps.put("userHxCodeString", conversationId);
        maps.put("termType", "1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_getAvatarAndnickname, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                //d("ProfileFragment", response);
                JSONObject jsonObject = null;
                JSONArray ja = null;
                Map<String, String> maps = new HashMap<String, String>();
                try {
                    String code = new JSONObject(new JSONObject(response).get("header").toString()).getString("code");
                    String message = new JSONObject(new JSONObject(response).get("header").toString()).getString("message");
                    if (code.equals("200")) {
                        jsonObject = new JSONObject(response).getJSONObject("body");
                        ja = jsonObject.getJSONArray("userInfoList");
                        UserInfoEntity user = GsonTools.getTargetClass(ja.getJSONObject(0).toString(), UserInfoEntity.class);
                        //Log.d("test300",ja.toString());
                        //Log.d("mytest20160516","SettingChatTitle->ja:"+ja.toString());
                        if (conversationId.equals(ZWConfig.ACTION_CustomerService)) {
                        } else {
                            //UiUtil.show(ChatActivity.this,  "对方环信id: "+conversationId);
                            //角色说明：1：表示顾问用户，2:表示家长用户
                            if (user.getRoleTyp().equals("1")) {
                                //UiUtil.show(ChatActivity.this,  "正和顾问聊天");
                                Intent it = new Intent(ChatActivity.this, BaseWebViewActivity.class);
                                String strURL = ZWConfig.Action_clientprofile + "counselorId=" + user.getUserId() + "&chattype=100";
                                it.putExtra("EXTRA_URL", strURL);
                                startActivity(it);
                            } else if (user.getRoleTyp().equals("2")) {
                                //UiUtil.show(ChatActivity.this,  "正和家长聊天");
                                String strURL = ZWConfig.Action_studentInfoPage + "chattype=100";
                                Intent it = new Intent(ChatActivity.this, BaseWebViewActivity.class);
                                it.putExtra("EXTRA_URL", strURL);
                                startActivity(it);
                            }
                        }
                    } else if (code.equals("404")) {
                        UiUtil.show(ChatActivity.this, message);

                    }
                    if (code.equals("400")) {
                        UiUtil.show(ChatActivity.this, message);
                    }
                    // getIsAdvanceRelation("",toChatUsername);
                } catch (JSONException e) {
                    e.printStackTrace();
                    UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                }
            }
        }, maps);
    }


    //杨德成 20160622 倒计时控制
    private void getIsAdvanceRelation(String token, final String hxCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("hxCode", hxCode);
        //params.put("termType", "1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_isAdvanceRelation,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");

                            android.util.Log.d("uuu", "info->" + j.toString());
                            if (code.equals("200")) {
                                //UiUtil.show(ChatActivity.this, message);
                                String jsonObject = new JSONObject(respon).getJSONObject("body").getString("isAdvanceRelation");
                                entity = GsonTools.getTargetClass(jsonObject, AdvanceRelationEntity.class);
                                advanceId = entity.getAdvanceId();
                                counselorId = entity.getCounselorId();

                                //isAdvanceRelation:1显示预约详情 0显示立即预约
                                //status:1表示没有倒计时而且预约详情接口为未确认的详情接口  2表示有倒计时而且预约详情为确认的详情接口 0:表示没有关系
                                if (entity.getIsAdvanceRelation().equals("1") && !hxCode.equals(ZWConfig.ACTION_CustomerService)) {
                                    /*chatFragment.ll_send.setVisibility(View.VISIBLE);
                                    chatFragment.tv_rightcontent.setText("预约详情");*/
                                } else if (entity.getIsAdvanceRelation().equals("0") && !hxCode.equals(ZWConfig.ACTION_CustomerService)) {
//                                    chatFragment.ll_send.setVisibility(View.VISIBLE);
                                    chatFragment.tv_rightcontent.setText("预约");
                                } else {
//                                    chatFragment.ll_send.setVisibility(View.GONE);
                                }


                                //0 没有   1 有
                                /*if (entity != null) {
                                    if (entity.getNotime().equals("0")) {
                                        chatFragment.ll_send.setVisibility(View.GONE);
                                    } else if (entity.getNotime().equals("1")) {
                                        chatFragment.ll_send.setVisibility(View.VISIBLE);
                                    }
                                }*/
                                chatFragment.ll_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //UiUtil.show(ChatActivity.this,  "跳转详情");

                                        //isAdvanceRelation:1显示预约详情 0显示立即预约
                                        //status:1表示没有倒计时而且预约详情接口为未确认的详情接口  2表示有倒计时而且预约详情为确认的详情接口 0:表示没有关系

                                        if (entity.getIsAdvanceRelation().equals("1")) {

                                            Intent intent2 = new Intent(ChatActivity.this, PrecontractMyOrderContent.class);
                                            intent2.putExtra("status", entity.getStatus());
                                            intent2.putExtra("counselorId", entity.getCounselorId());
                                            intent2.putExtra("advanceId", entity.getAdvanceId());
                                            startActivity(intent2);
                                            finish();
                                        } else if (entity.getIsAdvanceRelation().equals("0")) {

                                            //android.util.Log.d("uuu","info->"+j.toString());
                                            Intent intent = new Intent(ChatActivity.this, Precontract.class);
                                            intent.putExtra("counselorId", entity.getCounselorId());
                                            intent.putExtra("advanceId", entity.getAdvanceId());
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });

                                if (entity.getShow().equals("1")) {

                                    getZoomNo(toChatUsername);
                                    /*chatFragment.rl_Schedule2.setVisibility(View.VISIBLE);
                                    chatFragment.ll_Joining.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           //UiUtil.show(ChatActivity.this, "joining-test");
                                            //MyJoinInstantMeeting(zoomNo, name);

                                            getZoomUserOpt("1",toChatUsername);
                                        }
                                    });

                                    chatFragment.ll_Reject.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //UiUtil.show(ChatActivity.this, "reject-test");
                                            getZoomUserOpt("2",toChatUsername);
                                        }
                                    });*/
                                }


                            } else if (code.equals("404")) {
                                UiUtil.show(ChatActivity.this, message);

                            }
                            if (code.equals("400")) {
                                UiUtil.show(ChatActivity.this, message);
                            }

                        } catch (JSONException e) {
                            UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }

    @Override
    public void onMeetingEvent(int meetingEvent, int errorCode,
                               int internalErrorCode) {
       /* if (meetingEvent == MeetingEvent.MEETING_CONNECT_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        } else if (meetingEvent == MeetingEvent.MEETING_CONNECTED) {
            //发送视频邀请
            chatFragment.sendTextMessage("Connecting");
        }*/
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
            registerMeetingServiceListener();
            //getZoomAccount();
        }
    }

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    /**
     * 倒计时
     */
    public class CountdownUtil {
        private long time;
        TextView counetdownView;
        CountdownThread thread;
        SimpleDateFormat formatter;
        String hms;

        /**
         * @time:时间差(指定的一段时间长),时间戳
         * @counetdownView：TextView显示倒计时
         */
        public CountdownUtil(long time, TextView counetdownView) {
            this.time = time;
            this.counetdownView = counetdownView;
        }

        /**
         * 倒计时
         */
        public void countdown() {
            formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT +8:00"));//设置时区(北京),如果你不设置这个,你会发现你的时间总会多出来8个小时

            thread = new CountdownThread(time, 1000);// 构造CountDownTimer对象
            thread.start();
        }

        class CountdownThread extends CountDownTimer {
            public CountdownThread(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("mytest20160615","info->"+millisUntilFinished);
                hms = formatter.format(millisUntilFinished);//转化成  "00:00:00"的格式
                //hms = formatTime(millisUntilFinished);//转化成  "00:00:00"的格式
                //counetdownView.setText(hms);
                counetdownView.setText(DataUtil.formatDuring(Integer.parseInt(millisUntilFinished / 1000 + "")));
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                //倒计时结束时触发
                //type="100";
                chatFragment.ll_video.setBackgroundColor(0xFF26bdab);
                chatFragment.ll_voice.setBackgroundColor(0xFF26bdab);
                type = 1;
                counetdownView.setText("00:00:00");
                //tv_operating_title.setText("Send Alert");
                //im_icon.setVisibility(View.GONE);
            }
        }

        /**
         * 终止线程
         */
        public void stopThread() {
            thread.cancel();
        }
    }

    private void setTime() {
        /*
                        long time=System.currentTimeMillis();


                        final Calendar mCalendar=Calendar.getInstance();
                        mCalendar.setTimeInMillis(time);
                        int mHour=mCalendar.get(Calendar.HOUR);
                        int mMinuts=mCalendar.get(Calendar.MINUTE);
                        String _time=mHour+":"+mMinuts;*/


        /*Calendar cal = Calendar.getInstance();
        cal.getTimeInMillis();
        //北京时区GMT+8
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.clear();
        //beijingcal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        beijingcal.setTimeZone(TimeZone.getTimeZone("GMT"));
        beijingcal.setTimeInMillis(cal.getTimeInMillis());
        DateFormat fmt = new SimpleDateFormat("HH:mm");
        String beijingFormatStr = fmt.format(beijingcal.getTime());*/

        /*Calendar cal = Calendar.getInstance(Locale.CHINA);
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        DateFormat fmt = new SimpleDateFormat("HH:mm");
        String beijingFormatStr = fmt.format(cal.getTime());*/


        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("foo:" + foo.format(new Date()));

        Calendar gc = GregorianCalendar.getInstance();
        System.out.println("gc.getTime():" + gc.getTime());
        System.out.println("gc.getTimeInMillis():" + new Date(gc.getTimeInMillis()));

        //当前系统默认时区的时间：
        Calendar calendar = new GregorianCalendar();
        System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
        System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        //美国洛杉矶时区
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        //时区转换
        calendar.setTimeZone(tz);
        System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
        System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        Date time = new Date();

        //1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();

        //2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        //3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        //之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        System.out.println("UTC:" + new Date(cal.getTimeInMillis()));

        DateFormat fmt = new SimpleDateFormat("HH:mm");
        String beijingFormatStr = fmt.format(new Date(cal.getTimeInMillis() - 4 * 3600000));


        chatFragment.tv_time.setText(beijingFormatStr);

    }


    /**
     * 杨德成 20160623 评论对话框
     */
    private void showEvaluationDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(ChatActivity.this).create();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.schedulevaluationdialog_layout, null);
        dlg.setView(layout);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.schedulevaluationdialog_layout);
        // 为确认按钮添加事件,执行退出应用操作


        final TextView tv_ReasonFirst = (TextView) window.findViewById(R.id.tv_ReasonFirst);
        final LinearLayout ll_ReasonFirst = (LinearLayout) window.findViewById(R.id.ll_ReasonFirst);


        final EditText edit_message = (EditText) window.findViewById(R.id.edit_message);


        final LinearLayout ll_ReasonSecond = (LinearLayout) window.findViewById(R.id.ll_ReasonSecond);
        final TextView tv_ReasonSecond = (TextView) window.findViewById(R.id.tv_ReasonSecond);

        final LinearLayout ll_ReasonThird = (LinearLayout) window.findViewById(R.id.ll_ReasonThird);
        final TextView tv_ReasonThird = (TextView) window.findViewById(R.id.tv_ReasonThird);

        ll_ReasonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_ReasonThird.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonThird.setTextColor(0xFF26bdab);

                ll_ReasonSecond.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonSecond.setTextColor(0xFF26bdab);

                ll_ReasonFirst.setBackgroundResource(R.drawable.linearlayoutframe_selected);
                tv_ReasonFirst.setTextColor(0xFFffffff);

                reasonStr = Reason[0];
                //UiUtil.show(ScheduleDetailNotAcceptActivity.this,Reason[0]);
                //reason=Reason[0];
            }
        });


        ll_ReasonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_ReasonFirst.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonFirst.setTextColor(0xFF26bdab);

                ll_ReasonThird.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonThird.setTextColor(0xFF26bdab);

                ll_ReasonSecond.setBackgroundResource(R.drawable.linearlayoutframe_selected);
                tv_ReasonSecond.setTextColor(0xFFffffff);

                reasonStr = Reason[1];
            }
        });


        ll_ReasonThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_ReasonFirst.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonFirst.setTextColor(0xFF26bdab);

                ll_ReasonSecond.setBackgroundResource(R.drawable.linearlayoutframe_normal);
                tv_ReasonSecond.setTextColor(0xFF26bdab);


                ll_ReasonThird.setBackgroundResource(R.drawable.linearlayoutframe_selected);
                tv_ReasonThird.setTextColor(0xFFffffff);
                reasonStr = Reason[2];

                android.util.Log.d("yy", "onClick: " + Reason[2]);
            }
        });

        RelativeLayout iv_cancel = (RelativeLayout) window.findViewById(R.id.ll_title);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {


                dlg.cancel();
                NetCloseReschedule(advanceId);
            }
        });

        LinearLayout ll_Submit = (LinearLayout) window.findViewById(R.id.ll_Submit);
        ll_Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dlg.cancel();

                //UiUtil.show(ScheduleDetailNotAcceptActivity.this, reasonStr);
                String message = edit_message.getText().toString();
                NetEvaluation(advanceId, reasonStr, message);

            }
        });
    }


    //视频结束评论络操作
    private void NetEvaluation(final String advanceId, String reason, String comment) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("token", SPManager.getInstance(this).getToken());
        params.put("advanceId", advanceId);
        params.put("commentTitle", reason);
        params.put("comment", comment);

        OkHttpClientManager.postAsyn(ZWConfig.Action_advanceCommentClient,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                //UiUtil.show(ChatActivity.this, message);

                                Intent intent = new Intent(ChatActivity.this, PrecontractPingjia.class);
                                intent.putExtra("counselorId", counselorId);
                                startActivity(intent);

                                NetCloseReschedule(advanceId);


                                //finish();


                            } else if (code.equals("404")) {
                                UiUtil.show(ChatActivity.this, message);

                            }
                            if (code.equals("400")) {
                                UiUtil.show(ChatActivity.this, message);

                            }


                        } catch (JSONException e) {
                            UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }

    //视频结束评论络操作
    private void NetCloseReschedule(final String advanceId) {

        //Log.d("gggxxx","advanceId:"+advanceId+"commentTitle:"+ reason+"comment:"+comment);
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("advanceId", advanceId);
        OkHttpClientManager.postAsyn(ZWConfig.Action_updateAdvanceStatus,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                UiUtil.show(ChatActivity.this, message);
                                //finish();
                            } else if (code.equals("404")) {
                                UiUtil.show(ChatActivity.this, message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(ChatActivity.this, message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");
                        }
                    }
                }
                , params);
    }


    //发起视频前的网络请求
    private void NetStartAdvanceReschedule(final String advanceId, final int flag) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("advanceId", advanceId);
        OkHttpClientManager.postAsyn(ZWConfig.Action_startAdvance,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ChatActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                // UiUtil.show(ChatActivity.this, message);

                                //0表示发起视频 1表示发起语音
                                if (flag == 0) {
                                    //chatFragment.myStartVideoCall();
//                                    getZoomAccount();
                                    creatMeeting(SPManager.getInstance(ChatActivity.this).getHostId());
                                } else {
//                                    chatFragment.myStartVoiceCall();
                                }
                                //finish();

                            } else if (code.equals("404")) {
                                UiUtil.show(ChatActivity.this, message);
                                android.util.Log.d("uuu", "404 message: " + message);

                            }
                            if (code.equals("400")) {
                                UiUtil.show(ChatActivity.this, message);
                                android.util.Log.d("uuu", "400 message: " + message);
                            }


                        } catch (JSONException e) {
                            UiUtil.show(ChatActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }


    private void isAdvanceRelationCoun(String hxCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("hxCode", hxCode);
        OkHttpClientManager.postAsyn(ZWConfig.Action_isAdvanceRelation,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("555666", "getNetDataForupdate->" + response);

                            JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                            show = jsonObject.getString("show");
                            getZoomNo(toChatUsername);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param HXCode
     */
    private void getZoomNo(String HXCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("userHxCode", HXCode);
        OkHttpClientManager.postAsyn(ZWConfig.Action_selectZoomChatClientAndConsult,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {
                        Log.d("555", "getNetDataForupdate->" + respon);
                        JSONObject j;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {
                                JSONObject jsonObject = new JSONObject(j.get("body").toString());
                                String status = jsonObject.getString("status");
                                final String zoomNo = jsonObject.getString("zoomNo");
                                final String name = jsonObject.getString("name");

                                if (status.equals("STARTED")) {

                                    chatFragment.rl_Schedule2.setVisibility(View.VISIBLE);
                                    chatFragment.ll_Joining.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //UiUtil.show(ChatActivity.this, "joining-test");
                                            //MyJoinInstantMeeting(zoomNo, name);

                                            getZoomUserOpt("1", toChatUsername, zoomNo, name);
                                        }
                                    });

                                    chatFragment.ll_Reject.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //UiUtil.show(ChatActivity.this, "reject-test");
                                            getZoomUserOpt("2", toChatUsername, zoomNo, name);
                                        }
                                    });

                                   /* //MyJoinInstantMeeting(meetingNo);
                                    Log.d("tv","ZoomNo:"+zoomNo);
                                    chatFragment.rl_Schedule2.setVisibility(View.VISIBLE);
                                    chatFragment.ll_Joining.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UiUtil.show(ChatActivity.this, "joining-test");
                                            MyJoinInstantMeeting(zoomNo, name);
                                        }
                                    });

                                    chatFragment.ll_Reject.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UiUtil.show(ChatActivity.this, "reject-test");
                                        }
                                    });*/
                                } else {
                                    chatFragment.rl_Schedule2.setVisibility(View.GONE);
                                }
                            } else if (code.equals("404")) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), message,
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code.equals("400")) {
                                Toast.makeText(getApplicationContext(), message,
                                        Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            //UiUtil.show(ScheduleDetailActivity.this,  "Data parsing errors, please sign in again");
                            pd.dismiss();
                        }
                    }
                }

                , params);
    }


    /**
     * 接受或拒绝顾问端发起的视频邀请
     *
     * @param opStatus 0:用户未操作，1：接受，2：拒绝
     * @param HXCode
     */
    private void getZoomUserOpt(final String opStatus, final String HXCode, final String zoomNo, final String name) {
        if (opStatus.equals("1")) {
            pd.setTitle("正在进入视频聊天....");
            pd.show();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("userHxCode", HXCode);
        params.put("opStatus", opStatus);
        OkHttpClientManager.postAsyn(ZWConfig.Action_zoomUserOpt,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {
                        Log.d("55577", "getNetDataForupdate->" + respon);
                        JSONObject j;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            if (code.equals("200")) {

                                if (opStatus.equals("1")) {

                                    MyJoinInstantMeeting(zoomNo, name);
                                    //getZoomNo(HXCode);
                                } else if (opStatus.equals("2")) {
                                    UiUtil.show(ChatActivity.this, "已经拒绝");
                                    finish();
                                }

                            } else if (code.equals("404")) {

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), message,
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code.equals("400")) {
                                Toast.makeText(getApplicationContext(), message,
                                        Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            //UiUtil.show(ScheduleDetailActivity.this,  "Data parsing errors, please sign in again");
                            pd.dismiss();
                        }
                    }
                }

                , params);
    }

    public void MyJoinInstantMeeting(String meetingNo, String clientName) {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }
        MeetingService meetingService = zoomSDK.getMeetingService();
        int code = meetingService.joinMeeting(this, meetingNo, clientName);
        Log.d("meeting3", "code->" + code);
        pd.dismiss();
    }
}
