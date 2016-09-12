package com.urgoo.zhibo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.Interface.Constants;
import com.urgoo.adapter.PinglunListAdapter;
import com.urgoo.base.ActivityBase;
import com.urgoo.business.BaseService;
import com.urgoo.client.R;
import com.urgoo.common.DataUtil;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.event.CounselorEvent;
import com.urgoo.counselor.model.CounselorDetail;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.domain.OldZhiBoEntity;
import com.urgoo.domain.ShareDetail;
import com.urgoo.domain.ZhiBoDetailEntity;
import com.urgoo.domain.ZhiBoPinglunEntity;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.urgoo.schedule.activites.Precontract;
import com.urgoo.schedule.activites.PrecontractMyOrder;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;
import com.urgoo.view.MyListView;
import com.urgoo.zhibo.biz.LiveManager;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Administrator on 2016/7/14.
 */
public class ZhiBodDetailActivity extends ActivityBase implements Constants, ZoomSDKInitializeListener, MeetingServiceListener {

    private LinearLayout back, ll_more;

    private String liveId = "";
    private String zhibo = "1";
    private TextView tv_title, tv_baomingnumber, tv_miaoshu, tv_shichangvalue, tv_shijianvalue, tv_jiabingcontent;
    private TextView tvAppointment;
    private RelativeLayout re_zhibo;
    private SimpleDraweeView iv_mingpian;
    private ShareDetail shareDetail;

    private String zoomNO = "";

    private MyListView mListView, jingc_listView;
    private PinglunListAdapter mAdapter;

    private RelativeLayout re_pinglun, re_zhibopinglun;
    private LinearLayout ll_comment;

    private TextView tv_statuscontent;
    private Button btn_enter;

    ZoomSDK sdk;


    private int type = 0;
    private List<Object> mList = new ArrayList<Object>();
    private List<Object> mList2 = new ArrayList<Object>();
    private int pageNo = 0;

    boolean isLastRow = false;

    private ScrollView sc_content;
    private LinearLayout ll_send;
    private Button btn_pinglu;
    private EditText edit_message;

    //状态    直播类型1：直播，2：已结束直播，3：往期直播(由运营人员上传视频)
    private String status = "";

    private String counselorId = "";
    private TextView tv_yugao;
    private Button btn_bottomcontent;
    private LinearLayout ll_bottom;
    private SimpleDraweeView im_oldzhibofirst, im_oldzhibosecond, im_oldzhibothird;
    private String[] oldArry;
    private TextView tv_counselor;

    private CountdownUtil c;
    private List<Object> oldList = new ArrayList<Object>();
    private String videoURLStr = "";
    private String isSign = "";
    //当值为1是可以进入视频聊天
    private int isvideotype = 0;

