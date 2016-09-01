package com.urgoo.schedule.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.Interface.Constants;
import com.urgoo.base.ActivityBase;
import com.urgoo.business.BaseService;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.DataUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.schedule.activites.Bean.advanceDetail;
import com.urgoo.schedule.activites.Bean.advanceDetailTime;
import com.urgoo.schedule.activites.biz.PrecontentManager;
import com.urgoo.view.CircleImageView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.Util;
import com.zw.express.tool.image.ImageLoaderUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

/**
 * Created by duanfei on 2016/6/15.
 */

public class PrecontractMyOrderContent extends ActivityBase implements View.OnClickListener, Constants, ZoomSDKInitializeListener, MeetingServiceListener {

    public static final String TAG = "mtag";
    private Button but_order_content_cancelmake;//取消_取消
    private Button but_order_content_jieshou;  // 接收预约  取消预约
    private TextView tv_order_content_username;
    private TextView tv_order_content_workyear;
    private TextView tv_order_content_goodfield;
    private TextView tv_order_content_maketimes;
    private TextView tv_order_content_countdown;
    private TextView tv_order_content_times;
    private TextView tv_order_content_timesTWO;
    private TextView tv_order_content_times_USA;
    private TextView tv_order_content_times_USATWO;
    private TextView tv_order_content_times2;
    private TextView tv_order_content_times2TWO;
    private TextView tv_order_content_times_USA2;
    private TextView tv_order_content_times_USA2TWO;
    private TextView tv_order_content_times3;
    private TextView tv_pingjia;
    private TextView tv_order_content_times3TWO;
    private TextView tv_order_content_times_USA3;
    private TextView tv_order_content_times_USA3TWO;
    private TextView tv_order_content_coun;
    private TextView tv_order_content_title;
    private TextView tv_order_content_content;
    private TextView tv_order_content_countriesname;
    private EditText ed_order_content_message;
    private RelativeLayout RelLayout_order_content_immediately_now;//立即预约_立即预约
    private RelativeLayout RelLayout_order_content_now; //立即预约_立即联系
    private RelativeLayout RelLayout_order_content_immediately_cancel; //立即联系_立即联系
    private RelativeLayout RelLayout_order_content_immediately_evaluation_left; //评价_评价
    private RelativeLayout RelLayout_order_content_immediately_evaluation_right; //评价_立即聘用
    private RelativeLayout RelLayout_order_content_jieshou;  // 接收预约  接收预约按钮
    private RelativeLayout RelLayout_order_content_immediately;//取消_立即联系
    private LinearLayout Relat_order_content_time;
    private LinearLayout Relat_order_content_time2;
    private LinearLayout Relat_order_content_time3;
    private LinearLayout Relat_order_content_time2TWO;
    private LinearLayout Relat_order_content_time3TWO;
    private LinearLayout Linear_order_content_pingjia;
    private LinearLayout Linear_order_content_RetLayout_jieshou;// 接收预约
    private LinearLayout RelativeLayouts;    //其他显示
    private LinearLayout RelativeLayoutsTWO;//待确定显示
    private LinearLayout Linear_order_content_RetLayout_bottom;//取消
    private LinearLayout Linear_order_content_RetLayout_bottom_evaluation;//评价
    private LinearLayout Linear_order_content_RetLayout_bottom_cancel;//立即联系
    private LinearLayout Linear_order_content_RetLayout_bottom_now;//立即预约
    private LinearLayout LinLyout_myorder_back;//回退按钮
    private Handler mHandler;
    //杨德成 201607 1表示倒计时结束
    private static int HANDLER = 0;
    private static int HANDLERS = 1;
    private CircleImageView img_order_content_usericon;
    private int figs = 0;
    private int status;
    private int mInt = 0;
    // 获取到的数据
    private String statu;
    private String types;
    private String advanceId;
    private String haveComment;
    private String comment;
    private String star;
    private RatingBar ratingBar_z1_score;
    private ImageView im_cont_TWO2;
    private ImageView im_cont_TWO;
    private ImageView im_cont;
    private ImageView im_cont2;
    private ImageView img_oo;
    private ImageView img_oo2;
    private ImageView img_oo3;
    private ImageView img_cont_guwen;
    private advanceDetail mTobeData;
    private advanceDetail mComBean;
    private advanceDetail mConBean;
    private ArrayList<advanceDetailTime> mTobeList = new ArrayList<>();
    private ArrayList<advanceDetailTime> mComList = new ArrayList<>();
    private ArrayList<advanceDetailTime> mDetailTimeBean = new ArrayList<>();


    //杨德成 20160727 添加zoom视频聊天
    private RelativeLayout rl_zoomship;
    ProgressDialog pd;

