/**
 *
 */
package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.base.HomeFragment;
import com.urgoo.business.BaseService;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.common.event.MessageEvent;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.message.EaseHelper;
import com.urgoo.net.EventCode;
import com.urgoo.profile.biz.ProfileManager;
import com.urgoo.schedule.activites.PrecontractMyOrder;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.PickUtils;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;
import okhttp3.Call;


/**
 * @author A18ccms a18ccms_gmail_com
 * @ClassName: ProfileFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年3月22日 下午1:01:33
 */
public class ProfileFragment extends HomeFragment implements OnClickListener {
    /**
     * 退出按钮
     */
    private RelativeLayout logoutBtn;
    private RelativeLayout re_wanshan_ziliaos;
    private RelativeLayout I_sign_up;
    private RelativeLayout myMessage;

    RelativeLayout re_serviceargeement, re_help, re_wanshan_yuyue;

    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;

    private TextView txt_username;
    private TextView txt_phone;
    private String username;
    private String phone;
    private LinearLayout ll_Follow, order_ll, account_ll;
    private ImageView img_verified, iv_notification_icon;
    private TextView tv_fixed, iv_avatar2, experienceTv;
    private ImageView ivMessageRed;

    private SimpleDraweeView iv_avatar;
    private MessageFragmentCallback callback;
    private boolean isShowRed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.minefragment_layout, null);
        initView();
        getUserInfo();
        getSelectRedCount();
        return view;
    }

    public void getSelectRedCount(){
        ProfileManager.getInstance(getActivity()).getSelectRedCount(this);
    }

    private void invokeUnreadCallback() {
        if (callback != null) {
            callback.onUnreadMessageCallback(isShowRed);
        }
    }

    /**
     * 刷新未读消息数量
     */
    public interface MessageFragmentCallback {
        void onUnreadMessageCallback(boolean isShow);
    }

    public void setMessageFragmentCallback(MessageFragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeSelectRedCount:
                try {
                    int count = Integer.parseInt(new JSONObject(result.get("body").toString()).getString("count")); //消息中心红点
                    int advanceCount = Integer.parseInt(new JSONObject(result.get("body").toString()).getString("advanceCount")); //预约红点
                    int allCount = Integer.parseInt(new JSONObject(result.get("body").toString()).getString("allCount")); //预约红点
                    if (allCount > 0) {
                        isShowRed = true;
                    } else {
                        isShowRed = false;
                    }
                    invokeUnreadCallback();
                    if (advanceCount > 0) {
                        iv_notification_icon.setVisibility(View.VISIBLE);
                    } else {
                        iv_notification_icon.setVisibility(View.GONE);
                    }
                    if (count > 0) {
                        ivMessageRed.setVisibility(View.VISIBLE);
                    } else {
                        ivMessageRed.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSelectRedCount();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //UiUtil.show(getActivity(), "隐藏");
        } else {
            getSelectRedCount();
            //notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
           /* Intent it= new Intent(getActivity(), UrgooVideoActivity.class);
            it.putExtra("icon", "");
            it.putExtra("name", "");
            it.putExtra("hxCode", "");
            startActivity(it);*/
            //getData();
            //UiUtil.show(getActivity(), "显示");
            //UiUtil.show(getActivity(),"显示");
            getZOOMInfo();
        }
    }

    //杨德成20160801 获取ZOOM房间;接受或拒绝顾问端发起的视频邀请 0:用户未操作，1：接受，2：拒绝
    private void getZOOMInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(getActivity()).getToken());
        //params.put("token", "p2OdthB5P+A=");
        params.put("termType", "2");
        OkHttpClientManager.postAsyn(ZWConfig.Action_getZoomRoom,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        //UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            android.util.Log.d("test123", j.toString());
                            NetHeaderInfoEntity hentity = BaseService.getNetHeadInfo(j);
                            if (hentity.getCode().equals("200")) {

                                String zoomId = new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomId");
                                String nickname = new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("nickname");
                                String pic = new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("pic");
                                String zoomNo = new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomNo");
                                String status = new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("status");
                                //notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
                                if (!status.equals("2")) {
//                                    if (!APPManagerTool.isActivityRunning(getContext().getApplicationContext(), "UrgooVideoActivity")) {
//                                        Intent it = new Intent(getActivity(), UrgooVideoActivity.class);
//                                        it.putExtra("icon", pic);
//                                        it.putExtra("name", nickname);
//                                        it.putExtra("zoomId", zoomId);
//                                        it.putExtra("zoomNo", zoomNo);
//                                        //it.putExtra("hxCode", "");
//                                        startActivity(it);
//                                    }
                                }

                            } else if (hentity.getCode().equals("404")) {
                                // UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());
                            }
                            if (hentity.getCode().equals("400")) {
                                //UiUtil.show(ZhiBodDetailActivity.this,hentity.getMessage());
                            }
                        } catch (JSONException e) {
                            // UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");
                        }
                    }
                }
                , params);
    }

    protected void initView() {
        re_help = $(view, R.id.re_help);
        img_verified = $(view, R.id.img_verified);
        tv_fixed = $(view, R.id.tv_fxid);
        iv_avatar2 = $(view, R.id.iv_avatar2);
        order_ll = $(view, R.id.order_ll);
        account_ll = $(view, R.id.account_ll);
        experienceTv = $(view, R.id.exprience_tv);
        re_wanshan_ziliaos = $(view, R.id.re_wanshan_ziliaos);
        re_wanshan_yuyue = $(view, R.id.re_wanshan_yuyue);
        ivMessageRed = (ImageView) view.findViewById(R.id.iv_message_red);
        myMessage = (RelativeLayout) view.findViewById(R.id.my_message);
        myMessage.setOnClickListener(this);
        order_ll.setOnClickListener(this);
        account_ll.setOnClickListener(this);
        re_help.setOnClickListener(this);
        iv_avatar = (SimpleDraweeView) view.findViewById(R.id.img_avatar);
        re_wanshan_ziliaos.setOnClickListener(this);
        re_wanshan_yuyue.setOnClickListener(this);
        iv_avatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PickUtils.doPickPhotoAction(getActivity());
            }

        });

        ll_Follow = (LinearLayout) view.findViewById(R.id.ll_Follow);
        ll_Follow.setOnClickListener(this);
        $(view, R.id.re_wode_yyue).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePwdActivity.class));
            }
        });