    private RelativeLayout im_sharesdk;
    private ImageView iv_ImageView;
    private int size;
    private boolean isLoadMore = false;
    private CounselorDetail mCounselorDetail;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        sdk = ZoomSDK.getInstance();
        if (arg0 == null) {
            sdk.initialize(ZhiBodDetailActivity.this, APP_KEY, APP_SECRET, WEB_DOMAIN, ZhiBodDetailActivity.this);
            sdk.setDropBoxAppKeyPair(ZhiBodDetailActivity.this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(ZhiBodDetailActivity.this, ONEDRIVE_CLIENT_ID);
            sdk.setGoogleDriveClientId(ZhiBodDetailActivity.this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }
        setContentView(R.layout.zhiboddetail_layout);
        initData();
        initView();
    }


    //刷新整个页面数据
    private void refreshData(String liveId) {
        getZoomLiveDetail(liveId);
        getOldZhiBoData();
        sc_content.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (c != null) {
            c.stopThread();
        }
    }

    @Override
    public void initView() {
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isEmpty(mCounselorDetail.getIsAdvanceRelation().trim())) {
                    // 如果为 0 ，表示该家长和该顾问没有联系，点击后调转到预约界面
                    if (mCounselorDetail.getIsAdvanceRelation().equals("0")) {
                        startActivity(new Intent(ZhiBodDetailActivity.this, Precontract.class).putExtra("counselorId", counselorId));
                    } else if (mCounselorDetail.getIsAdvanceRelation().equals("1")) {

                        // 如果为 1 ，表示该家长和该顾问有联系，点击后调转到预约详情界面
                        startActivity(new Intent(ZhiBodDetailActivity.this, PrecontractMyOrderContent.class)
                                .putExtra("status", mCounselorDetail.getStatus())
                                .putExtra("advanceId", mCounselorDetail.getAdvanceId())
                                .putExtra("type", mCounselorDetail.getType()));
                    }
                }
            }
        });
        tv_counselor = (TextView) findViewById(R.id.tv_counselor);
        iv_ImageView = (ImageView) findViewById(R.id.iv_ImageView);
        im_sharesdk = (RelativeLayout) findViewById(R.id.im_sharesdk);
        im_sharesdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.share(ZhiBodDetailActivity.this, shareDetail.title, shareDetail.text, shareDetail.pic, ZWConfig.URGOOURL_BASE + shareDetail.url);
            }
        });

        tv_counselor = (TextView) findViewById(R.id.tv_counselor);
        tv_counselor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zhibo.equals("0")) {
                    finish();
                } else {
                    Intent intent = new Intent(ZhiBodDetailActivity.this, CounselorActivity.class);
                    intent.putExtra("counselorId", counselorId);
                    intent.putExtra("zhibo", "0");
                    startActivity(intent);
                }
            }
        });
        im_oldzhibofirst = (SimpleDraweeView) findViewById(R.id.im_oldzhibofirst);
        im_oldzhibofirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oldList.size() > 0) {
                    OldZhiBoEntity entity = (OldZhiBoEntity) oldList.get(0);
                    //UiUtil.show(ZhiBodDetailActivity.this, entity.getLiveId());
                    liveId = entity.getLiveId();
                    refreshData(entity.getLiveId());
                    refreshPinglunView();
                } else {
                    UiUtil.show(ZhiBodDetailActivity.this, "无对应数据!");
                }


            }
        });
        im_oldzhibosecond = (SimpleDraweeView) findViewById(R.id.im_oldzhibosecond);
        im_oldzhibosecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldList.size() > 1) {
                    OldZhiBoEntity entity = (OldZhiBoEntity) oldList.get(1);
                    //UiUtil.show(ZhiBodDetailActivity.this, entity.getLiveId());
                    liveId = entity.getLiveId();
                    refreshData(entity.getLiveId());
                    refreshPinglunView();
                } else {
                    UiUtil.show(ZhiBodDetailActivity.this, "无对应数据!");
                }
            }
        });
        im_oldzhibothird = (SimpleDraweeView) findViewById(R.id.im_oldzhibothird);
        im_oldzhibothird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldList.size() > 2) {
                    OldZhiBoEntity entity = (OldZhiBoEntity) oldList.get(2);
                    // UiUtil.show(ZhiBodDetailActivity.this, entity.getLiveId());
                    liveId = entity.getLiveId();
                    refreshData(entity.getLiveId());
                    refreshPinglunView();
                } else {
                    UiUtil.show(ZhiBodDetailActivity.this, "无对应数据!");
                }

            }
        });

        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        btn_bottomcontent = (Button) findViewById(R.id.btn_bottomcontent);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        sc_content = (ScrollView) findViewById(R.id.sc_content);

        sc_content.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            handleStop(scroller);
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };


            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        View view = ((ScrollView) v).getChildAt(0);
                        if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                            //加载数据代码
                            if (size == 10 && !isLoadMore) {
                                getZhiBoPinglunList();
                                isLoadMore = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    default:
                        break;
                }
                return false;
            }


            private void handleStop(Object view) {
                ScrollView scroller = (ScrollView) view;
                hideSoftInput(sc_content);
                ll_bottom.setVisibility(View.VISIBLE);
                ll_send.setVisibility(View.GONE);
                //UiUtil.show(ZhiBodDetailActivity.this,"kkkkkkkkkkkkk");
                //scrollY = scroller.getScrollY();
            }
        });

        tv_miaoshu = (TextView) findViewById(R.id.tv_miaoshu);
        tv_shijianvalue = (TextView) findViewById(R.id.tv_shijianvalue);
        tv_shichangvalue = (TextView) findViewById(R.id.tv_shichangvalue);
        tv_jiabingcontent = (TextView) findViewById(R.id.tv_jiabingcontent);
        iv_mingpian = (SimpleDraweeView) findViewById(R.id.iv_mingpian);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                    Bundle extras = new Bundle();
                    extras.putInt(MainActivity.EXTRA_TAB, 1);
                    Util.openActivityWithBundle(ZhiBodDetailActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                finish();
            }
        });

