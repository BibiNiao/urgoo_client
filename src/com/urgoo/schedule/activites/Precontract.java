package com.urgoo.schedule.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.FragmentActivityBase;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.db.MakeManager;
import com.urgoo.db.MakePerson;
import com.urgoo.domain.NowMakeBean;
import com.urgoo.message.activities.ChatActivity;
import com.urgoo.view.BaseDialog;
import com.urgoo.view.CircleImageView;
import com.urgoo.view.SyncHorizontalScrollView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * Created by lijie on 2016/6/1.
 */
public class Precontract extends FragmentActivityBase implements View.OnClickListener {
    private static final int HANDLER = 0;
    private static final int HANDLER_END = 2;
    public static String mString = null;
    public static String mString2 = null;
    public static String mString3 = null;
    public static String mTime = null;
    public static String mTime2 = null;
    public static String mTime3 = null;
    public static String mData = null;
    public static String mData2 = null;
    public static String mData3 = null;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String TAG = "make ";
    private CircleImageView img_schedule_usericon;
    private TextView tv_schedule_username,
            tv_schedule_workyear,
            tv_schedule_goodfield,
            tv_schedule_maketimes;
    private Handler mHandler;
    private ArrayList<NowMakeBean.AdvanceBean> mAdvanList = new ArrayList<>();
    private ArrayList<MakePerson> mPersons = new ArrayList<MakePerson>();
    private NowMakeBean mdata;
    private LinearLayout schedule_now_make, LinLyout_schedule_back;
    private nowMakeFragment now;
    private MakeManager mgr;
    private String counselorId;
    public static String[] tabTitle;
    private RelativeLayout rl_nav;
    private SyncHorizontalScrollView mHsv;
    private RadioGroup rg_nav_content;
    private ImageView iv_nav_left;
    private ImageView iv_nav_right;
    private ViewPager mViewPager;
    private int indicatorWidth;
    private LayoutInflater mInflater;
    private TabFragmentPagerAdapter mAdapter;
    private EditText schedul_now_et;
    private int currentIndicatorLeft = 0;
    private String str_EditText = "未填写";
    private ImageView img_cont_guwen;
    private TextView tv_times;
    private TextView tv_make_help;
    private TextView tv_schedule_countriesname;
    private MakePerson mMakePerson;
    private RadioButton RadioButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_xm);
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("counselorId") != null) {
            counselorId = mIntent.getStringExtra("counselorId");
        }else {
            Toast.makeText(Precontract.this, "网络请求失败，请检查网络后重试!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Log.d(TAG, "counselorId: " + counselorId);
        mgr = new MakeManager(Precontract.this);
        getNetDataForiconSwitch();
        initView();
        initListener();
        setListener();


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == HANDLER) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < mAdvanList.size(); i++) {
                                for (int j = 0; j < mAdvanList.get(i).getTimeList().size(); j++) {

                                    // 存取数据库时：按照当前页，和当前页的每条数据的标识ID，存取
                                    mMakePerson = new MakePerson(i, Integer.valueOf(mAdvanList.get(i).getTimeList().get(j).getAdvanceTimeId()), 1, 0);
//                                    mMakePerson = new MakePerson(i, j + 1, 1, 0);
                                    mPersons.add(mMakePerson);
                                }
                            }
                            mgr.add(mPersons);
                            // 添加一个   type  item  state  num
                            //              0    0      0     0
                            mgr.add();
                            Message msg = new Message();
                            msg.what = HANDLER_END;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                if (msg.what == HANDLER_END) {
                    initData();
                    tabTitle = new String[mAdvanList.size()];
                    for (int i = 0; i < mAdvanList.size(); i++) {
                        tabTitle[i] = mAdvanList.get(i).getDataYanlong();
                    }
                    indicatorWidth = Util.getDeviceWidth(Precontract.this) / 7;
                    mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, Precontract.this);
                    mInflater = (LayoutInflater) Precontract.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    initNavigationHSV();
                    mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mAdapter);
                    checkgbStuts();
                }
            }
        };
    }


    private void checkgbStuts() {
        rg_nav_content.getChildAt(0).performClick();
        for (int i = 0; i < mAdvanList.size(); i++) {
            if (!mAdvanList.get(i).getBusyDay().equals("green")) {
                rg_nav_content.getChildAt(i).setBackgroundResource(R.drawable.make_time_color_bg2);
                ((RadioButton) rg_nav_content.getChildAt(i)).setBackgroundResource(R.drawable.tab_bgs);
            } else {
                rg_nav_content.getChildAt(i).setBackgroundResource(R.drawable.make_time_color_bg);
                ((RadioButton) rg_nav_content.getChildAt(i)).setBackgroundResource(R.drawable.tab_bg);
            }
        }
    }

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (rg_nav_content != null && rg_nav_content.getChildCount() > position) {
                    ((RadioButton) rg_nav_content.getChildAt(position)).performClick();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        rg_nav_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.EDGE_LEFT) {
                    mHsv.fling(0);
                    mHsv.scrollTo(indicatorWidth, 0);
                }
                return true;
            }
        });

        rg_nav_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rg_nav_content.getChildAt(checkedId) != null) {
                    TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft,
                            ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    mViewPager.setCurrentItem(checkedId);
                    currentIndicatorLeft = (rg_nav_content.getChildAt(checkedId)).getLeft();
                    int ints = Integer.parseInt(((RadioButton) rg_nav_content.getChildAt(checkedId)).getText().toString());
                    String pattern = "-";
                    Pattern pat = Pattern.compile(pattern);
                    final String[] rs = pat.split(mAdvanList.get(checkedId).getAdvanceDate());
                    onDateSet((Integer.parseInt(rs[0]) - 2000),
                            Integer.parseInt(rs[1].toString()),
                            Integer.parseInt(rs[2].toString()), checkedId);

//                     TODO 实验获取周几的方法，目前可用，以后可能会有问题，改为以上使用蔡勒方法，
//                    if (ints < 10) {
//                        ints += 10;
//                    }
//                    switch (ints % 7) {
//                        case 3:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(0)).getLeft(), 0);
//                            break;
//                        case 4:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(1)).getLeft(), 0);
//                            break;
//                        case 5:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(2)).getLeft(), 0);
//                            break;
//                        case 6:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(3)).getLeft(), 0);
//                            break;
//                        case 0:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(4)).getLeft(), 0);
//                            break;
//                        case 1:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(5)).getLeft(), 0);
//                            break;
//                        case 2:
//                            mHsv.smoothScrollTo((checkedId > 1 ? (rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - (rg_nav_content.getChildAt(6)).getLeft(), 0);
//                            break;
//                    }
                }
            }
        });
    }

    // 获取到当前日期对应的周几的方法
    public void onDateSet(int y, int m, int d, int position) {
        Log.d(TAG, " y = " + y + "  ;  m = " + m + "  ; d = " + d);
        int w = (y + Math.abs(y / 4) + Math.abs(20 / 4) - 2 * 20 + Math.abs(26 * (m + 1) / 10) + (d) - 1) % 7;
        switch (w) {
            case 0:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(0)).getLeft(), 0);
                break;
            case 1:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(1)).getLeft(), 0);
                break;
            case 2:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(2)).getLeft(), 0);
                break;
            case 3:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(3)).getLeft(), 0);
                break;
            case 4:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(4)).getLeft(), 0);
                break;
            case 5:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(5)).getLeft(), 0);
                break;
            case 6:
                mHsv.smoothScrollTo((position > 1 ? (rg_nav_content.getChildAt(position)).getLeft() : 0) - (rg_nav_content.getChildAt(6)).getLeft(), 0);
                break;
            default:
                break;
        }
    }

    private void initNavigationHSV() {
        rg_nav_content.removeAllViews();
        for (int i = 0; i < tabTitle.length; i++) {
            RadioButtons = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
            RadioButtons.setId(i);
            RadioButtons.setText(tabTitle[i]);
            RadioButtons.setLayoutParams(new ViewGroup.LayoutParams(indicatorWidth,
                    indicatorWidth));
            rg_nav_content.addView(RadioButtons);

        }

    }

    //  添加Fragment
    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int mI) {
            now = new nowMakeFragment();
            mFragments.add(now);
            now.setType(mI);
            now.setId(counselorId);
            now.setBean(mAdvanList);
            return now;

        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

    }

    public void initData() {
        if (mdata != null) {
//            if (!mdata.getCnName().equals(""))getCnName
//                tv_schedule_username.setText(mdata.getCnName());
//            else
            if (!Util.isEmpty(mdata.getEnName())) {
                tv_schedule_username.setText(mdata.getEnName());
            }

            if (!Util.isEmpty(mdata.getCountryName())) {
                tv_schedule_countriesname.setText(mdata.getCountryName());
            }


            if (!Util.isEmpty(mdata.getToday()))
                tv_times.setText(mdata.getToday());

            if (!Util.isEmpty(mdata.getAdvancedTime()))
                tv_schedule_workyear.setText(mdata.getAdvancedTime() + "人预约过");
            else
                tv_schedule_workyear.setText("0人预约过");
            if (!Util.isEmpty(mdata.getClosedTime()))
                tv_schedule_goodfield.setText(mdata.getClosedTime() + "人已视频");
            else
                tv_schedule_goodfield.setText("0人已视频");
            if (!Util.isEmpty(mdata.getRatio()))
                tv_schedule_maketimes.setText("预约接受率:  " + mdata.getRatio());
            else tv_schedule_maketimes.setText("预约接受率:  0%");
            if (!Util.isEmpty(mdata.getUserIcon()))
                new imageLoadBusiness().imageLoadByURL(mdata.getUserIcon(), img_schedule_usericon);
        }
    }

    public void initView() {
        schedul_now_et = (EditText) findViewById(R.id.schedul_now_et);
        img_schedule_usericon = (CircleImageView) findViewById(R.id.img_schedule_usericon);
        tv_schedule_username = (TextView) findViewById(R.id.tv_schedule_username);
        tv_schedule_workyear = (TextView) findViewById(R.id.tv_schedule_workyear);
        tv_schedule_goodfield = (TextView) findViewById(R.id.tv_schedule_goodfield);
        tv_schedule_maketimes = (TextView) findViewById(R.id.tv_schedule_maketimes);
        tv_schedule_countriesname = (TextView) findViewById(R.id.tv_schedule_countriesname);
        schedule_now_make = (LinearLayout) findViewById(R.id.schedule_now_make);
        LinLyout_schedule_back = (LinearLayout) findViewById(R.id.LinLyout_schedule_back);
        rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
        mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
        rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
        iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
        iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
        mViewPager = (ViewPager) findViewById(R.id.schedule_viewPage);
        img_cont_guwen = (ImageView) findViewById(R.id.img_cont_guwen);
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_make_help = (TextView) findViewById(R.id.tv_make_help);

    }

    private void initListener() {
        schedule_now_make.setOnClickListener(this);
        LinLyout_schedule_back.setOnClickListener(this);
        img_cont_guwen.setOnClickListener(this);
        tv_make_help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinLyout_schedule_back:
                finish();
                break;
            case R.id.schedule_now_make:
                but_make();
                break;

            case R.id.tv_make_help:
                startActivity(new Intent(Precontract.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
                finish();
                break;
            // TODO H5
            case R.id.img_cont_guwen:
                if (mdata != null) {
                    Intent mIntent3 = new Intent(Precontract.this, BaseWebViewActivity.class);
                    mIntent3.putExtra(BaseWebViewActivity.EXTRA_URL, ZWConfig.URGOOURL_BASE + "001/001/client/videoHelp?token=" + SPManager.getInstance(this).getToken());
                    startActivity(mIntent3);
//                    finish();
                } else
                    Toast.makeText(Precontract.this, R.string.jiancha, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //预约按钮
    private void but_make() {
        if (mString == null) {
            Toast.makeText(Precontract.this, R.string.tishi, Toast.LENGTH_SHORT).show();
        } else {
            if (mData == null) {
                mData = "";
            }
            if (mData2 != null) {
                mData = mData + ",";
            } else {
                mData2 = "";
            }
            if (mData3 != null) {
                mData2 = mData2 + ",";
            } else {
                mData3 = "";
            }
            if (!Util.isEmpty(schedul_now_et.getText())) {
                str_EditText = schedul_now_et.getText().toString();
            } else
                str_EditText = "未填写";

            Map<String, String> params = new HashMap<String, String>();
            params.put("token", SPManager.getInstance(this).getToken() == null ? "" : SPManager.getInstance(this).getToken());
            params.put("counselorId", counselorId);
            params.put("advanceDateTimeJson", mData + mData2 + mData3);
            params.put("message", str_EditText);

            Log.d(TAG, "counselorId : " + counselorId + "    advanceDateTimeJson : " + mData + mData2 + mData3 + "    message : " + str_EditText);
            OkHttpClientManager.postAsyn(ZWConfig.Action_addAdvanceClient,
                    new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String respon) {
                            try {
                                JSONObject j = new JSONObject(respon);
                                String code = new JSONObject(j.get("header").toString()).getString("code");
                                String message = new JSONObject(j.get("header").toString()).getString("message");
                                if (code.equals("200")) {
                                    final BaseDialog mBaseDialog = new BaseDialog(Precontract.this);
                                    View contentView = LayoutInflater.from(Precontract.this).inflate(R.layout.makegood, null);
                                    mBaseDialog.setContentView(contentView);
                                    mBaseDialog.setCanceledOnTouchOutside(false);
                                    TextView but_makegood_submit = (TextView) contentView.findViewById(R.id.but_makegood_submit);
                                    ImageView ImageDelIcon = (ImageView) contentView.findViewById(R.id.ImageDelIcon);
                                    but_makegood_submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBaseDialog.dismiss();
                                            Intent it = new Intent(Precontract.this, BaseWebViewActivity.class);
                                            it.putExtra("EXTRA_URL", ZWConfig.ACTION_myInfo);
                                            startActivity(it);
                                            finish();
                                        }
                                    });

                                    ImageDelIcon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBaseDialog.dismiss();
                                            startActivity(new Intent(Precontract.this, PrecontractMyOrder.class));
                                            finish();
                                        }
                                    });

                                    mBaseDialog.show();
                                    isnull();
                                } else if (code.equals("404")) {
                                    UiUtil.show(Precontract.this, message);
                                }
                                if (code.equals("400")) {
                                    UiUtil.show(Precontract.this, message);
                                }
                            } catch (JSONException e) {
                                UiUtil.show(Precontract.this, "服务器繁忙，请稍后再试！");
                            }
                        }
                    }, params);
            isnull();
        }
    }

    //清空数据操作
    private void isnull() {
        mString = null;
        mString2 = null;
        mString3 = null;
        mTime = null;
        mTime2 = null;
        mTime3 = null;
        mData = null;
        mData2 = null;
        mData3 = null;
    }

    private void getNetDataForiconSwitch() {
        Map<String, String> params = new HashMap<String, String>();
        Log.d(TAG, "counselorId: " + counselorId);
        params.put("token", SPManager.getInstance(this).getToken());
        params.put("counselorId", counselorId);
        OkHttpClientManager.postAsyn(ZWConfig.URL_advanceInfoClient3,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {

                        try {
                            JSONObject j = new JSONObject(respon);
                            Log.d(TAG, "onResponse:11111 " + j.toString());
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message = new JSONObject(j.get("header").toString()).getString("message");

                            if (code.equals("200")) {
                                android.util.Log.d(TAG, "true");
                                JSONObject jsonObject = new JSONObject(respon).getJSONObject("body");
                                android.util.Log.d(TAG, "onResponse: " + jsonObject);
                                JSONObject jsontime = jsonObject.getJSONObject("advanceDateList");
                                android.util.Log.d(TAG, " jsontime : " + jsontime);

                                JSONArray mJSONArray = jsontime.getJSONArray("1");
                                Log.d(TAG, "mJSONArray: " + mJSONArray);

                                ArrayList<NowMakeBean.AdvanceBean> list = (new Gson()).fromJson(jsontime.getJSONArray("1").toString(),
                                        new TypeToken<ArrayList<NowMakeBean.AdvanceBean>>() {
                                        }.getType());
                                ArrayList<NowMakeBean.AdvanceBean> list2 = (new Gson()).fromJson(jsontime.getJSONArray("2").toString(),
                                        new TypeToken<ArrayList<NowMakeBean.AdvanceBean>>() {
                                        }.getType());
                                ArrayList<NowMakeBean.AdvanceBean> list3 = (new Gson()).fromJson(jsontime.getJSONArray("3").toString(),
                                        new TypeToken<ArrayList<NowMakeBean.AdvanceBean>>() {
                                        }.getType());
                                ArrayList<NowMakeBean.AdvanceBean> list4 = (new Gson()).fromJson(jsontime.getJSONArray("4").toString(),
                                        new TypeToken<ArrayList<NowMakeBean.AdvanceBean>>() {
                                        }.getType());
                                mAdvanList.addAll(list);
                                mAdvanList.addAll(list2);
                                mAdvanList.addAll(list3);
                                mAdvanList.addAll(list4);

                                JSONObject jsondeta = jsonObject.getJSONObject("advanceCounselorInfo");
                                String lastTime = jsondeta.optString("lastTime", null);
                                String goodField = jsondeta.optString("goodField", null);
                                String isPotentialClient = jsondeta.optString("isPotentialClient", null);
                                String cnName = jsondeta.optString("cnName", "");
                                String enName = jsondeta.optString("enName", null);
                                String count = jsondeta.optString("count", null);
                                String countryName = jsondeta.optString("countryName", null);
                                String historyCount = jsondeta.optString("historyCount", null);
                                String advancedTime = jsondeta.optString("advancedTime", null);
                                String ratio = jsondeta.optString("ratio", null);
                                String closedTime = jsondeta.optString("closedTime", null);
                                String userIcon = jsondeta.optString("userIcon", null);
                                String today = jsondeta.optString("today", null);
                                mdata = new NowMakeBean(today, lastTime, goodField, isPotentialClient, cnName, advancedTime, enName, count, countryName, historyCount, closedTime, userIcon, ratio);
                                Log.d(TAG, "mData: " + mdata.toString());
                                Message msg = new Message();
                                msg.what = HANDLER;
                                mHandler.sendMessage(msg);
                            } else if (code.equals("404")) {
                                UiUtil.show(Precontract.this, message);
                            }
                            if (code.equals("400")) {
                                UiUtil.show(Precontract.this, message);
                            }
                        } catch (JSONException e) {
                            UiUtil.show(Precontract.this, "Data parsing errors, please sign in again");
                        }
                    }
                }, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mgr != null)
            mgr.close();
    }

//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mgr != null)
//            mgr.close();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (mgr != null)
//            mgr.close();
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (mgr == null)
//            mgr = new MakeManager(Precontract.this);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mgr == null) {
            mgr = new MakeManager(Precontract.this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mgr == null) {
            mgr.closeDB();
        }
    }

}