//        rl_switch_notification = (RelativeLayout) view.findViewById(R.id.rl_switch_notification);
        I_sign_up = (RelativeLayout) view.findViewById(R.id.I_sign_up);

        I_sign_up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivitiesActivity.class));
            }
        });
        iv_notification_icon = (ImageView) view.findViewById(R.id.iv_notification_icons);


        logoutBtn = (RelativeLayout) view.findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ProfileFragment", "onactivity");
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PickUtils.PHOTO_PICKED_WITH_DATA: // 调用Gallery返回的
                    PickUtils.doCropPhoto(getActivity(), data.getData());
                    Log.d("图片路径", String.valueOf(data.getData()));
                    break;
                case PickUtils.CAMERA_WITH_DATA: // 照相机程序返回的
                    PickUtils.rotatePhotoAndSave(ZWConfig.pickPicture);
                    PickUtils.doCropPhoto(getActivity(), Uri.fromFile(new File(ZWConfig.pickPicture)));  //如果不裁剪及不需要调用该方法
                    break;
                case PickUtils.PHOTO_CROP: //图片裁剪操作
                    iv_avatar.setImageURI(ZWConfig.tempPicture);
                    PickUtils.uploadImage(getActivity(), ZWConfig.tempPicture.getPath());
                    break;
            }
        }
    }

    public Handler filehandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d("BasicInformationActivit", "msg.what:" + msg.what);
            switch (msg.what) {
                case MSGTYPE_QBINTERACT_SUCC:
                    try {
                        Bundle bundle = msg.getData();
                        String res = bundle.getString(ZWConfig.NET_RESPONSE);
                        JSONObject jsonObject = new JSONObject(res).getJSONObject("body");
                        iv_avatar.setImageURI(Uri.parse(jsonObject.getString("userIcon")));
//                        getUserInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        UiUtil.show(getActivity(), e.toString());
                        UiUtil.show(getActivity(), "请检查网络或稍后重试");
                    }
                    break;
                case MSGTYPE_QB_FAIL: {
                    UiUtil.show(getActivity(), "请检查网络或稍后重试");
                    break;
                }
                default: {
                    super.handleMessage(msg);
                    break;
                }
            }
        }

    };

    private void getUserInfo() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", SPManager.getInstance(getActivity()).getToken() == null ? "" : SPManager.getInstance(getActivity()).getToken());
        OkHttpClientManager.postAsyn(ZWConfig.ACTION_selectParentInfo,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("ProfileFragment", response);
                        JSONObject jsonObject = null;
                        Map<String, String> maps = new HashMap<String, String>();
                        try {
                            jsonObject = new JSONObject(response).getJSONObject("body").getJSONObject("parentInfo");

                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String next = keys.next();
                                maps.put(next, jsonObject.getString(next));
                            }
                            //  img_verified.setVisibility("1".equals(maps.get("status")) ? View.VISIBLE : View.GONE);
//                    tv_fixed.setText(maps.get("nickName"));
//                    experienceTv.setText(maps.get("grade"));
//                    iv_avatar2.setText(maps.get("countryName"));
                            if (maps.get("nickName") != null) {
                                tv_fixed.setText(maps.get("nickName"));
                            } else
                                tv_fixed.setText("");
                            if (maps.get("grade") != null) {
                                experienceTv.setText(maps.get("grade"));
                            } else
                                experienceTv.setText("");
                            if (maps.get("countryName") != null) {
                                iv_avatar2.setText(maps.get("countryName"));
                            } else
                                iv_avatar2.setText("");
                            if (!TextUtils.isEmpty(jsonObject.optString("userIcon"))) {
                                //ImageLoaderUtil.displayImage(ZWConfig.URGOOURL_BASE + jsonObject.optString("userIcon"), iv_avatar);
                                //   ImageLoaderUtil.displayImage("http://115.28.50.163:8080/urgoo/upload/1460539388664.png", iv_avatar);
//                        imageLoadBusiness.imageLoadByNewURL(jsonObject.getString("userIcon"), iv_avatar);
                                iv_avatar.setImageURI(Uri.parse(jsonObject.getString("userIcon")));
//                        imageLoadBusiness.imageLoadByNewURL(jsonObject.getString("userIcon"), iv_avatar);
                            }

                        } catch (JSONException e) {
                            Log.d("JSONException", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, maps);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.my_message:
                Util.openActivity(getActivity(), MessageActivity.class);
                break;
            case R.id.btn_logout: //退出登陆
                logout();
                break;
            case R.id.re_wanshan_ziliaos:
                Intent it = new Intent(getActivity(), BaseWebViewActivity.class);
                it.putExtra("EXTRA_URL", ZWConfig.ACTION_myInfo);
                startActivity(it);
                break;
            // 预约
            case R.id.account_ll:
//                intent = new Intent(getActivity(), BaseWebViewActivity.class);
//                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentAccount);
//                startActivity(intent);
                startActivity(new Intent(getActivity(), PrecontractMyOrder.class));
                break;
            case R.id.order_ll:
                intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentOrder);
                startActivity(intent);
                break;
            case R.id.re_help:
                intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.Action_helpJz);
                startActivity(intent);
                break;


            // 关注的顾问
            case R.id.ll_Follow:
//                intent = new Intent(getActivity(), BaseWebViewActivity.class);
//                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_searchConsultant);
//                startActivity(intent);
//                startActivity(new Intent(getActivity(), PrecontractMyOrder.class));

                break;
            case R.id.re_wanshan_yuyue:
                startActivity(new Intent(getActivity(), QrcodeActivity.class));
                break;


            default:
                break;
        }

    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        EaseHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        JpushUtlis.setAlias(getActivity(), "", new TagAliasCallback() {
                            @Override
                            public void gotResult(int mI, String mS, Set<String> mSet) {
                                android.util.Log.d("alias", "设置alias为 :  " + mS);
                            }
                        });
                        // 重新显示登陆页面
                        SPManager.getInstance(getActivity()).clearLoginInfo();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });
    }


    private void iconSwitch() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(getActivity()).getToken() == null ? "" : SPManager.getInstance(getActivity()).getToken());
        params.put("termType", "2");

        OkHttpClientManager.postAsyn(ZWConfig.URL_RED,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();

                        android.util.Log.d("hong", "2: ");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        try {
                            j = new JSONObject(respon);
                            android.util.Log.d("hong ", "onResponse: j" + j.toString());
                            String count = new JSONObject(j.get("body").toString()).getString("showRed");
                            String s = new JSONObject(count).getString("isShow");
                            android.util.Log.d("hong", s);
                            if (Integer.parseInt(s) > 0) {
                                iv_notification_icon.setVisibility(View.VISIBLE);
                            } else {
                                iv_notification_icon.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                , params);
    }

}