//        ll_more = (LinearLayout) findViewById(R.id.ll_more);
//        ll_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_baomingnumber = (TextView) findViewById(R.id.tv_baomingnumber);
        re_zhibo = (RelativeLayout) findViewById(R.id.re_zhibo);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //状态    直播类型1：直播，2：已结束直播，3：往期直播(由运营人员上传视频)
                if (status.equals("1")) {
                    if (isSign.equals("0")) {
                        netBaoMing(liveId);
                    } else {
                        MyJoinInstantMeeting(zoomNO, spManager.getNickName() == null ? "某XX" : spManager.getNickName());
                    }
                } else if (status.equals("3")) {
                    if (videoURLStr.equals("")) {
                        UiUtil.show(ZhiBodDetailActivity.this, "无视频文件");
                    } else {
                        broadcastVideo(videoURLStr);
                    }

                }
            }
        });
        btn_bottomcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("1")) {
                    if (isSign.equals("0")) {
                        netBaoMing(liveId);
                    } else if (isvideotype == 1){
                        MyJoinInstantMeeting(zoomNO, spManager.getNickName() == null ? "某XX" : spManager.getNickName());
                    }
                } else if (status.equals("3")) {
                    if (videoURLStr.equals("")) {
                        UiUtil.show(ZhiBodDetailActivity.this, "无视频文件");
                    } else {
                        broadcastVideo(videoURLStr);
                    }
                }
            }

        });

        mListView = (MyListView) findViewById(R.id.pinglun_listView);
        mAdapter = new PinglunListAdapter(ZhiBodDetailActivity.this, mList, 0);
        mListView.setAdapter(mAdapter);

        View ll = (View) LinearLayout.inflate(ZhiBodDetailActivity.this, R.layout.listview_footer_normal, null);
        mListView.addFooterView(ll);//设置可以上拉加载
        mListView.setFocusable(false);

        edit_message = (EditText) findViewById(R.id.edit_message);
        tv_yugao = (TextView) findViewById(R.id.tv_yugao);

        re_pinglun = (RelativeLayout) findViewById(R.id.re_pinglun);
        re_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bottom.setVisibility(View.GONE);
                ll_send.setVisibility(View.VISIBLE);
                edit_message.requestFocus();
                showSoftInput();
            }
        });

        btn_pinglu = (Button) findViewById(R.id.btn_pinglu);
        btn_pinglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = edit_message.getText().toString();
                if (message.equals("") || message == null) {
                    UiUtil.show(ZhiBodDetailActivity.this, "评论内容不能为空！");
                } else {
                    hideSoftInput(btn_pinglu);
                    NetEvaluation(liveId, message);
                }

            }
        });

    }


    //获取晚期直播数据
    private void getOldZhiBoData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
        //params.put("token", "ENW2behsR+g=");
        params.put("liveId", liveId);
        params.put("page", "0");

        OkHttpClientManager.postAsyn(ZWConfig.Action_selectZoomPassedInDetail,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            NetHeaderInfoEntity hentity = BaseService.getNetHeadInfo(j);
                            if (hentity.getCode().equals("200")) {

                                JSONObject jo2 = j.getJSONObject("body");
                                ja = jo2.getJSONArray("liveList");
                                Log.d("yyy4444", "0000>" + ja.toString());
                                oldList.clear();
                                if (ja != null && ja.length() > 0) {
                                    for (int i = 0; i < ja.length(); i++) {
                                        //JSONObject j = ja.getJSONObject(i);
                                        OldZhiBoEntity entity = GsonTools.getTargetClass(ja.getJSONObject(i).toString(), OldZhiBoEntity.class);
                                        if (entity != null) {
                                            oldList.add(entity);
                                        }
                                    }

                                }
                                //UiUtil.show(ZhiBodDetailActivity.this, "评论成功!");
                                refreshOldoView();

                            } else if (hentity.getCode().equals("404")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }
                            if (hentity.getCode().equals("400")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }


                        } catch (JSONException e) {
                            UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }

    //播放视频
    private void broadcastVideo(String videoURL) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setDataAndType(Uri.parse("http://urgootest.oss-cn-qingdao.aliyuncs.com/test.mp4"),"video/mp4");
        intent.setDataAndType(Uri.parse(videoURL), "video/mp4");
        startActivity(intent);
    }

    //评论络操作
    private void NetEvaluation(final String liveId, String comment) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
        // params.put("token", "ENW2behsR+g=");
        params.put("liveId", liveId);
        params.put("content", comment);

        OkHttpClientManager.postAsyn(ZWConfig.Action_insertLiveCommentContent,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        sc_content.fullScroll(ScrollView.FOCUS_DOWN);
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            NetHeaderInfoEntity hentity = BaseService.getNetHeadInfo(j);

                            if (hentity.getCode().equals("200")) {
                                UiUtil.show(ZhiBodDetailActivity.this, "评论成功!");
                                refreshPinglunView();

                            } else if (hentity.getCode().equals("404")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }
                            if (hentity.getCode().equals("400")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }


                        } catch (JSONException e) {
                            UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }


    //报名
    private void netBaoMing(final String liveId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken());
        //params.put("token", "ENW2behsR+g=");
        params.put("liveId", liveId);

        OkHttpClientManager.postAsyn(ZWConfig.Action_addActivitySign,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");
                            NetHeaderInfoEntity hentity = BaseService.getNetHeadInfo(j);
                            if (hentity.getCode().equals("200")) {

                                UiUtil.show(ZhiBodDetailActivity.this, "报名成功!");

                                refreshData(liveId);

                            } else if (hentity.getCode().equals("404")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }
                            if (hentity.getCode().equals("400")) {
                                UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());

                            }


                        } catch (JSONException e) {
                            UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");

                        }
                    }
                }

                , params);
    }

    private void refreshPinglunView() {
        edit_message.setText("");
        edit_message.requestFocus();
        ll_bottom.setVisibility(View.VISIBLE);
        ll_send.setVisibility(View.GONE);
        pageNo = 0;
        getZhiBoPinglunList();
    }

    private void getZhiBoPinglunList() {
        LiveManager.getInstance(this).getZhiBoPinglunList(liveId, "", "2", pageNo, this);
    }

    private void getZoomLiveDetail(String liveId) {
        LiveManager.getInstance(this).getZoomLiveDetail(liveId, this);
    }


    @Override
    public void initData() {
        liveId = getIntent().getStringExtra("liveId");
        if (!Util.isEmpty(getIntent().getStringExtra("zhibo"))) {
            zhibo = getIntent().getStringExtra("zhibo");
        }
        getZoomLiveDetail(liveId);
        getOldZhiBoData();
    }

    private void refreshOldoView() {
        //mageLoadBusiness imservice=new imageLoadBusiness();
        for (int i = 0; i < oldList.size(); i++) {
            OldZhiBoEntity entity = (OldZhiBoEntity) oldList.get(i);
            if (i == 0) {
                im_oldzhibofirst.setImageURI(Uri.parse(entity.getMasterPic()));
            } else if (i == 1) {
                im_oldzhibosecond.setImageURI(Uri.parse(entity.getMasterPic()));
            } else if (i == 2) {
                im_oldzhibothird.setImageURI(Uri.parse(entity.getMasterPic()));
            }
        }

    }

    private void refreshListView() {
        // mAdapter.setmInfoType(type);
        if (mList != null && mList.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            mListView.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetZhiBoPinglunList:
                try {
                    if (pageNo == 0) {
                        mList.clear();
                    }
                    JSONObject jo2 = result.getJSONObject("body");
                    JSONArray ja = jo2.getJSONArray("liveCommentList");
                    Log.d("yyy22222222222", "0000>" + ja.toString());
                    size = ja.length();
                    if (ja != null && size > 0) {
                        for (int i = 0; i < size; i++) {
                            //JSONObject j = ja.getJSONObject(i);
                            ZhiBoPinglunEntity entity = GsonTools.getTargetClass(ja.getJSONObject(i).toString(), ZhiBoPinglunEntity.class);
                            if (entity != null) {
                                mList.add(entity);
                            }

                        }
                        refreshListView();
                        if (size == 10) {
                            pageNo++;
                        }
                        isLoadMore = false;
                        ProgressBar pb = (ProgressBar) findViewById(R.id.loading);
                        TextView tv = (TextView) findViewById(R.id.more);
                        pb.setVisibility(View.GONE);
                        tv.setText("只有那么多了哦");
                        tv.setVisibility(View.VISIBLE);

                    } else {
                        size = 0;
                        ProgressBar pb = (ProgressBar) findViewById(R.id.loading);
                        TextView tv = (TextView) findViewById(R.id.more);
                        pb.setVisibility(View.GONE);
                        if (!isLoadMore) {
                            tv.setText("快来第一个评论吧~");
                        }
                        tv.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeZoomLiveDetail:
                try {
                    //UiUtil.show(ZhiBodDetailActivity.this, message);
                    shareDetail = GsonTools.getTargetClass(new JSONObject(result.get("body").toString()).getString("shareDetail"), ShareDetail.class);
                    ZhiBoDetailEntity entity = GsonTools.getTargetClass(new JSONObject(result.get("body").toString()).getString("liveDetail"), ZhiBoDetailEntity.class);
                    mCounselorDetail = GsonTools.getTargetClass(new JSONObject(result.get("body").toString()).getString("advanceRelation"), CounselorDetail.class);
                    zoomNO = entity.getZoomNo();
                    status = entity.getStatus();
                    counselorId = entity.getTargetId();
                    videoURLStr = entity.getVideo();
                    isSign = entity.getIsSign();
                    refreshUIView(entity);
                } catch (JSONException e) {
                    UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");
                }
                break;
        }
    }

    //刷新主接口UI
    private void refreshUIView(ZhiBoDetailEntity entity) {


        if (entity.getTargetId().equals("0")) {
            tv_counselor.setVisibility(View.GONE);
            iv_ImageView.setVisibility(View.GONE);
        } else {
            tv_counselor.setVisibility(View.VISIBLE);
            iv_ImageView.setVisibility(View.VISIBLE);
        }
        iv_mingpian.setImageURI(Uri.parse(entity.getMasterPic()));
        tv_title.setText(entity.getTitle());
        tv_baomingnumber.setText(entity.getPaticipateCount() + "人已报名");
        tv_miaoshu.setText(entity.getDes());


        long liveTime = Long.parseLong(entity.getLiveTimeLong());
        tv_shichangvalue.setText(DataUtil.formatDuring3(Integer.parseInt(liveTime + "")));
        //tv_shichangvalue.setText(" "+entity.getLiveTimeLong()+"小时");
        tv_shijianvalue.setText(" " + entity.getLiveStartTime());
        tv_jiabingcontent.setText(entity.getIntroduce());
        tv_yugao.setText(entity.getLiveNotice());

        // 1： 进入直播,2 已结束, 3 查看回播
        if (entity.getStatus().equals("1")) {
            re_zhibo.setBackgroundResource(R.drawable.linearlayoutframe_selected);
            btn_enter.setText("进入直播");
            btn_enter.setBackgroundColor(0xFF26BDAB);
            long timeStr = entity.getBalanceTime();
            if (timeStr >= 0) {
                btn_enter.setEnabled(false);
                c = new CountdownUtil(timeStr * 1000, btn_enter);
                c.countdown();
               /* //1：已经报名，0：未报名
                if(entity.getIsSign().equals("0")){
                    btn_bottomcontent.setEnabled(true);
                    btn_bottomcontent.setTextColor(0xFF26BDAB);
                    btn_bottomcontent.setText("立即报名");
                }*/
                if (entity.getIsSign().equals("1")) {
                    btn_bottomcontent.setEnabled(false);
                    btn_bottomcontent.setTextColor(0xFF676767);
                    btn_bottomcontent.setText("已报名");
                } else {
                    btn_bottomcontent.setEnabled(true);
                    btn_bottomcontent.setTextColor(0xFF26BDAB);
                    btn_bottomcontent.setText("立即报名");
                }
            } else {
                isvideotype = 1;
                btn_bottomcontent.setEnabled(true);
                btn_bottomcontent.setTextColor(0xFF26BDAB);
                btn_bottomcontent.setText("进入直播");
            }
        } else if (entity.getStatus().equals("2")) {
            if (c != null) {
                c.stopThread();
            }
            re_zhibo.setBackgroundResource(R.drawable.linearlayoutframe_over);
            btn_enter.setEnabled(false);
            btn_enter.setText("已结束");
            btn_enter.setBackgroundColor(0xFF5E605F);

            btn_bottomcontent.setEnabled(false);
            btn_bottomcontent.setText("已结束");
            btn_bottomcontent.setTextColor(0xFF676767);

        } else if (entity.getStatus().equals("3")) {
            if (c != null) {
                c.stopThread();
            }
            tv_baomingnumber.setText("");
            re_zhibo.setBackgroundResource(R.drawable.linearlayoutframe_selected);
            btn_enter.setEnabled(true);
            btn_enter.setText("查看回播");
            btn_enter.setBackgroundColor(0xFF26BDAB);
            btn_bottomcontent.setEnabled(true);
            btn_bottomcontent.setText("查看回播");
            btn_bottomcontent.setTextColor(0xFF26BDAB);
        }
        getZhiBoPinglunList();
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

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
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

    }


    /**
     * 倒计时
     */
    public class CountdownUtil {
        private long time;
        Button counetdownView;
        CountdownThread thread;
        SimpleDateFormat formatter;
        String hms;

        /**
         * @time:时间差(指定的一段时间长),时间戳
         * @counetdownView：TextView显示倒计时
         */
        public CountdownUtil(long time, Button counetdownView) {
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
                counetdownView.setText(DataUtil.formatDuring2(Integer.parseInt(millisUntilFinished / 1000 + "")) + "后开始");
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                //倒计时结束时触发
                isvideotype = 1;
                counetdownView.setEnabled(true);
                counetdownView.setText("进入直播");

                btn_bottomcontent.setTextColor(0xFF26BDAB);
                btn_bottomcontent.setText("进入直播");
                btn_bottomcontent.setEnabled(true);
            }
        }

        /**
         * 终止线程
         */
        public void stopThread() {
            thread.cancel();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                Bundle extras = new Bundle();
                extras.putInt(MainActivity.EXTRA_TAB, 1);
                Util.openActivityWithBundle(ZhiBodDetailActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}