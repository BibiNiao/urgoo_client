package com.urgoo.counselor.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.ActivityBase;
import com.urgoo.business.BaseService;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorData;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.event.CounselorEvent;
import com.urgoo.counselor.model.CounselorDetail;
import com.urgoo.counselor.model.CounselorDetailSubList;
import com.urgoo.counselor.model.CounselorServiceList;
import com.urgoo.counselor.model.EduList;
import com.urgoo.counselor.event.FocusEvent;
import com.urgoo.counselor.model.LabelList;
import com.urgoo.counselor.model.ServiceLongList;
import com.urgoo.counselor.model.Works;
import com.urgoo.counselor.model.experienceList;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.domain.ShareDetail;
import com.urgoo.flashview.listener.FlashViewListener;
import com.urgoo.message.activities.ChatActivity;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.urgoo.order.ServiceActivity;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.schedule.activites.Precontract;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;
import com.urgoo.view.CounselorBannerView;
import com.urgoo.view.FlowRadioGroup;
import com.urgoo.view.MyScrollView;
import com.urgoo.zhibo.activities.ZhiBodDetailActivity;
import com.zw.express.tool.PickUtils;
import com.zw.express.tool.Util;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by dff on 2016/7/7.
 */
public class CounselorActivity extends ActivityBase implements View.OnClickListener,
        MyScrollView.OnScrollListener {
    private static final int HANDLER_SERVER = 1;
    private static final int HANDLER_BANNA = 2;
    private static final int HANDLER_INFO = 0;
    public static final String COUNSELOR_ID = "counselorId";
    public String zhibo = "1";
    private RelativeLayout Relative_cont_Layout2;
    private RelativeLayout Relative_cont_Layout;
    private RelativeLayout RelativeLayout_top;
    private LinearLayout Linear_cont_Layouts;
    private LinearLayout Linear_cont_Layout;
    private LinearLayout Linear_cont_ljpy;
    private LinearLayout LinLyout_myorder_back2;
    private LinearLayout Linear_ImmediatelyTop;
    private Boolean figs = true;
    private Boolean figsinfo = false;
    private Boolean figsserver = false;
    private Boolean figsbanna = false;
    private String TAG = this.getClass().getName();
    private ImageView im_cont_shang1;
    private ImageView im_cont_shang2;
    private ImageView im_cont_shang3;
    private ImageView im_cont_shang4;
    private ImageView im_cont_xing;
    private ImageView im_cont_xing2;
    private ImageView img_title_xing;
    private ImageView img_title_fenixang;
    private ImageView img_title_xing2;
    private ImageView img_news;
    private ImageView im_cont_fenxiang;
    private TextView tv_cont_opin;
    private ViewGroup.LayoutParams lp;
    private ViewGroup.LayoutParams lp2;
    private MyScrollView ScrollView;
    private ArrayList<String> list1 = new ArrayList<>();
    private int searchLayoutTop;
    private int ImmediatelyTop;
    private int mDeviceHeight;
    private TextView tv_cont_countyName;
    private TextView tv_cont_work;
    private TextView tv_cont_Name;
    //    private TextView tv_cont_serviceTotal;
//    private TextView tv_cont_serviceCounted;
    //    private TextView tv_cont_GraduateSchool;
    private ListView tv_cont_GraduateSchool;
    private TextView myorder_message_title;
    private TextView tv_cont_guide;
    private TextView tv_cont_consultants;
    private TextView tv_cont_Friendship;
    private TextView tv_cont_Successfulcases;
    private TextView tv_cont_Friendship2;
    private TextView tv_cont_Expectations;
    private TextView tv_cont_Consultantworks_title;
    private TextView tv_cont_Friendship3;
    private TextView tv_cont_Consultantworks_cont;
    private TextView tv_cont_More;
    private TextView tv_cont_zhibo;
    private TextView tv_cont_organization;
    private RelativeLayout Relative_cont_Friendship;
    private RelativeLayout Relative_cont_Friendship2;
    private RelativeLayout Relative_cont_Friendship3;
    private RelativeLayout Relative_cont_Layout_Zhi;
    private LinearLayout Linear_cont_Layout_Successfulcases;
    private LinearLayout Linear_cont_Layout_Expectations;
    private LinearLayout Linear_cont_Layout_Consultantworks;
    private LinearLayout Linear_cont_Layout_organization;
    private TextView tv_cont_habitualResidence;
    private FlowRadioGroup flow_cont;
    private TextView tv_cont_attentionCount;
    private TextView tv_cont_counselorReadCount;
    private TextView tv_cont_advanceCount;
    private TextView tv_cont_levelEducation;
    private TextView tv_cont_slogan;
    private TextView tv_cont_urgoo;
    private TextView tv_cont_guanzhu;
    private TextView tv_cont_fenxiang;
    private TextView tv_cont_fwbz;
    private ListView list_cont_workyers;
    private ListView list_cont_Service;
    private ListView list_cont_counselorserver;
    private LayoutInflater layoutInflater;
    private LinearLayout Linear_cont_Layout_fuwu;
    private RelativeLayout but_cont_make;
    private String str = "未填写";
    private String first = "\u3000\u3000\u3000\u3000\u3000";
    private Handler mHandler;
    private List<String> imageUrls;
    private String counselorId;
    private CounselorBannerView bannerView;
    private LinearLayout LinLyout_myorder_back;
    private ArrayList<CounselorDetailSubList> mDetailSubList = new ArrayList<>();
    private ArrayList<LabelList> mLabelList = new ArrayList<>();
    private ArrayList<experienceList> mExperienceList = new ArrayList<>();
    private ArrayList<Works> mWorksList = new ArrayList<>();
    private ArrayList<EduList> mEduList = new ArrayList<>();
    private ArrayList<ServiceLongList> mServiceLongList = new ArrayList<>();
    private CounselorDetail mCounselorDetail;
    private ShareDetail shareDetail;
    private ArrayList<CounselorServiceList> mCounselorServiceLis = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        getZOOMInfo();
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra(COUNSELOR_ID) != null) {
            counselorId = mIntent.getStringExtra(COUNSELOR_ID);
            if (!Util.isEmpty(mIntent.getStringExtra("zhibo"))) {
                zhibo = mIntent.getStringExtra("zhibo");
                Log.d(TAG, "zhibo :  " + zhibo);
            }
            Log.d(TAG, "counselorId :  " + counselorId);
            showLoadingDialog();
            CounselorManager.getInstance(this).getStatCounselorCount(this, counselorId);
            CounselorManager.getInstance(this).getCounselorDetailSubList(this, counselorId);
            CounselorManager.getInstance(this).getCounselorInfo(this, counselorId);
            CounselorManager.getInstance(this).getCounselorServer(this, counselorId);
        } else {
            Toast.makeText(CounselorActivity.this, "网络请求失败，请检查网络后重试!", Toast.LENGTH_SHORT).show();
            finish();
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == HANDLER_INFO) {
                    initDatas();
                    CounselorData mData = new CounselorData();
                    mData.setCounselorServiceLis(mServiceLongList);
                }
                if (msg.what == HANDLER_SERVER) {
                    initDataServer();
                    getListHeight(list_cont_counselorserver);
                }
                if (msg.what == HANDLER_BANNA) {
                    inieDataBanna();
                }
            }
        };

        initview();                     //  初始化控件
        initdata();                     //  初始化数据
        initListenter();                //  点击接口定义

    }

    //  得到数据后的BANNA操作
    private void inieDataBanna() {
        imageUrls = new ArrayList<>();
        imageUrls.clear();
        for (int i = 0; i < mDetailSubList.size(); i++) {
            if (mDetailSubList.get(i).getType().equals("1")) {          //  type 为1 表示当前为视频
                imageUrls.add(mDetailSubList.get(i).getVideoPic());
                Log.d(TAG, "视频状态图片  " + mDetailSubList.get(i).getVideoPic());
            } else if (mDetailSubList.get(i).getType().equals("0")) {   //  type 为0 表示当前为图片
                imageUrls.add(mDetailSubList.get(i).getUrl());
                Log.d(TAG, "图片  " + mDetailSubList.get(i).getUrl());
            }
        }
        bannerView.init(imageUrls);
        if (imageUrls.size() > 1) { //  因为FlashView无图片可展示时，也会默认有一张图片，所以当其数据小于1条时，不让其具备点击
            bannerView.setOnPageClickListener(new FlashViewListener() {
                @Override
                public void onClick(int position) { //  点击时，传入当前点击的位置
                    if (mDetailSubList.get(position).getType().equals("1")) {          //  type 为1 表示当前为视频
                        broadcastVideo(mDetailSubList.get(position).getUrl());
                    } else {
                        Intent intent = new Intent(CounselorActivity.this, CounselorContActivity.class);
                        intent.putExtra("postion", String.valueOf(position));
                        intent.putExtra("counselorId", counselorId);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    //  顾问的基本数据得到后的填充
    private void initDatas() {

        //  顾问姓名
        if (!Util.isEmpty(mCounselorDetail.getEnName().trim())) {
            tv_cont_Name.setText(mCounselorDetail.getEnName());
            myorder_message_title.setText(mCounselorDetail.getEnName());
        }
        //  是否关注 inAttention
        if (!Util.isEmpty(mCounselorDetail.getIsAttention().trim()))
            if (mCounselorDetail.getIsAttention().equals("1")) {
                im_cont_xing.setVisibility(View.GONE);          //  已关注
                im_cont_xing2.setVisibility(View.VISIBLE);
                img_title_xing.setVisibility(View.GONE);
                img_title_xing2.setVisibility(View.VISIBLE);
            } else if (mCounselorDetail.getIsAttention().equals("0")) {
                im_cont_xing.setVisibility(View.VISIBLE);       //  未关注
                im_cont_xing2.setVisibility(View.GONE);
                img_title_xing.setVisibility(View.VISIBLE);
                img_title_xing2.setVisibility(View.GONE);
            }

        //  **人已关注
        if (!Util.isEmpty(mCounselorDetail.getAttentionCount().trim()))
            tv_cont_attentionCount.setText(mCounselorDetail.getAttentionCount() + "人已关注");
        else
            tv_cont_attentionCount.setText("0人已关注");

        //  **人已预约
        if (!Util.isEmpty(mCounselorDetail.getAdvanceCount().trim()))
            tv_cont_advanceCount.setText(mCounselorDetail.getAdvanceCount() + "人已预约");
        else
            tv_cont_advanceCount.setText("0人已预约");

        //  **人已查看
        if (!Util.isEmpty(mCounselorDetail.getCounselorReadCount().trim()))
            tv_cont_counselorReadCount.setText(mCounselorDetail.getCounselorReadCount() + "人已查看");
        else
            tv_cont_advanceCount.setText("0人已查看");

        //  工作年限
        if (!Util.isEmpty(mCounselorDetail.getWorkYear().trim()))
            tv_cont_work.setText(mCounselorDetail.getWorkYear());
        else
            tv_cont_work.setText("0 年");

        //  学历
        if (!Util.isEmpty(mCounselorDetail.getLevelEducation().trim()))
            tv_cont_levelEducation.setText(mCounselorDetail.getLevelEducation());
        else
            tv_cont_levelEducation.setText(str);

        //直播
        if (!Util.isEmpty(mCounselorDetail.getLiveId().trim())) {
            Relative_cont_Layout_Zhi.setVisibility(View.VISIBLE);
        } else {
            Relative_cont_Layout_Zhi.setVisibility(View.GONE);
        }

        //  国籍
        if (!Util.isEmpty(mCounselorDetail.getCountyName().trim()))
            tv_cont_countyName.setText(mCounselorDetail.getCountyName());
        else
            tv_cont_countyName.setText(str);

        //  口号 最多显示3行，超出则省略不显示
        if (!Util.isEmpty(mCounselorDetail.getSlogan().trim()))
            tv_cont_slogan.setText(mCounselorDetail.getSlogan());
        else
            tv_cont_slogan.setText(str);

        //  常驻地
        if (!Util.isEmpty(mCounselorDetail.getHabitualResidence().trim())) {
            tv_cont_habitualResidence.setText(mCounselorDetail.getHabitualResidence());
        } else {
            tv_cont_habitualResidence.setText(str);
        }

        //  资格认证
        if (!Util.isEmpty(mCounselorDetail.getOrganization().trim())) {
            Linear_cont_Layout_organization.setVisibility(View.VISIBLE);
            tv_cont_organization.setText(mCounselorDetail.getOrganization());
        } else {
            Linear_cont_Layout_organization.setVisibility(View.GONE);
        }

        //  个性标签
        if (mLabelList.size() > 0) {
            flow_cont.setVisibility(View.VISIBLE);
            initDataTab();
        } else {
            flow_cont.setVisibility(View.GONE);
        }

        //  顾问作品
        if (mWorksList.size() != 0 && mWorksList.size() > 0) {
            Linear_cont_Layout_Consultantworks.setVisibility(View.VISIBLE);
            tv_cont_Consultantworks_title.setText(mWorksList.get(0).getTitle());
            tv_cont_Consultantworks_cont.setText(mWorksList.get(0).getContent());
        } else {
            Linear_cont_Layout_Consultantworks.setVisibility(View.GONE);
        }

        //  可接收总数
//        if (!Util.isEmpty(mCounselorDetail.getServiceTotal().trim())) {
//            tv_cont_serviceTotal.setText("/ " + mCounselorDetail.getServiceTotal());
//        } else {
//            tv_cont_serviceTotal.setText(str);
//        }

        //  已接收数
//        if (!Util.isEmpty(mCounselorDetail.getServiceCounted().trim())) {
//            tv_cont_serviceCounted.setText(mCounselorDetail.getServiceCounted());
//        } else {
//            tv_cont_serviceCounted.setText(str);
//        }

//        //  毕业院校
//        if (!Util.isEmpty(mCounselorDetail.getEducationBg().trim()))
//            tv_cont_GraduateSchool.setText(mCounselorDetail.getEducationBg());
//        else
//            tv_cont_GraduateSchool.setText(str);

        //  毕业院校

        tv_cont_GraduateSchool.setAdapter(new SchoolServerAdapter());
        getListHeight(tv_cont_GraduateSchool);

        //  指导方式
        if (!Util.isEmpty(mCounselorDetail.getServiceMode().trim())) {
            tv_cont_guide.setText(mCounselorDetail.getServiceMode());
        } else
            tv_cont_guide.setVisibility(View.GONE);

        //  工作经验
        list_cont_workyers.setAdapter(new WorkYersAdapter());
        getListHeight(list_cont_workyers);

        //  顾问介绍
        if (!Util.isEmpty(mCounselorDetail.getSelfBio().trim())) {
            tv_cont_consultants.setText(mCounselorDetail.getSelfBio());
            //  顾问介绍翻译
            if (!Util.isEmpty(mCounselorDetail.getSelfBioTranz().trim())) {
                Relative_cont_Friendship.setVisibility(View.VISIBLE);
                tv_cont_Friendship.setText(first + mCounselorDetail.getSelfBioTranz());
            } else {
                Relative_cont_Friendship.setVisibility(View.GONE);
            }
        } else {
            tv_cont_consultants.setText("未填写");
        }

        //  期待
        if (!Util.isEmpty(mCounselorDetail.getRequires().trim())) {
            Linear_cont_Layout_Expectations.setVisibility(View.VISIBLE);
            tv_cont_Expectations.setText(mCounselorDetail.getRequires());
            //  期待翻译
            if (!Util.isEmpty(mCounselorDetail.getRequiresTranz().trim())) {
                Relative_cont_Friendship3.setVisibility(View.VISIBLE);
                tv_cont_Friendship3.setText(first + mCounselorDetail.getRequiresTranz());
            } else {
                Relative_cont_Friendship3.setVisibility(View.GONE);
            }
        } else {
            Linear_cont_Layout_Expectations.setVisibility(View.GONE);
        }

        //  成功案例
        if (!Util.isEmpty(mCounselorDetail.getSuccessCase().trim())) {
            Linear_cont_Layout_Successfulcases.setVisibility(View.VISIBLE);
            tv_cont_Successfulcases.setText(mCounselorDetail.getSuccessCase());
            //  成功案例翻译
            if (!Util.isEmpty(mCounselorDetail.getSuccessCaseTranz().trim())) {
                Relative_cont_Friendship2.setVisibility(View.VISIBLE);
                tv_cont_Friendship2.setText(first + mCounselorDetail.getSuccessCaseTranz());
            } else {
                Relative_cont_Friendship2.setVisibility(View.GONE);
            }
        } else {
            Linear_cont_Layout_Successfulcases.setVisibility(View.GONE);
        }

        //  顾问提醒
        if (!Util.isEmpty(mCounselorDetail.getExtraService().trim())) {
            Linear_cont_Layout_fuwu.setVisibility(View.VISIBLE);
            tv_cont_fwbz.setText(mCounselorDetail.getExtraService());
        } else {
            Linear_cont_Layout_fuwu.setVisibility(View.GONE);
        }

        //  服务内容
        //  先显示3条数据，展开布局后，方显示所有的数据
//        if (mServiceLongList != null) {
//            if (mServiceLongList.size() > 3) {
//                for (int i = 0; i < 2; i++) {
//                    list1.add(mServiceLongList.get(i).getDetailed());
//                }
//                list1.add("..."); //  先显示两条数据，第三条为...省略号
//            } else {
//                for (int i = 0; i < mServiceLongList.size(); i++) {
//                    list1.add(mServiceLongList.get(i).getDetailed());
//                }
//                list1.add("..."); //  先显示两条数据，第三条为...省略号
//            }
//        }
        //  服务内容，根据布局展不展开传入Boolean值，false为为展开状态，值显示3条

        list_cont_Service.setAdapter(new ServiceLongListAdapter(true));
        getListHeight(list_cont_Service);

    }

    //  顾问服务内容
    private void initDataServer() {
        list_cont_counselorserver.setAdapter(new CounselorServerAdapter());
    }

    //  初始化部分不变数据
    private void initdata() {
        mDeviceHeight = Util.getDeviceHeight(this);
        //文本添加下划线
        tv_cont_opin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_cont_opin.getPaint().setAntiAlias(true);//抗锯齿)
        Linear_cont_Layout.setMinimumHeight(Linear_cont_Layout.getMeasuredHeight());
        lp = Linear_cont_Layout.getLayoutParams();
//        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = (int) (mDeviceHeight * 0.6);
//        Linear_cont_Layouts.setMinimumHeight(Linear_cont_Layouts.getMeasuredHeight());
//        lp2 = Linear_cont_Layouts.getLayoutParams();
//        lp2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    private void initListenter() {
        Relative_cont_Layout.setOnClickListener(this);
        Relative_cont_Layout2.setOnClickListener(this);
        tv_cont_More.setOnClickListener(this);
        Linear_cont_ljpy.setOnClickListener(this);
        im_cont_xing.setOnClickListener(this);
        im_cont_xing2.setOnClickListener(this);
        img_title_xing.setOnClickListener(this);
        img_title_xing2.setOnClickListener(this);
        tv_cont_opin.setOnClickListener(this);
        tv_cont_urgoo.setOnClickListener(this);
//        but_cont_contact.setOnClickListener(this);
        but_cont_make.setOnClickListener(this);
        LinLyout_myorder_back.setOnClickListener(this);
        LinLyout_myorder_back2.setOnClickListener(this);
        im_cont_fenxiang.setOnClickListener(this);
        img_title_fenixang.setOnClickListener(this);
        tv_cont_zhibo.setOnClickListener(this);
        img_news.setOnClickListener(this);
    }

    //  个性标签实现方法
    private void initDataTab() {
        for (int i = 0; i < mLabelList.size(); i++) {
            if (mLabelList.get(i) != null) {
                View mInflate = LayoutInflater.from(this).inflate(R.layout.frb_search_view_item, null);
                RadioButton rbSearch = (RadioButton) mInflate.findViewById(R.id.rb_search2);
                rbSearch.setText(mLabelList.get(i).getLableCnName() + "");
                flow_cont.addView(mInflate);
            }
        }
    }

    //  布局初始化后调用，获取布局中的位置
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int width = Util.getDeviceWidth(CounselorActivity.this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width / 7, width / 7);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, (int) ((0.773f * width) - width / 13), width / 6, 0);
        im_cont_xing.setLayoutParams(lp);
        im_cont_xing2.setLayoutParams(lp);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(width / 7, width / 7);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.setMargins(0, (int) ((0.773f * width) - width / 13), width / 36, 0);
        im_cont_fenxiang.setLayoutParams(lp2);
        im_cont_fenxiang.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(width / 7, width / 7);
        lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp3.setMargins(0, (int) ((0.773f * width) + width / 20), (int) (width / 8.1), 0);
        tv_cont_guanzhu.setLayoutParams(lp3);
        tv_cont_guanzhu.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(width / 7, width / 7);
        lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp4.setMargins(0, (int) ((0.773f * width) + width / 20), -(width / 50), 0);
        tv_cont_fenxiang.setLayoutParams(lp4);
        tv_cont_fenxiang.setVisibility(View.VISIBLE);


        if (hasFocus) {
            searchLayoutTop = (int) ((0.700f * width));//获取圆标的top位置
            ImmediatelyTop = Linear_ImmediatelyTop.getBottom();//获取聘用的top位置
            Log.d(TAG, " ImmediatelyTop : " + ImmediatelyTop);
        }

        if (SPManager.getInstance(this).getLogins().equals("1")) {
            img_news.setVisibility(View.GONE);
        } else {
            img_news.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Relative_cont_Layout:    //   关于顾问卡片收缩
                if (figs) {
                    im_cont_shang1.setVisibility(View.VISIBLE);
                    im_cont_shang2.setVisibility(View.GONE);
                    lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    figs = false;
                } else {
                    im_cont_shang1.setVisibility(View.GONE);
                    im_cont_shang2.setVisibility(View.VISIBLE);
                    lp.height = (int) (mDeviceHeight * 0.6);
//                    ScrollView.smoothScrollTo(0, 0);
                    ScrollView.fullScroll(ScrollView.FOCUS_UP);
                    RelativeLayout_top.setVisibility(View.VISIBLE);
                    figs = true;
                }
                break;

            case R.id.Relative_cont_Layout2:    //   服务介绍卡片收缩
//                if (figs2) {
//                    im_cont_shang3.setVisibility(View.GONE);
//                    im_cont_shang4.setVisibility(View.VISIBLE);
//                    lp2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    list1.clear();
//                    if (mServiceLongList != null) {
//                        if (mServiceLongList.size() > 3) {
//                            for (int i = 0; i < 2; i++) {
//                                list1.add(mServiceLongList.get(i).getDetailed());
//                            }
//                            list1.add("..."); //  先显示两条数据，第三条为...省略号
//                        } else {
//                            for (int i = 0; i < mServiceLongList.size(); i++) {
//                                list1.add(mServiceLongList.get(i).getDetailed());
//                            }
//                            list1.add("..."); //  先显示两条数据，第三条为...省略号
//                        }
//                    }
//                    list_cont_Service.setAdapter(new ServiceLongListAdapter(false));
//                    getListHeight(list_cont_Service);
//                    figs2 = false;
//                } else {
//                    im_cont_shang3.setVisibility(View.VISIBLE);
//                    im_cont_shang4.setVisibility(View.GONE);
//                    lp2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    list_cont_Service.setAdapter(new ServiceLongListAdapter(true));
//                    getListHeight(list_cont_Service);
//                    figs2 = true;
//                }
                break;

            case R.id.Linear_cont_ljpy:          //   点了立即聘用按钮
                RelativeLayout_top.setVisibility(View.VISIBLE);
                ScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;

            case R.id.im_cont_xing:             //   关注
                getaddFollow();
                break;

            case R.id.im_cont_xing2:            //   取消关注
                getCancleFollow();
                break;

            case R.id.img_title_xing:           //   关注
                getaddFollow();
                break;

            case R.id.img_title_xing2:          //   取消关注
                getCancleFollow();
                break;

            case R.id.tv_cont_opin:             //   拨号
                PickUtils.doPhone(CounselorActivity.this, tv_cont_opin.getText().toString());
                Log.d(TAG, "电话号码 ：" + tv_cont_opin.getText().toString());
                break;

            case R.id.tv_cont_urgoo:            //   优宝
                startActivity(new Intent(CounselorActivity.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
                break;

            case R.id.but_cont_make:            //   预约
                if (!Util.isEmpty(mCounselorDetail.getIsAdvanceRelation().trim())) {
                    // 如果为 0 ，表示该家长和该顾问没有联系，点击后调转到预约界面
                    if (mCounselorDetail.getIsAdvanceRelation().equals("0")) {
                        startActivity(new Intent(CounselorActivity.this, Precontract.class).putExtra("counselorId", mCounselorDetail.getCounselorId()));
                        Log.d(TAG, "跳转页面 : Precontract " + mCounselorDetail.getCounselorId());
                    } else if (mCounselorDetail.getIsAdvanceRelation().equals("1")) {

                        // 如果为 1 ，表示该家长和该顾问有联系，点击后调转到预约详情界面
                        startActivity(new Intent(CounselorActivity.this, PrecontractMyOrderContent.class)
                                .putExtra("status", mCounselorDetail.getStatus())
                                .putExtra("advanceId", mCounselorDetail.getAdvanceId())
                                .putExtra("type", mCounselorDetail.getType()));
                        Log.d(TAG, "跳转页面 : PrecontractMyOrderContent  " + mCounselorDetail.getStatus() + "  AdvanceId: "
                                + mCounselorDetail.getAdvanceId() + " type :" + mCounselorDetail.getType());
                    }
                }

                break;

            case R.id.LinLyout_myorder_back:    //   回退箭头
                if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                    Bundle extras = new Bundle();
                    extras.putInt(MainActivity.EXTRA_TAB, 0);
                    Util.openActivityWithBundle(CounselorActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                finish();
                break;

            case R.id.LinLyout_myorder_back2:    //   回退箭头
                if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                    Bundle extras = new Bundle();
                    extras.putInt(MainActivity.EXTRA_TAB, 0);
                    Util.openActivityWithBundle(CounselorActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                finish();
                break;

            case R.id.tv_cont_zhibo:            //   查看他的直播
                if (zhibo.equals("0")) {
                    finish();
                } else {
                    startActivity(new Intent(CounselorActivity.this, ZhiBodDetailActivity.class)
                            .putExtra("liveId", mCounselorDetail.getLiveId())
                            .putExtra("zhibo", "0"));
                }
                break;

            case R.id.img_news:                 //   引导
                SPManager.getInstance(this).setLogins("1");
                img_news.setVisibility(View.GONE);
                break;

            case R.id.tv_cont_More:             //   更多
                if (!Util.isEmpty(counselorId)) {
                    startActivity(new Intent(CounselorActivity.this, WorksActivity.class).putExtra("counselorId", counselorId));
                }
                break;
            case R.id.im_cont_fenxiang:         //   分享
                ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, ZWConfig.URGOOURL_BASE + shareDetail.url);
                break;
            case R.id.img_title_fenixang:       //   分享
                ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, ZWConfig.URGOOURL_BASE + shareDetail.url);
                break;
        }
    }

    //  计算list实际高度
    private void getListHeight(ListView mlistview) {
        ListAdapter listAdapter = mlistview.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, mlistview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mlistview.getLayoutParams();
        params.height = totalHeight
                + (mlistview.getDividerHeight() * (listAdapter.getCount() - 1));
        mlistview.setLayoutParams(params);
    }

    //  MyScrollView接口回调
    public void onScroll(int scrollY) {
        if (scrollY > 0 && scrollY <= searchLayoutTop) {
            RelativeLayout_top.setVisibility(View.GONE);
        } else if (scrollY <= 0) {
            RelativeLayout_top.setVisibility(View.GONE);
        } else
            RelativeLayout_top.setVisibility(View.VISIBLE);
    }

    //  取消关注 interface
    private void getCancleFollow() {
        CounselorManager.getInstance(this).getCancleFollow(this, counselorId);
    }

    //  关注 interface
    private void getaddFollow() {
        CounselorManager.getInstance(this).getaddFollow(this, counselorId);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        switch (eventCode) {
            //  顾问被查看
            case EventCodeStatCounselorCount:
                Log.d(" dff ", " 添加查看次数成功 ");
                break;
            //  关注
            case EventCodeAddFollow:
                dismissLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result.get("header").toString());
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {   //  关注成功
                        Toast.makeText(CounselorActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        im_cont_xing.setVisibility(View.GONE);
                        im_cont_xing2.setVisibility(View.VISIBLE);
                        img_title_xing.setVisibility(View.GONE);
                        img_title_xing2.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new FocusEvent());
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            //  取消关注
            case EventCodeCancleFollow:
                dismissLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result.get("header").toString());
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {   //  取消关注
                        Toast.makeText(CounselorActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                        im_cont_xing.setVisibility(View.VISIBLE);
                        im_cont_xing2.setVisibility(View.GONE);
                        img_title_xing.setVisibility(View.VISIBLE);
                        img_title_xing2.setVisibility(View.GONE);
                        EventBus.getDefault().post(new FocusEvent());
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            // BANNA http
            case EventCodeSelectCounselorDetailSubList:
                figsbanna = true;
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mDetailSubList = gson.fromJson(jsonObject.getJSONArray("counselorDetailSubList").toString(), new TypeToken<ArrayList<CounselorDetailSubList>>() {
                    }.getType());
                    Log.d(" dff ", "mDetailSubList: " + mDetailSubList.toString());
                    Message msg = new Message();
                    msg.what = HANDLER_BANNA;
                    mHandler.sendMessage(msg);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            //  基本信息 http
            case EventCodeFindCounselorDetail:
                figsinfo = true;
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mCounselorDetail = gson.fromJson(jsonObject.getJSONObject("counselorDetail").toString(), new TypeToken<CounselorDetail>() {
                    }.getType());
                    shareDetail = gson.fromJson(jsonObject.getJSONObject("shareDetail").toString(), new TypeToken<ShareDetail>() {
                    }.getType());
                    mLabelList = gson.fromJson(jsonObject.getJSONArray("labelList").toString(), new TypeToken<ArrayList<LabelList>>() {
                    }.getType());
                    mExperienceList = gson.fromJson(jsonObject.getJSONArray("experienceList").toString(), new TypeToken<ArrayList<experienceList>>() {
                    }.getType());
                    mServiceLongList = gson.fromJson(jsonObject.getJSONArray("serviceLongList").toString(), new TypeToken<ArrayList<ServiceLongList>>() {
                    }.getType());
                    mWorksList = gson.fromJson(jsonObject.getJSONArray("works").toString(), new TypeToken<ArrayList<Works>>() {
                    }.getType());
                    mEduList = gson.fromJson(jsonObject.getJSONArray("eduList").toString(), new TypeToken<ArrayList<EduList>>() {
                    }.getType());
                    Log.d(" dff ", "mCounselorDetail: " + mCounselorDetail.toString());
                    Log.d(" dff ", "mLabelList: " + mLabelList.toString());
                    Log.d(" dff ", "mServiceLongList: " + mServiceLongList.toString());
                    Log.d(" dff ", "mWorksList: " + mWorksList.toString());
                    Log.d(" dff ", "mEduList: " + mEduList.toString());
                    Log.d(" dff ", "shareDetail: " + shareDetail.toString());
                    Log.d(" dff ", "mExperienceList: " + mExperienceList.toString());
                    Message msg = new Message();
                    msg.what = HANDLER_INFO;
                    mHandler.sendMessage(msg);

                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            // 顾问详情 服务项目
            case EventCodeSelectCounselorServiceList:
                figsserver = true;
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mCounselorServiceLis = gson.fromJson(jsonObject.getJSONArray("counselorServiceList").toString(), new TypeToken<ArrayList<CounselorServiceList>>() {
                    }.getType());
                    Log.d(" dff ", "mCounselorServiceLis: " + mCounselorServiceLis.toString());
                    Message msg = new Message();
                    msg.what = HANDLER_SERVER;
                    mHandler.sendMessage(msg);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }

        if (figsserver && figsinfo && figsbanna) {
            dismissLoadingDialog();
        }
    }

    private void initview() {
        layoutInflater = LayoutInflater.from(CounselorActivity.this);
        Relative_cont_Layout = (RelativeLayout) findViewById(R.id.Relative_cont_Layout);
        bannerView = (CounselorBannerView) findViewById(R.id.banner);
        Relative_cont_Layout2 = (RelativeLayout) findViewById(R.id.Relative_cont_Layout2);
        Relative_cont_Friendship = (RelativeLayout) findViewById(R.id.Relative_cont_Friendship);
        Relative_cont_Friendship2 = (RelativeLayout) findViewById(R.id.Relative_cont_Friendship2);
        Relative_cont_Friendship3 = (RelativeLayout) findViewById(R.id.Relative_cont_Friendship3);
        Relative_cont_Layout_Zhi = (RelativeLayout) findViewById(R.id.Relative_cont_Layout_Zhi);
        Linear_cont_Layout_Successfulcases = (LinearLayout) findViewById(R.id.Linear_cont_Layout_Successfulcases);
        Linear_cont_Layout_Expectations = (LinearLayout) findViewById(R.id.Linear_cont_Layout_Expectations);
        Linear_cont_Layout_Consultantworks = (LinearLayout) findViewById(R.id.Linear_cont_Layout_Consultantworks);
        Linear_cont_Layout_organization = (LinearLayout) findViewById(R.id.Linear_cont_Layout_organization);
        RelativeLayout_top = (RelativeLayout) findViewById(R.id.RelativeLayout_top);
        Linear_cont_Layout = (LinearLayout) findViewById(R.id.Linear_cont_Layout);
        Linear_cont_Layout_fuwu = (LinearLayout) findViewById(R.id.Linear_cont_Layout_fuwu);
        Linear_cont_Layouts = (LinearLayout) findViewById(R.id.Linear_cont_Layouts);
        LinLyout_myorder_back2 = (LinearLayout) findViewById(R.id.LinLyout_myorder_back2);
        Linear_ImmediatelyTop = (LinearLayout) findViewById(R.id.Linear_ImmediatelyTop);
        LinLyout_myorder_back = (LinearLayout) findViewById(R.id.LinLyout_myorder_back);
        Linear_cont_ljpy = (LinearLayout) findViewById(R.id.Linear_cont_ljpy);
        im_cont_shang1 = (ImageView) findViewById(R.id.im_cont_shang1);
        im_cont_fenxiang = (ImageView) findViewById(R.id.im_cont_fenxiang);
        img_title_fenixang = (ImageView) findViewById(R.id.img_title_fenixang);
        img_title_xing = (ImageView) findViewById(R.id.img_title_xing);
        img_title_xing2 = (ImageView) findViewById(R.id.img_title_xing2);
        img_news = (ImageView) findViewById(R.id.img_news);
        im_cont_shang2 = (ImageView) findViewById(R.id.im_cont_shang2);
        im_cont_shang3 = (ImageView) findViewById(R.id.im_cont_shang3);
        im_cont_shang4 = (ImageView) findViewById(R.id.im_cont_shang4);
        im_cont_xing = (ImageView) findViewById(R.id.im_cont_xing);
        im_cont_xing2 = (ImageView) findViewById(R.id.im_cont_xing2);
        tv_cont_opin = (TextView) findViewById(R.id.tv_cont_opin);
        tv_cont_urgoo = (TextView) findViewById(R.id.tv_cont_urgoo);
        tv_cont_guanzhu = (TextView) findViewById(R.id.tv_cont_guanzhu);
        tv_cont_fenxiang = (TextView) findViewById(R.id.tv_cont_fenxiang);
        tv_cont_fwbz = (TextView) findViewById(R.id.tv_cont_fwbz);
        tv_cont_habitualResidence = (TextView) findViewById(R.id.tv_cont_habitualResidence);
        flow_cont = (FlowRadioGroup) findViewById(R.id.flow_cont);
        tv_cont_attentionCount = (TextView) findViewById(R.id.tv_cont_attentionCount);
        tv_cont_counselorReadCount = (TextView) findViewById(R.id.tv_cont_counselorReadCount);
        tv_cont_advanceCount = (TextView) findViewById(R.id.tv_cont_advanceCount);
        tv_cont_slogan = (TextView) findViewById(R.id.tv_cont_slogan);
        tv_cont_levelEducation = (TextView) findViewById(R.id.tv_cont_levelEducation);
        myorder_message_title = (TextView) findViewById(R.id.myorder_message_title);
        tv_cont_guide = (TextView) findViewById(R.id.tv_cont_guide);
        tv_cont_More = (TextView) findViewById(R.id.tv_cont_More);
        tv_cont_zhibo = (TextView) findViewById(R.id.tv_cont_zhibo);
        tv_cont_organization = (TextView) findViewById(R.id.tv_cont_organization);
        tv_cont_Name = (TextView) findViewById(R.id.tv_cont_Name);
//        tv_cont_serviceTotal = (TextView) findViewById(R.id.tv_cont_serviceTotal);
//        tv_cont_serviceCounted = (TextView) findViewById(R.id.tv_cont_serviceCounted);
        tv_cont_countyName = (TextView) findViewById(R.id.tv_cont_countyName);
        tv_cont_work = (TextView) findViewById(R.id.tv_cont_work);
        tv_cont_organization = (TextView) findViewById(R.id.tv_cont_organization);
        tv_cont_GraduateSchool = (ListView) findViewById(R.id.tv_cont_GraduateSchool);
//        tv_cont_GraduateSchool = (TextView) findViewById(R.id.tv_cont_GraduateSchool);
        tv_cont_consultants = (TextView) findViewById(R.id.tv_cont_consultants);
        tv_cont_Friendship = (TextView) findViewById(R.id.tv_cont_Friendship);
        tv_cont_Successfulcases = (TextView) findViewById(R.id.tv_cont_Successfulcases);
        tv_cont_Friendship2 = (TextView) findViewById(R.id.tv_cont_Friendship2);
        tv_cont_Expectations = (TextView) findViewById(R.id.tv_cont_Expectations);
        tv_cont_Consultantworks_title = (TextView) findViewById(R.id.tv_cont_Consultantworks_title);
        tv_cont_Friendship3 = (TextView) findViewById(R.id.tv_cont_Friendship3);
        tv_cont_Consultantworks_cont = (TextView) findViewById(R.id.tv_cont_Consultantworks_cont);
        ScrollView = (MyScrollView) findViewById(R.id.ScrollView);
        list_cont_workyers = (ListView) findViewById(R.id.list_cont_workyers);
        list_cont_Service = (ListView) findViewById(R.id.list_cont_ServiceLongList);
        list_cont_counselorserver = (ListView) findViewById(R.id.list_cont_counselorserver);
        but_cont_make = (RelativeLayout) findViewById(R.id.but_cont_make);
//        but_cont_contact = (Button) findViewById(R.id.but_cont_contact);
        ScrollView.setOnScrollListener(this);
        ScrollView.setFocusable(false);
        list_cont_workyers.setFocusable(false);
        list_cont_Service.setFocusable(false);
        list_cont_counselorserver.setFocusable(false);
        tv_cont_GraduateSchool.setFocusable(false);
        ScrollView.smoothScrollTo(0, 0);
        EventBus.getDefault().register(this);
    }

    //  工作经验的 adapter
    private class WorkYersAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mExperienceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mExperienceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.conunselor_workyers_item, parent, false);
                viewHolder.tv_content_position = (TextView) convertView.findViewById(R.id.tv_content_position);
                viewHolder.tv_content_timedata = (TextView) convertView.findViewById(R.id.tv_content_timedata);
                viewHolder.tv_content_companyName = (TextView) convertView.findViewById(R.id.tv_content_companyName);
                viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                viewHolder.item_img.setVisibility(View.GONE);
            } else
                viewHolder.item_img.setVisibility(View.VISIBLE);
            viewHolder.tv_content_companyName.setText(mExperienceList.get(position).getCompanyName());
            viewHolder.tv_content_position.setText(mExperienceList.get(position).getPosition());
            viewHolder.tv_content_timedata.setText(mExperienceList.get(position).getStartDate() + " - " + mExperienceList.get(position).getEndDate());
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_content_companyName;
            private TextView tv_content_position;
            private TextView tv_content_timedata;
            private ImageView item_img;
        }
    }

    //  服务内容 adapter
    private class ServiceLongListAdapter extends BaseAdapter {
        private Boolean mBoolean = true;

        public ServiceLongListAdapter(Boolean mBoolean) {
            this.mBoolean = mBoolean;
        }

        @Override
        public int getCount() {
            //   根据传入的Boolean值
            if (mBoolean)
                return mServiceLongList.size();     //  为TRUE时，表示展开布局，显示全部数据
            else
                return list1.size();
        }

        @Override
        public Object getItem(int position) {
            if (mBoolean)
                return mServiceLongList.get(position);
            else
                return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.conunselor_service_item, parent, false);
                viewHolder.tv_content_serverName = (TextView) convertView.findViewById(R.id.tv_content_serverName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (mBoolean)
                viewHolder.tv_content_serverName.setText((position + 1) + ". " + mServiceLongList.get(position).getDetailed());
            else
                viewHolder.tv_content_serverName.setText((position + 1) + ". " + list1.get(position));
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_content_serverName;
        }
    }

    //  服务方式 adapter
    private class CounselorServerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mCounselorServiceLis.size();
        }

        @Override
        public Object getItem(int position) {
            return mCounselorServiceLis.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.conunselor_counselor_server_item, parent, false);
                viewHolder.tv_c_s_name = (TextView) convertView.findViewById(R.id.tv_c_s_name);
                viewHolder.tv_c_s_rmb = (TextView) convertView.findViewById(R.id.tv_c_s_rmb);
                viewHolder.but_c_s_hire = (Button) convertView.findViewById(R.id.but_c_s_hire);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_c_s_name.setText(mCounselorServiceLis.get(position).getServiceName());
//            if (mCounselorServiceLis.get(position).getServicePrice() > 1000)       //  由price价格 确定是否需要加，号
//                viewHolder.tv_c_s_rmb.setText("总费用 RMB " + mCounselorServiceLis.get(position).getServicePrice() / 1000 + ",000.0 元");
//            else
                viewHolder.tv_c_s_rmb.setText("总费用 RMB " + mCounselorServiceLis.get(position).getServicePrice() + " 元");

            viewHolder.but_c_s_hire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {       //   立即聘用的点击事件，可在此处执行操作

                    Intent intent = new Intent(CounselorActivity.this, ServiceActivity.class);
                    if (!Util.isEmpty(mCounselorDetail.getExtraService().trim())) {
                        intent.putExtra("extraService", String.valueOf(mCounselorDetail.getExtraService()));
                    }
                    intent.putExtra("serviceId", String.valueOf(mCounselorServiceLis.get(position).getServiceId()));
                    intent.putExtra("counselorId", String.valueOf(counselorId));
                    intent.putExtra("servicePrice", String.valueOf((int) (mCounselorServiceLis.get(position).getServicePrice())));
                    startActivity(intent);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_c_s_name;
            private TextView tv_c_s_rmb;
            private Button but_c_s_hire;
        }
    }

    //  毕业院校 adapter
    private class SchoolServerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mEduList.size();
        }

        @Override
        public Object getItem(int position) {
            return mEduList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.conunselor_workyers_item, parent, false);
                viewHolder.tv_content_position = (TextView) convertView.findViewById(R.id.tv_content_position);
                viewHolder.tv_content_timedata = (TextView) convertView.findViewById(R.id.tv_content_timedata);
                viewHolder.tv_content_companyName = (TextView) convertView.findViewById(R.id.tv_content_companyName);
                viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position == 0) {
                viewHolder.item_img.setVisibility(View.GONE);
            } else
                viewHolder.item_img.setVisibility(View.VISIBLE);
            viewHolder.tv_content_companyName.setText(mEduList.get(position).getEducationName());
            viewHolder.tv_content_position.setText(mEduList.get(position).getMajor());
            viewHolder.tv_content_timedata.setText(mEduList.get(position).getStartTime() + " - " + mEduList.get(position).getEndTime());
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_content_companyName;
            private TextView tv_content_position;
            private TextView tv_content_timedata;
            private ImageView item_img;
        }
    }

    //  调取系统播放器播放
    private void broadcastVideo(String videoURL) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoURL), "video/mp4");
        startActivity(intent);
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
                                    Intent it = new Intent(CounselorActivity.this, UrgooVideoActivity.class);
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

    public void onEvent(CounselorEvent event) {
        CounselorManager.getInstance(this).getCounselorInfo(this, counselorId);
    }

    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SPManager.getInstance(this).setLogins("1");
            img_news.setVisibility(View.GONE);
            if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
                Bundle extras = new Bundle();
                extras.putInt(MainActivity.EXTRA_TAB, 0);
                Util.openActivityWithBundle(CounselorActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
