package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.Interface.Constants;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

/**
 * Created by Administrator on 2016/7/12.
 */
public class UrgooVideoActivity extends BaseActivity implements Constants, ZoomSDKInitializeListener, MeetingServiceListener {


    private RelativeLayout re_Refuse, re_accept;

    private final static String TAG = "Zoom SDK Example";

    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;

    private final static int STYPE = MeetingService.USER_TYPE_ZOOM;
    private final static String DISPLAY_NAME = "ZoomUS SDK";

    private SimpleDraweeView iv_avatar;

    ProgressDialog pd;

    private String icon = "";
    private String name = "";
    private String hxCode = "";
    private String zoomId = "";
    private String zoomNo = "";

    private TextView tv_name;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        if (arg0 == null) {
            ZoomSDK sdk = ZoomSDK.getInstance();
            sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
            sdk.setGoogleDriveClientId(this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }
        setContentView(R.layout.urgoovideoactivity_layout);


        initView();

        initData();

        pd = new ProgressDialog(UrgooVideoActivity.this);

    }

    @Override
    public void initView() {


        tv_name = (TextView) findViewById(R.id.tv_name);

        re_Refuse = (RelativeLayout) findViewById(R.id.re_Refuse);
        re_Refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getZoomUserOpt("2");
            }
        });


        re_accept = (RelativeLayout) findViewById(R.id.re_accept);
        re_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getZoomUserOpt("1");

            }
        });

        iv_avatar = (SimpleDraweeView) findViewById(R.id.img_avatar);

    }


    @Override
    public void initData() {

        icon = getIntent().getStringExtra("icon");
        name = getIntent().getStringExtra("name");
        //hxCode=getIntent().getStringExtra("hxCode");
        zoomId = getIntent().getStringExtra("zoomId");
        ;
        zoomNo = getIntent().getStringExtra("zoomNo");

        refreshView();

    }

    public void refreshView() {

        tv_name.setText(name);
        iv_avatar.setImageURI(Uri.parse(icon));

    }

    @Override
    public void onMeetingEvent(int i, int i1, int i2) {

    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        //Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();

            registerMeetingServiceListener();
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
     * 接受或拒绝顾问端发起的视频邀请
     *
     * @param opStatus 0:用户未操作，1：接受，2：拒绝
     */
    private void getZoomUserOpt(final String opStatus) {
        if (opStatus.equals("1")) {
            pd.setTitle("正在进入视频聊天....");
            pd.show();
        }
        name = spManager.getNickName();
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
        //params.put("token", "p2OdthB5P+A=");
        params.put("opStatus", opStatus);
        params.put("termType", "2");

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
                                    //getZoomNo(HXCode);
                                    MyJoinInstantMeeting(zoomNo, name);
                                } else if (opStatus.equals("2")) {
                                    UiUtil.show(UrgooVideoActivity.this, "已拒绝");
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

    /**
     * @param HXCode
     */
    private void getZoomNo(String HXCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
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
                                    MyJoinInstantMeeting(zoomNo, name);

                                } else {

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
        if (code != 0) {
            Toast.makeText(getApplicationContext(), "Request failed",
                    Toast.LENGTH_SHORT).show();
            pd.dismiss();
            finish();


        }
        Log.d("meeting3", "code->" + code);
        pd.dismiss();
    }
}