    String toZoomChatUsername;
    ZoomSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_myorder_content);

        getZOOMInfo();

        sdk = ZoomSDK.getInstance();
        if (savedInstanceState == null) {
            sdk.initialize(PrecontractMyOrderContent.this, APP_KEY, APP_SECRET, WEB_DOMAIN, PrecontractMyOrderContent.this);
            sdk.setDropBoxAppKeyPair(PrecontractMyOrderContent.this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(PrecontractMyOrderContent.this, ONEDRIVE_CLIENT_ID);
            sdk.setGoogleDriveClientId(PrecontractMyOrderContent.this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }


        final Intent mIntent = getIntent();
        if (!Util.isEmpty(mIntent.getStringExtra("status")) && !Util.isEmpty(mIntent.getStringExtra("advanceId"))) {
            statu = mIntent.getStringExtra("status");
            advanceId = mIntent.getStringExtra("advanceId");
            types = mIntent.getStringExtra("type");
        } else {
            Toast.makeText(PrecontractMyOrderContent.this, "网络繁忙，请稍后2秒再试！", Toast.LENGTH_SHORT).show();
            finish();
        }
        getNetDataForupdate(statu);
        initView();    //初始化
        initOnclick();


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initDataS(msg);
            }
        };
    }

    private void initDataS(Message msg) {
        if (msg.what == HANDLER) {

            Log.d(TAG, "handleMessage: " + msg.what);
            if (mTobeData != null) {
                status = mTobeData.getStatus();
            }
            if (mConBean != null) {
                status = mConBean.getStatus();
            }
            if (mComBean != null) {
                status = mComBean.getStatus();
            }
            switch (status) {
                case 1://待确定
                    figs = 1;
                    Log.d(TAG, "handleMessage：     待确定 ");
                    if (types != null) {
                        if (types.equals("1")) {
                            ifLayoutone();      //取消预约和立即联系
                        }
                        if (types.equals("2")) {
                            ifLayoutones();     //取消预约和接受预约
                        }
                    }
                    break;
                case 2://已确定
                    figs = 2;
                    Log.d(TAG, "handleMessage:     已确认");
                    if (mConBean != null) {
                        if (mConBean.getDaojishi() != null) {
                            if (Long.parseLong(mConBean.getDaojishi()) > 0) {
                                ifLayoutoness();
                            } else
                                ifLayouttwo();
                        }
                        tv_order_content_title.setText("已确认");
                        tv_order_content_content.setText("顾问已接受你的预约，请准时与顾问联系～");
                        mTobeList.clear();
                    }
                    break;
                case 3://顾问拒绝
                case 4://家长取消
                    if (mComBean != null) {
                        tv_order_content_title.setText(mComBean.getState());
                        if (mComBean.getReason() != null) {
                            tv_order_content_content.setText(mComBean.getReason());
                        } else
                            tv_order_content_content.setText("时间冲突");
                        if (mComBean.getIsAdvanceRelation() != null) {
                            if (mComBean.getIsAdvanceRelation().equals("0")) {
                                ifLayouttherr();
                            } else if (mComBean.getIsAdvanceRelation().equals("1")) {
                                ifLayouttwos();
                            }
                        }
                    }
                    tv_order_content_timesTWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    tv_order_content_times_USATWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    tv_order_content_times2TWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    tv_order_content_times_USA2TWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    tv_order_content_times3TWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    tv_order_content_times_USA3TWO.setTextColor(getResources().getColor(R.color.b_b7b7b7));
                    break;
                case 5://正常关闭
                    if (haveComment != null) {
                        if (haveComment.equals("0")) {
                            tv_order_content_title.setText("已完成");
                            tv_order_content_content.setText("视频预约已结束，快给顾问一个评价吧～");
                            Linear_order_content_pingjia.setVisibility(View.GONE);
                            ifLayout5();
                        } else if (haveComment.equals("1")) {
                            //设置星星评价
                            Linear_order_content_pingjia.setVisibility(View.VISIBLE);
                            tv_pingjia.setText(comment);
                            ratingBar_z1_score.setRating(Float.parseFloat(star));
                            tv_order_content_title.setText("已完成");
                            tv_order_content_content.setText("感谢你的评价～");
                            ifLayouttherr();
                        }
                    }
                    break;
            }
            initData();
        }
        if (msg.what == HANDLERS) {
            mInt--;
            if (mInt >= 0) {
                tv_order_content_countdown.setText("" + DataUtil.formatDuring(mInt));
                Message message = mHandler.obtainMessage(HANDLERS);
                mHandler.sendMessageDelayed(message, 1000);
            } else {
                rl_zoomship.setVisibility(View.VISIBLE);
                tv_order_content_countdown.setText("00:00:00");
                getZOOMInfo();

            }
        }
    }

    @Override
    protected void onDestroy() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);
        }
        super.onDestroy();
    }

    private void initOnclick() {
        LinLyout_myorder_back.setOnClickListener(this);
        RelLayout_order_content_now.setOnClickListener(this);//立即预约_立即联系
        RelLayout_order_content_immediately_now.setOnClickListener(this);//立即预约_立即预约
        RelLayout_order_content_immediately_cancel.setOnClickListener(this);//立即联系_立即联系
        RelLayout_order_content_immediately_evaluation_left.setOnClickListener(this);//评价_评价
        RelLayout_order_content_immediately_evaluation_right.setOnClickListener(this);//评价_立即聘用
        RelLayout_order_content_immediately.setOnClickListener(this);//取消_立即联系
        but_order_content_cancelmake.setOnClickListener(this);//取消_取消
        but_order_content_jieshou.setOnClickListener(this);//接受_取消
        RelLayout_order_content_jieshou.setOnClickListener(this);//接受_接收
        img_cont_guwen.setOnClickListener(this);//接受_接收

    }

    // 读取到的数据放到对应id上去
    public void initData() {
        if (mTobeData != null) {
            datatobe();
        } else if (mConBean != null) {
            datacon();
        } else if (mComBean != null) {
            datacom();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinLyout_myorder_back:
                finish();
                break;
            case R.id.RelLayout_order_content_now:
                start();
                break;
            case R.id.RelLayout_order_content_immediately_now:
                Log.d(TAG, "onClick: 立即预约");
                Log.d(TAG, "CounselorId   " + mComBean.getCounselorId());
                Intent mIntent = new Intent(PrecontractMyOrderContent.this, Precontract.class);         // TODO 111111   立即预约
                mIntent.putExtra("counselorId", mComBean.getCounselorId());
                startActivity(mIntent);
                finish();

                break;
            case R.id.RelLayout_order_content_immediately_cancel:
                Log.d(TAG, "onClick:已确定中 立即联系");
                start();
                break;
            case R.id.RelLayout_order_content_immediately_evaluation_left:
                Log.d(TAG, "onClick: 评价");
                Intent mIntent2 = new Intent(PrecontractMyOrderContent.this, PingjiaActivity.class);
                mIntent2.putExtra("advanceId", mComBean.getAdvanceId() + "");
                startActivity(mIntent2);

                break;
            case R.id.RelLayout_order_content_immediately_evaluation_right:
                Log.d(TAG, "onClick: 立即聘用");
                if (mComBean != null) {
                    Intent intent = new Intent(PrecontractMyOrderContent.this, BaseWebViewActivity.class);
                    String URT = ZWConfig.ACTION_PINGYONG + "&counselorId=" + mComBean.getCounselorId();
                    intent.putExtra(BaseWebViewFragment.EXTRA_URL, URT);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.RelLayout_order_content_immediately:
                Log.d(TAG, "onClick: 待确定   立即联系");
                start();
                break;
            case R.id.but_order_content_cancelmake:
                Log.d(TAG, "onClick: 取消");
                //已完成
                Intent mIntent1 = new Intent(PrecontractMyOrderContent.this, CancellationActivity.class);
                if (mTobeData != null) {
                    //区分取消按钮的点击应传入那个Bean对象中的advanceId
                    mIntent1.putExtra("advanceId", mTobeData.getAdvanceId() + "");
                } else if (mConBean != null) {
                    mIntent1.putExtra("advanceId", mConBean.getAdvanceId() + "");
                }
                startActivity(mIntent1);
                finish();
                break;
            case R.id.but_order_content_jieshou:
                Log.d(TAG, "onClick: 接收预约  ☞  取消");
                //已完成
                Intent mIntent12 = new Intent(PrecontractMyOrderContent.this, CancellationActivity.class);
                mIntent12.putExtra("advanceId", mTobeData.getAdvanceId() + "");
                startActivity(mIntent12);
                break;
            case R.id.RelLayout_order_content_jieshou:
                Log.d(TAG, "onClick: 接收预约  ☞  接受");

                PrecontentManager.getInstance(this).reAdvanceAccept(PrecontractMyOrderContent.this, advanceId);
                break;
            // TODO H5
            case R.id.img_cont_guwen:
                Intent mIntent3 = new Intent(PrecontractMyOrderContent.this, BaseWebViewActivity.class);
                mIntent3.putExtra(BaseWebViewActivity.EXTRA_URL, ZWConfig.URGOOURL_BASE + "001/001/client/videoHelp?token=" + SPManager.getInstance(this).getToken());
                startActivity(mIntent3);
                break;
        }
    }

    private void getNetDataForupdate(String mUrl) {
        if (mUrl.equals("1")) {
            showLoadingDialog();
            PrecontentManager.getInstance(this).getPreContent1(PrecontractMyOrderContent.this, advanceId);
        }

        if (mUrl.equals("2")) {
            Log.d(TAG, " 已确定的详情 ");
            showLoadingDialog();
            PrecontentManager.getInstance(this).getPreContent2(PrecontractMyOrderContent.this, advanceId);

        }
        if (mUrl.equals("3")) {
            Log.d(TAG, " 已结束详情 ");
            showLoadingDialog();
            PrecontentManager.getInstance(this).getPreContent3(PrecontractMyOrderContent.this, advanceId);

        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            // 待确定
            case EventCodeAdvanceConfirmeDetailClient1:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mTobeData = gson.fromJson(jsonObject.getJSONObject("advanceDetail").toString(), new TypeToken<advanceDetail>() {
                    }.getType());
                    mTobeList = gson.fromJson(jsonObject.getJSONArray("advanceDetailTime").toString(), new TypeToken<ArrayList<advanceDetailTime>>() {
                    }.getType());
                    Log.d("待确定 dff ", "mTobeData: " + mTobeData.toString());
                    Log.d("待确定 dff ", "mTobeList: " + mTobeList.toString());
                    Message msg = new Message();
                    msg.what = HANDLER;
                    mHandler.sendMessage(msg);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            // 已确定
            case EvetCodeAdvanceConfirmeDetailClient2:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mConBean = gson.fromJson(jsonObject.getJSONObject("advanceDetail").toString(), new TypeToken<advanceDetail>() {
                    }.getType());
                    mDetailTimeBean = gson.fromJson(jsonObject.getJSONArray("advanceDetailTime").toString(), new TypeToken<ArrayList<advanceDetailTime>>() {
                    }.getType());
                    Log.d("已确认 dff ", "mConBeans: " + mConBean.toString());
                    Log.d("已确认 dff ", "mDetailTimeBeans: " + mDetailTimeBean.toString());
                    Message msg = new Message();
                    msg.what = HANDLER;
                    mHandler.sendMessage(msg);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            // 已结束
            case EventCodeAdvanceDetailClosedClient3:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mComBean = gson.fromJson(jsonObject.getJSONObject("advanceDetail").toString(), new TypeToken<advanceDetail>() {
                    }.getType());
                    mComList = gson.fromJson(jsonObject.getJSONArray("advanceDetailTime").toString(), new TypeToken<ArrayList<advanceDetailTime>>() {
                    }.getType());
                    Log.d("已结束 dff ", "mComBean: " + mComBean.toString());
                    Log.d("已结束 dff ", "mComList: " + mComList.toString());
                    Message msg = new Message();
                    msg.what = HANDLER;
                    mHandler.sendMessage(msg);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            // 接收预约
            case EventCodeReAdvanceAccept:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("header").toString());
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        mTobeList.clear();
                        Toast.makeText(PrecontractMyOrderContent.this, "接受成功！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PrecontractMyOrderContent.this, PrecontractMyOrder.class));
                        finish();
                    } else {
                        Toast.makeText(PrecontractMyOrderContent.this, "接受失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }


    //设置显示隐藏状态

    //  评价后 布局
    private void ifLayouttherr() {
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.GONE);
        RelativeLayouts.setVisibility(View.VISIBLE);
    }

    //  预约结束  待评价  布局
    private void ifLayout5() {
        tv_order_content_countdown.setVisibility(View.GONE);
        tv_order_content_coun.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.GONE);
        RelativeLayouts.setVisibility(View.VISIBLE);
    }

    //  已确认 倒计时结束 布局
    private void ifLayouttwo() {
        tv_order_content_countdown.setVisibility(View.VISIBLE);
        tv_order_content_coun.setVisibility(View.VISIBLE);
        //已确定状态立即联系，其余GONE掉
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.GONE);
        RelativeLayouts.setVisibility(View.VISIBLE);
    }

    private void ifLayouttwos() {
        tv_order_content_countdown.setVisibility(View.GONE);
        tv_order_content_coun.setVisibility(View.GONE);
        //已确定状态立即联系，其余GONE掉
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.GONE);
        RelativeLayouts.setVisibility(View.VISIBLE);
    }

    //  待确定中  待顾问接受时 布局
    private void ifLayoutone() {
        tv_order_content_title.setText("等待接受");
        tv_order_content_content.setText("等待顾问确认预约～");
        tv_order_content_countdown.setVisibility(View.GONE);
        tv_order_content_coun.setVisibility(View.GONE);
        //待确定中的状态1 和用户取消，显示立即预约布局
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.VISIBLE);   //
        Linear_order_content_RetLayout_jieshou.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.VISIBLE);
        RelativeLayouts.setVisibility(View.GONE);

    }

    //  已确认 倒计时进行中 布局
    private void ifLayoutoness() {
        tv_order_content_countdown.setVisibility(View.VISIBLE);
        tv_order_content_coun.setVisibility(View.VISIBLE);
        //待确定中的状态1 和用户取消，显示立即预约布局
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.GONE);
        RelativeLayouts.setVisibility(View.VISIBLE);

    }

    //  待确定中  接受顾问发来的预约 布局
    private void ifLayoutones() {
        tv_order_content_title.setText("等待接受");
        tv_order_content_content.setText("等待您确认预约～");
        tv_order_content_countdown.setVisibility(View.GONE);
        tv_order_content_coun.setVisibility(View.GONE);
        //待确定中的状态2，立即接受布局
        Linear_order_content_RetLayout_bottom_now.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom.setVisibility(View.GONE);
        Linear_order_content_RetLayout_jieshou.setVisibility(View.VISIBLE);
        Linear_order_content_RetLayout_bottom_evaluation.setVisibility(View.GONE);
        Linear_order_content_RetLayout_bottom_cancel.setVisibility(View.GONE);
        RelativeLayoutsTWO.setVisibility(View.VISIBLE);
        RelativeLayouts.setVisibility(View.GONE);
        //如果是待确定页面，日期是不显示的

    }

    //  初始化控件
    public void initView() {
        im_cont_TWO = (ImageView) findViewById(R.id.im_cont_TWO);
        im_cont_TWO2 = (ImageView) findViewById(R.id.im_cont_TWO2);
        im_cont = (ImageView) findViewById(R.id.im_cont);
        im_cont2 = (ImageView) findViewById(R.id.im_cont2);
        img_oo = (ImageView) findViewById(R.id.img_oo);
        img_oo2 = (ImageView) findViewById(R.id.img_oo2);
        img_oo3 = (ImageView) findViewById(R.id.img_oo3);
        img_cont_guwen = (ImageView) findViewById(R.id.img_cont_guwen);
        tv_pingjia = (TextView) findViewById(R.id.tv_pingjia);
        ratingBar_z1_score = (RatingBar) findViewById(R.id.ratingBar_z1_score);
        tv_order_content_title = (TextView) findViewById(R.id.tv_order_content_title);
        tv_order_content_coun = (TextView) findViewById(R.id.tv_order_content_coun);
        tv_order_content_content = (TextView) findViewById(R.id.tv_order_content_content);
        Linear_order_content_pingjia = (LinearLayout) findViewById(R.id.Linear_order_content_pingjia);
        RelativeLayouts = (LinearLayout) findViewById(R.id.RelativeLayouts);
        RelativeLayoutsTWO = (LinearLayout) findViewById(R.id.RelativeLayoutsTWO);
        tv_order_content_username = (TextView) findViewById(R.id.tv_order_content_username);
        tv_order_content_workyear = (TextView) findViewById(R.id.tv_order_content_workyear);
        tv_order_content_goodfield = (TextView) findViewById(R.id.tv_order_content_goodfield);
        tv_order_content_maketimes = (TextView) findViewById(R.id.tv_order_content_maketimes);
        tv_order_content_countdown = (TextView) findViewById(R.id.tv_order_content_countdown);
        tv_order_content_times = (TextView) findViewById(R.id.tv_order_content_times);
        tv_order_content_timesTWO = (TextView) findViewById(R.id.tv_order_content_timesTWO);
        tv_order_content_times_USA = (TextView) findViewById(R.id.tv_order_content_times_USA);
        tv_order_content_times_USATWO = (TextView) findViewById(R.id.tv_order_content_times_USATWO);
        tv_order_content_times2 = (TextView) findViewById(R.id.tv_order_content_times2);
        tv_order_content_times2TWO = (TextView) findViewById(R.id.tv_order_content_times2TWO);
        tv_order_content_times_USA2 = (TextView) findViewById(R.id.tv_order_content_times_USA2);
        tv_order_content_times_USA2TWO = (TextView) findViewById(R.id.tv_order_content_times_USA2TWO);
        tv_order_content_times3 = (TextView) findViewById(R.id.tv_order_content_times3);
        tv_order_content_times3TWO = (TextView) findViewById(R.id.tv_order_content_times3TWO);
        tv_order_content_times_USA3 = (TextView) findViewById(R.id.tv_order_content_times_USA3);
        tv_order_content_times_USA3TWO = (TextView) findViewById(R.id.tv_order_content_times_USA3TWO);
        ed_order_content_message = (EditText) findViewById(R.id.ed_order_content_message);
        tv_order_content_countriesname = (TextView) findViewById(R.id.tv_order_content_countriesname);
        Relat_order_content_time = (LinearLayout) findViewById(R.id.Relat_order_content_time);
        Relat_order_content_time2 = (LinearLayout) findViewById(R.id.Relat_order_content_time2);
        Relat_order_content_time2TWO = (LinearLayout) findViewById(R.id.Relat_order_content_time2TWO);
        Relat_order_content_time3 = (LinearLayout) findViewById(R.id.Relat_order_content_time3);
        Relat_order_content_time3TWO = (LinearLayout) findViewById(R.id.Relat_order_content_time3TWO);
        Linear_order_content_RetLayout_bottom = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_bottom);
        LinLyout_myorder_back = (LinearLayout) findViewById(R.id.LinLyout_myorder_back);
        img_order_content_usericon = (CircleImageView) findViewById(R.id.img_order_content_usericon);
        RelLayout_order_content_immediately_now = (RelativeLayout) findViewById(R.id.RelLayout_order_content_immediately_now);
        RelLayout_order_content_now = (RelativeLayout) findViewById(R.id.RelLayout_order_content_now);
        RelLayout_order_content_immediately_cancel = (RelativeLayout) findViewById(R.id.RelLayout_order_content_immediately_cancel);
        RelLayout_order_content_immediately_evaluation_left = (RelativeLayout) findViewById(R.id.RelLayout_order_content_immediately_evaluation_left);
        RelLayout_order_content_immediately_evaluation_right = (RelativeLayout) findViewById(R.id.RelLayout_order_content_immediately_evaluation_right);
        RelLayout_order_content_immediately = (RelativeLayout) findViewById(R.id.RelLayout_order_content_immediately);
        RelLayout_order_content_jieshou = (RelativeLayout) findViewById(R.id.RelLayout_order_content_jieshou);
        but_order_content_cancelmake = (Button) findViewById(R.id.but_order_content_cancelmake);
        but_order_content_jieshou = (Button) findViewById(R.id.but_order_content_jieshou);
        Linear_order_content_RetLayout_bottom = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_bottom);
        Linear_order_content_RetLayout_bottom_evaluation = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_bottom_evaluation);
        Linear_order_content_RetLayout_bottom_cancel = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_bottom_cancel);
        Linear_order_content_RetLayout_bottom_now = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_bottom_now);
        Linear_order_content_RetLayout_jieshou = (LinearLayout) findViewById(R.id.Linear_order_content_RetLayout_jieshou);


        rl_zoomship = (RelativeLayout) findViewById(R.id.rl_zoomship);

    }

    //待确定中数据加载
    private void datatobe() {
        // 姓名
        if (!Util.isEmpty(mTobeData.getEnName())) {
            tv_order_content_username.setText(mTobeData.getEnName());
        }
        //预约过
        if (!Util.isEmpty(mTobeData.getAdvancedTime()))
            tv_order_content_workyear.setText(mTobeData.getAdvancedTime() + "人预约过");
        else
            tv_order_content_workyear.setText("0人预约过");

        //已视频
        if (!Util.isEmpty(mTobeData.getClosedTime()))
            tv_order_content_goodfield.setText(mTobeData.getClosedTime() + "人已视频");
        else
            tv_order_content_goodfield.setText("0人已视频");

        if (!Util.isEmpty(mTobeData.getRatio()))
            tv_order_content_maketimes.setText("预约接受率： " + mTobeData.getRatio());
        else
            tv_order_content_maketimes.setText("预约接受率：0%");


        // 国籍
        if (!Util.isEmpty(mTobeData.getCountryName())) {
            tv_order_content_countriesname.setText(mTobeData.getCountryName());
        }

        //留言
        if (!Util.isEmpty(mTobeData.getMessage()))
            ed_order_content_message.setText(mTobeData.getMessage());


        //头像
        if (!Util.isEmpty(mTobeData.getUserIcon()))
            ImageLoaderUtil.displayImage(mTobeData.getUserIcon(), img_order_content_usericon);


        // 以下正常
        if (figs == 1) {
            Log.d(TAG, "initData: figs==1");
            switch (mTobeList.size()) {
                case 1:
                    dataone2();
                    break;
                case 2:
                    Relat_order_content_time2TWO.setVisibility(View.VISIBLE);
                    im_cont_TWO.setVisibility(View.VISIBLE);
                    dataone2();
                    datatwo2();
                    break;
                case 3:
                    Relat_order_content_time2TWO.setVisibility(View.VISIBLE);
                    Relat_order_content_time3TWO.setVisibility(View.VISIBLE);
                    im_cont_TWO.setVisibility(View.VISIBLE);
                    im_cont_TWO2.setVisibility(View.VISIBLE);
                    dataone2();
                    datatwo2();
                    datathree2();
                    break;
            }
        }
    }

    //  长度为 1 时，显示的布局，显示一条
    private void dataone2() {
        if (!Util.isEmpty(mTobeList.get(0).getCnStartTime()) && !Util.isEmpty(mTobeList.get(0).getCnEndTime()))
            tv_order_content_timesTWO.setText(mTobeList.get(0).getCnStartTime() + "-" + mTobeList.get(0).getCnEndTime() + "  " + mTobeList.get(0).getAdvanceDateCn());
        else
            tv_order_content_timesTWO.setText("未填写");
        if (!Util.isEmpty(mTobeList.get(0).getOtherStartTime()) && !Util.isEmpty(mTobeList.get(0).getOtherEndTime()))
            tv_order_content_times_USATWO.setText("(美国东部时间 " + mTobeList.get(0).getOtherStartTime() + "-" + mTobeList.get(0).getOtherEndTime() + ")");
        else
            tv_order_content_times_USATWO.setText("未填写");
    }

    //  长度为 2 时，显示的布局，显示两条
    private void datatwo2() {
        if (!Util.isEmpty(mTobeList.get(1).getCnStartTime()) && !Util.isEmpty(mTobeList.get(1).getCnEndTime()))
            tv_order_content_times2TWO.setText(mTobeList.get(1).getCnStartTime() + "-" + mTobeList.get(1).getCnEndTime() + "  " + mTobeList.get(1).getAdvanceDateCn());
        else
            tv_order_content_times2TWO.setText("未填写");

        if (!Util.isEmpty(mTobeList.get(1).getOtherStartTime()) && !Util.isEmpty(mTobeList.get(1).getOtherEndTime()))
            tv_order_content_times_USA2TWO.setText("(美国东部时间 " + mTobeList.get(1).getOtherStartTime() + "-" + mTobeList.get(1).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA2TWO.setText("未填写");
    }

    //  长度为 3 时，显示的布局，显示三条
    private void datathree2() {
        if (!Util.isEmpty(mTobeList.get(2).getCnStartTime()) && !Util.isEmpty(mTobeList.get(2).getCnEndTime()))
            tv_order_content_times3TWO.setText(mTobeList.get(2).getCnStartTime() + "-" + mTobeList.get(2).getCnEndTime() + "  " + mTobeList.get(2).getAdvanceDateCn());
        else
            tv_order_content_times3TWO.setText("未填写");
        if (!Util.isEmpty(mTobeList.get(2).getOtherStartTime()) && !Util.isEmpty(mTobeList.get(2).getOtherEndTime()))
            tv_order_content_times_USA3TWO.setText("(美国东部时间 " + mTobeList.get(2).getOtherStartTime() + "-" + mTobeList.get(2).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA3TWO.setText("未填写");
    }

    //确定中数据加载
    private void datacon() {

        if (!Util.isEmpty(mConBean.getEnName())) {
            tv_order_content_username.setText(mConBean.getEnName());
        }
        if (!Util.isEmpty(mConBean.getAdvancedTime()))
            tv_order_content_workyear.setText(mConBean.getAdvancedTime() + "人预约过");
        else
            tv_order_content_workyear.setText("0人预约过");

        if (!Util.isEmpty(mConBean.getClosedTime()))
            tv_order_content_goodfield.setText(mConBean.getClosedTime() + "人已视频");
        else
            tv_order_content_goodfield.setText("0人已视频");

        if (!Util.isEmpty(mConBean.getRatio()))
            tv_order_content_maketimes.setText("预约接受率： " + mConBean.getRatio());
        else
            tv_order_content_maketimes.setText("预约接受率：0%");

        // 国籍
        if (!Util.isEmpty(mConBean.getCountryName())) {
            tv_order_content_countriesname.setText(mConBean.getCountryName());
        }

        if (!Util.isEmpty(mConBean.getDaojishi())) {
            tv_order_content_countdown.setText(DataUtil.formatDuring(Integer.parseInt(mConBean.getDaojishi())) + "");
            mInt = Integer.parseInt(mConBean.getDaojishi());
            Message message = mHandler.obtainMessage(HANDLERS);
            mHandler.sendMessageDelayed(message, 1000);
        }
        if (!Util.isEmpty(mConBean.getMessage()))
            ed_order_content_message.setText(mConBean.getMessage());


        if (!Util.isEmpty(mConBean.getUserIcon()))
            new imageLoadBusiness().imageLoadByURL(mConBean.getUserIcon(), img_order_content_usericon);




        Log.d(TAG, "mDetailTimeBean size: " + mDetailTimeBean.size());
        switch (mDetailTimeBean.size()) {
            case 1:
                datas1();
                Relat_order_content_time.setVisibility(View.VISIBLE);
                break;
            case 2:
                datas1();
                datas2();
                Relat_order_content_time.setVisibility(View.VISIBLE);
                Relat_order_content_time2.setVisibility(View.VISIBLE);
                im_cont_TWO.setVisibility(View.VISIBLE);
                im_cont.setVisibility(View.VISIBLE);
                break;
            case 3:
                datas1();
                datas2();
                datas3();
                Relat_order_content_time.setVisibility(View.VISIBLE);
                Relat_order_content_time2.setVisibility(View.VISIBLE);
                Relat_order_content_time3.setVisibility(View.VISIBLE);
                im_cont_TWO.setVisibility(View.VISIBLE);
                im_cont_TWO2.setVisibility(View.VISIBLE);
                im_cont.setVisibility(View.VISIBLE);
                im_cont2.setVisibility(View.VISIBLE);
                break;
        }

    }

    //  长度为 3 时，显示的布局，显示三条
    private void datas3() {
        if (mDetailTimeBean.get(2).getStatus().equals("2")) {
            tv_order_content_times3.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA3.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo3.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times3.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA3.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo3.setVisibility(View.GONE);
        }
        if (!Util.isEmpty(mDetailTimeBean.get(2).getCnStartTime()) && !Util.isEmpty(mDetailTimeBean.get(2).getCnEndTime()))
            tv_order_content_times3.setText(mDetailTimeBean.get(2).getCnStartTime() + "-" + mDetailTimeBean.get(2).getCnEndTime() + "  " + mDetailTimeBean.get(2).getAdvanceDateCn());
        else
            tv_order_content_times3.setText("未填写");
        if (!Util.isEmpty(mDetailTimeBean.get(2).getOtherStartTime()) && !Util.isEmpty(mDetailTimeBean.get(2).getOtherEndTime()))
            tv_order_content_times_USA3.setText("(美国东部时间 " + mDetailTimeBean.get(2).getOtherStartTime() + "-" + mDetailTimeBean.get(2).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA3.setText("未填写");
    }

    //  长度为 2 时，显示的布局，显示两条
    private void datas2() {
        if (mDetailTimeBean.get(1).getStatus().equals("2")) {
            tv_order_content_times2.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA2.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo2.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times2.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA2.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo2.setVisibility(View.GONE);
        }
        if (!Util.isEmpty(mDetailTimeBean.get(1).getCnStartTime()) && !Util.isEmpty(mDetailTimeBean.get(1).getCnEndTime()))
            tv_order_content_times2.setText(mDetailTimeBean.get(1).getCnStartTime() + "-" + mDetailTimeBean.get(1).getCnEndTime() + "  " + mDetailTimeBean.get(1).getAdvanceDateCn());
        else
            tv_order_content_times2.setText("未填写");
        if (!Util.isEmpty(mDetailTimeBean.get(1).getOtherStartTime()) && !Util.isEmpty(mDetailTimeBean.get(1).getOtherEndTime()))
            tv_order_content_times_USA2.setText("(美国东部时间 " + mDetailTimeBean.get(1).getOtherStartTime() + "-" + mDetailTimeBean.get(1).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA2.setText("未填写");
    }

    //  长度为 1 时，显示的布局，显示一条
    private void datas1() {
        if (mDetailTimeBean.get(0).getStatus().equals("2")) {
            tv_order_content_times.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo.setVisibility(View.GONE);
        }

        if (!Util.isEmpty(mDetailTimeBean.get(0).getCnStartTime()) && !Util.isEmpty(mDetailTimeBean.get(0).getCnEndTime()))
            tv_order_content_times.setText(mDetailTimeBean.get(0).getCnStartTime() + "-" + mDetailTimeBean.get(0).getCnEndTime() + "  " + mDetailTimeBean.get(0).getAdvanceDateCn());
        else
            tv_order_content_times.setText("未填写");
        if (!Util.isEmpty(mDetailTimeBean.get(0).getOtherStartTime()) && !Util.isEmpty(mDetailTimeBean.get(0).getOtherEndTime()))
            tv_order_content_times_USA.setText("(美国东部时间 " + mDetailTimeBean.get(0).getOtherStartTime() + "-" + mDetailTimeBean.get(0).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA.setText("未填写");
    }

    //已结束中数据加载
    private void datacom() {
        if (!Util.isEmpty(mComBean.getEnName())) {
            tv_order_content_username.setText(mComBean.getEnName());
        }

        if (!Util.isEmpty(mComBean.getAdvancedTime()))
            tv_order_content_workyear.setText(mComBean.getAdvancedTime() + "人预约过");
        else
            tv_order_content_workyear.setText("0人预约过");

        if (!Util.isEmpty(mComBean.getClosedTime()))
            tv_order_content_goodfield.setText(mComBean.getClosedTime() + "人已视频");
        else
            tv_order_content_goodfield.setText("0人已视频");

        if (!Util.isEmpty(mComBean.getRatio()))
            tv_order_content_maketimes.setText("预约接受率： " + mComBean.getRatio());
        else
            tv_order_content_maketimes.setText("预约接受率：0%");

        if (!Util.isEmpty(mComBean.getMessage()))
            ed_order_content_message.setText(mComBean.getMessage());


        if (!Util.isEmpty(mComBean.getUserIcon()))
            new imageLoadBusiness().imageLoadByURL(mComBean.getUserIcon(), img_order_content_usericon);

        // 国籍
        if (!Util.isEmpty(mComBean.getCountryName())) {
            tv_order_content_countriesname.setText(mComBean.getCountryName());
        }


        // 根据得到的list的长度，确定有几个需要显示
        switch (mComList.size()) {
            case 1:
                dataone1();
                break;
            case 2:
                Relat_order_content_time2.setVisibility(View.VISIBLE);
                dataone1();
                datatwo1();
                break;
            case 3:
                Relat_order_content_time2.setVisibility(View.VISIBLE);
                Relat_order_content_time3.setVisibility(View.VISIBLE);
                dataone1();
                datatwo1();
                datathree1();
                break;
        }

    }

    //  长度为 3 时，显示的布局，显示三条
    private void datathree1() {

        if (mComList.get(2).getStatus().equals("2")) {
            tv_order_content_times3.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA3.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo3.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times3.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA3.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo3.setVisibility(View.GONE);
        }

        if (!Util.isEmpty(mComList.get(2).getCnStartTime()) && !Util.isEmpty(mComList.get(2).getCnEndTime()))
            tv_order_content_times3.setText(mComList.get(2).getCnStartTime() + "-" + mComList.get(2).getCnEndTime() + "  " + mComList.get(2).getAdvanceDateCn());
        else
            tv_order_content_times3.setText("未填写");
        if (!Util.isEmpty(mComList.get(2).getOtherStartTime()) && !Util.isEmpty(mComList.get(2).getOtherEndTime()))
            tv_order_content_times_USA3.setText("(美国东部时间 " + mComList.get(2).getOtherStartTime() + "-" + mComList.get(2).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA3.setText("未填写");
    }

    //  长度为 2 时，显示的布局，显示两条
    private void datatwo1() {
        if (mComList.get(1).getStatus().equals("2")) {
            tv_order_content_times2.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA2.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo2.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times2.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA2.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo2.setVisibility(View.GONE);
        }


        if (!Util.isEmpty(mComList.get(1).getCnStartTime()) && !Util.isEmpty(mComList.get(1).getCnEndTime()))
            tv_order_content_times2.setText(mComList.get(1).getCnStartTime() + "-" + mComList.get(1).getCnEndTime() + "  " + mComList.get(1).getAdvanceDateCn());
        else
            tv_order_content_times2.setText("未填写");

        if (!Util.isEmpty(mComList.get(1).getOtherStartTime()) && !Util.isEmpty(mComList.get(1).getOtherEndTime()))
            tv_order_content_times_USA2.setText("(美国东部时间 " + mComList.get(1).getOtherStartTime() + "-" + mComList.get(1).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA2.setText("未填写");
    }

    //  长度为 1 时，显示的布局，显示一条
    private void dataone1() {
        if (mComList.get(0).getStatus().equals("2")) {
            tv_order_content_times.setTextColor(getResources().getColor(R.color.b_383838));
            tv_order_content_times_USA.setTextColor(getResources().getColor(R.color.b_383838));
            img_oo2.setVisibility(View.VISIBLE);
        } else {
            tv_order_content_times.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            tv_order_content_times_USA.setTextColor(getResources().getColor(R.color.b_b7b7b7));
            img_oo2.setVisibility(View.GONE);
        }

        if (!Util.isEmpty(mComList.get(0).getCnStartTime()) && !Util.isEmpty(mComList.get(0).getCnEndTime()))
            tv_order_content_times.setText(mComList.get(0).getCnStartTime() + "-" + mComList.get(0).getCnEndTime() + "  " + mComList.get(0).getAdvanceDateCn());
        else
            tv_order_content_times.setText("未填写");
        if (!Util.isEmpty(mComList.get(0).getOtherStartTime()) && !Util.isEmpty(mComList.get(0).getOtherEndTime()))
            tv_order_content_times_USA.setText("(美国东部时间 " + mComList.get(0).getOtherStartTime() + "-" + mComList.get(0).getOtherEndTime() + ")");
        else
            tv_order_content_times_USA.setText("未填写");
    }

    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    @Override
    public void onMeetingEvent(int i, int i1, int i2) {

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

    //杨德成20160801 获取ZOOM房间;接受或拒绝顾问端发起的视频邀请 0:用户未操作，1：接受，2：拒绝
    private void getZOOMInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        //params.put("token", "ENW2behsR+g=");
        params.put("termType", "2");
        OkHttpClientManager.postAsyn(ZWConfig.Action_getZoomRoom,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

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
                                    Intent it = new Intent(PrecontractMyOrderContent.this, UrgooVideoActivity.class);
                                    it.putExtra("icon", pic);
                                    it.putExtra("name", nickname);
                                    it.putExtra("zoomId", zoomId);
                                    it.putExtra("zoomNo", zoomNo);
                                    //it.putExtra("hxCode", "");
                                    startActivity(it);
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

    public void start() {
//        startActivity(new Intent(PrecontractMyOrderContent.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
        finish();
    }

}