package com.urgoo.counselor.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.adapter.CounselorListAdapter;
import com.urgoo.adapter.NationalityListDropDownAdapter;
import com.urgoo.adapter.ServiceListDropDownAdapter;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.domain.CounselorFilterInfo;
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.view.FlowRadioGroup;
import com.yyydjk.library.DropDownMenu;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bb on 2016/7/16.
 */
public class CounselorList extends BaseActivity implements StringRequestCallBack, View.OnClickListener {
    private String headers[] = {"所有服务", "顾问国籍", "高级筛选"};
    private String seniors[] = {"服务类型", "指导方式", "中文水平", "顾问经验", "顾问位置", "协会认证", "顾问性别"};
    public static final String SERVICE_TYPE = "serviceType";

    private List<View> popupViews = new ArrayList<>();
    private DropDownMenu mDropDownMenu;
    private ServiceListDropDownAdapter serviceAdapter;
    private NationalityListDropDownAdapter nationalityAdapter;
    private UltimateRecyclerView recyclerView;
    private CounselorListAdapter counselorListAdapter;
    private CounselorFilterInfo counselorFilterInfo;
    private ArrayList<CounselorEntiy> tcEntiys = new ArrayList<>();

    private ListView serviceView;
    private ListView nationalityView;
    private TextView tvNoSearch;
    private TextView tvSumbit;
    private TextView tvReset;
    private View constellationView;
    private LinearLayout llSenior;
    private String countryType = "";
    private int countryId = 0;
    private String gender = "";
    private int genderId = 0;
    private String serviceType = "";
    private int serviceId = 0;
    private String serviceMode = "";
    private int serviceModeId = 0;
    private String chineseLevelType = "";
    private int chineseLevelId = 0;
    private String counselorExperanceType = "";
    private int counselorExperanceId = 0;
    private String organizationType = "";
    private int organizationId = 0;
    private int page = 0;
    private boolean isUpdate;
    private boolean isAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselorlist);
        serviceType = getIntent().getStringExtra(SERVICE_TYPE);
        initViews();
    }

    /**
     * 填充数据
     */
    private void setData() {
        serviceAdapter = new ServiceListDropDownAdapter(this, counselorFilterInfo.getServiceList(), serviceId);
        serviceView.setAdapter(serviceAdapter);
        nationalityAdapter = new NationalityListDropDownAdapter(this, counselorFilterInfo.getCountryTypeList(), countryId);
        nationalityView.setAdapter(nationalityAdapter);

        LinearLayout serviceview = (LinearLayout) mDropDownMenu.getChildAt(0);
        if (serviceId == 0) {
            ((TextView) serviceview.getChildAt(0)).setText("所有服务");
        } else {
            ((TextView) serviceview.getChildAt(0)).setText(counselorFilterInfo.getServiceList().get(serviceId).getServiceName());
        }

        LinearLayout countryview = (LinearLayout) mDropDownMenu.getChildAt(0);
        if (countryId == 0) {
            ((TextView) countryview.getChildAt(2)).setText("顾问国籍");
        } else {
            ((TextView) countryview.getChildAt(2)).setText(counselorFilterInfo.getCountryTypeList().get(countryId).getCountryName());
        }


        llSenior.removeAllViews();
        for (int i = 0; i < seniors.length; i++) {
            TextView tvTitle = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(50, 30, 0, 0);
            tvTitle.setText(seniors[i]);
            tvTitle.setTextColor(getResources().getColor(R.color.tv666666));
            tvTitle.setTextSize(13);
            tvTitle.setLayoutParams(layoutParams);

            FlowRadioGroup radioGroup = new FlowRadioGroup(this);
            radioGroup.setLayoutParams(new FlowRadioGroup.LayoutParams(FlowRadioGroup.LayoutParams.MATCH_PARENT, FlowRadioGroup.LayoutParams.WRAP_CONTENT));

            switch (i) {
                case 0:
                    for (int index = 0; index < counselorFilterInfo.getServiceList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getServiceList().get(index).getServiceName(), serviceId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl.height = (int) (Util.getDeviceHeight(this) / 4.5);
                    break;
                case 1:
                    for (int index = 0; index < counselorFilterInfo.getServiceModeList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getServiceModeList().get(index).getServiceModeName(), serviceModeId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl1 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl1.height = Util.getDeviceHeight(this) / 16;
                    break;
                case 2:
                    for (int index = 0; index < counselorFilterInfo.getChineselevelList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getChineselevelList().get(index).getChineseLevelName(), chineseLevelId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl2 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl2.height = Util.getDeviceHeight(this) / 16;
                    break;
                case 3:
                    for (int index = 0; index < counselorFilterInfo.getCounselorExperanceList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getCounselorExperanceList().get(index).getCounselorExperanceName(), counselorExperanceId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl3 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl3.height = Util.getDeviceHeight(this) / 8;
                    break;
                case 4:
                    for (int index = 0; index < counselorFilterInfo.getCountryTypeList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getCountryTypeList().get(index).getCountryName(), countryId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl4 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl4.height = Util.getDeviceHeight(this) / 16;
                    break;
                case 5:
                    for (int index = 0; index < counselorFilterInfo.getOrgList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getOrgList().get(index).getOrganizationName(), organizationId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl5 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    //杨德成 20160730 高度变大 原版：rl5.height = Util.getDeviceHeight(this) / 16;
                    rl5.height = Util.getDeviceHeight(this) / 8;
                    break;

                case 6:
                    for (int index = 0; index < counselorFilterInfo.getGenderList().size(); index++) {
                        getGroupHeight(index, counselorFilterInfo.getGenderList().get(index).getGenderName(), genderId, radioGroup);
                    }
                    RadioGroup.LayoutParams rl6 = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();
                    rl6.height = Util.getDeviceHeight(this) / 16;
                    break;
            }
            final int finalI = i;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.getChildAt(checkedId);
                    for (int i = 0; i < group.getChildCount(); i++) {
                        ((RadioButton) group.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_text_brief));
                    }
                    if (group.getCheckedRadioButtonId() == rb.getId()) {
                        rb.setTextColor(getResources().getColor(R.color.common_botton_bar_blue));
                    }
                    switch (finalI) {
                        case 0:
                            serviceId = checkedId;
                            serviceType = counselorFilterInfo.getServiceList().get(checkedId).getServiceType();
                            break;
                        case 1:
                            serviceModeId = checkedId;
                            serviceMode = counselorFilterInfo.getServiceModeList().get(checkedId).getServiceMode();
                            break;
                        case 2:
                            chineseLevelId = checkedId;
                            chineseLevelType = counselorFilterInfo.getChineselevelList().get(checkedId).getChineseLevelType();
                            break;
                        case 3:
                            counselorExperanceId = checkedId;
                            counselorExperanceType = counselorFilterInfo.getCounselorExperanceList().get(checkedId).getCounselorExperanceType();
                            break;
                        case 4:
                            countryId = checkedId;
                            countryType = counselorFilterInfo.getCountryTypeList().get(checkedId).getCountryType();
                            break;
                        case 5:
                            organizationId = checkedId;
                            organizationType = counselorFilterInfo.getOrgList().get(checkedId).getOrganizationType();
                            break;
                        case 6:
                            genderId = checkedId;
                            gender = counselorFilterInfo.getGenderList().get(checkedId).getGenderType();
                            break;
                    }
                }
            });

            View line = new View(this);
            LinearLayout.LayoutParams viewlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            viewlp.setMargins(50, 30, 50, 0);
            line.setLayoutParams(viewlp);
            line.setBackgroundColor(getResources().getColor(R.color.color_light_background));
            llSenior.addView(tvTitle);
            llSenior.addView(radioGroup);
            llSenior.addView(line);
        }

    }

    /**
     * 设置高度
     *
     * @param index
     * @param name
     * @param id
     * @param radioGroup
     */
    private void getGroupHeight(int index, String name, int id, RadioGroup radioGroup) {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.view_radiobutton, null);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        if (index == 0) {
            lp.setMargins(50, 30, 0, 0);
        } else {
            lp.setMargins(30, 30, 0, 0);
        }
        radioButton.setLayoutParams(lp);
        radioButton.setText(name);
        radioButton.setId(index);
        if (index == id) {
            radioButton.setChecked(true);
        }
        if (radioButton.isChecked()) {
            radioButton.setTextColor(getResources().getColor(R.color.common_botton_bar_blue));
        }
        radioGroup.addView(radioButton);
    }


    public void initViews() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        tvNoSearch = (TextView) findViewById(R.id.tv_nosearch);

        //init 国籍
        nationalityView = new ListView(this);
        nationalityView.setDividerHeight(0);

        //init 服务
        serviceView = new ListView(this);
        serviceView.setDividerHeight(0);

        //init sex menu
        constellationView = getLayoutInflater().inflate(R.layout.activity_senior, null);
        llSenior = (LinearLayout) constellationView.findViewById(R.id.ll_senior);
        tvSumbit = (TextView) constellationView.findViewById(R.id.tv_sumbit);
        tvReset = (TextView) constellationView.findViewById(R.id.tv_reset);
        tvSumbit.setOnClickListener(this);
        tvReset.setOnClickListener(this);

        popupViews.add(serviceView);
        popupViews.add(nationalityView);
        popupViews.add(constellationView);

        //add item click event
        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                serviceId = position;
                if (isAll) {
                    page = 0;
                }
                isAll = false;
                serviceType = counselorFilterInfo.getServiceList().get(position).getServiceType();
                serviceAdapter.setCheckItem(serviceId);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : counselorFilterInfo.getServiceList().get(position).getServiceName());
                mDropDownMenu.closeMenu();
                clearWithOut();
                getTranslateCounselor(page, countryType, gender, serviceType, serviceMode, chineseLevelType, counselorExperanceType, organizationType);
            }
        });

        nationalityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryId = position;
                if (isAll) {
                    page = 0;
                }
                isAll = false;
                countryType = counselorFilterInfo.getCountryTypeList().get(position).getCountryType();
                nationalityAdapter.setCheckItem(countryId);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : counselorFilterInfo.getCountryTypeList().get(position).getCountryName());
                mDropDownMenu.closeMenu();
                clearWithOut();
                getTranslateCounselor(page, countryType, gender, serviceType, serviceMode, chineseLevelType, counselorExperanceType, organizationType);
            }
        });

        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

        counselorListAdapter = new CounselorListAdapter(this, tcEntiys);
        counselorListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(CounselorActivity.COUNSELOR_ID, counselorListAdapter.getItem(position).getCounselorId());
                Util.openActivityWithBundle(CounselorList.this, CounselorActivity.class, extras);
            }
        });

        recyclerView = new UltimateRecyclerView(this);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setAdapter(counselorListAdapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(false);
        recyclerView.enableLoadmore();
        counselorListAdapter.setCustomLoadMoreView(getLayoutInflater().inflate(R.layout.bottom_progressbar, null));
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                isUpdate = false;
                if (isAll) {
                    getMyCounselorList(page);
                } else {
                    getTranslateCounselor(page, countryType, gender, serviceType, serviceMode, chineseLevelType, counselorExperanceType, organizationType);
                }
            }
        });

        showLoadingDialog();
        getCounselorFilter();

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerView);
    }

    private void getTranslateCounselor(int page, String countryType, String gender, String serviceType, String serviceMode,
                                       String chineseLevelType, String counselorExperanceType, String organizationType) {
        CounselorManager.getInstance(this).getCounselorList(this, page, countryType, gender, serviceType,
                serviceMode, chineseLevelType, counselorExperanceType, organizationType, "");
    }

    private void getMyCounselorList(int page) {
        CounselorManager.getInstance(this).getMyCounselorList(page, this);
    }

    private void getCounselorFilter() {
        CounselorManager.getInstance(this).getCounselorFilter(this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetCounselorFilter:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    counselorFilterInfo = gson.fromJson(jsonObject.getJSONObject("obj").toString(), new TypeToken<CounselorFilterInfo>() {
                    }.getType());
                    if (!Util.isEmpty(serviceType)) {
                        for (int i = 0; i < counselorFilterInfo.getServiceList().size(); i++) {
                            if (serviceType.equals(counselorFilterInfo.getServiceList().get(i).getServiceType())) {
                                serviceId = i;
                            }
                        }
                    } else {
                        serviceType = "";
                    }
                    isUpdate = true;
                    getMyCounselorList(page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetMyCounselorList:
                dismissLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    tcEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<CounselorEntiy>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing() || isUpdate) {
                        if (tcEntiys.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoSearch.setVisibility(View.GONE);
                            counselorListAdapter.clear();
                            counselorListAdapter.addData(tcEntiys);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvNoSearch.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (!tcEntiys.isEmpty()) {
                            counselorListAdapter.addData(tcEntiys);
                        }
                    }
                    if (tcEntiys.size() < ZWConfig.pageSize) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetCounselorList:
                dismissLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    tcEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<CounselorEntiy>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing() || isUpdate) {
                        if (tcEntiys.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoSearch.setVisibility(View.GONE);
                            counselorListAdapter.clear();
                            counselorListAdapter.addData(tcEntiys);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvNoSearch.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (!tcEntiys.isEmpty()) {
                            counselorListAdapter.addData(tcEntiys);
                        }
                    }
                    if (tcEntiys.size() < ZWConfig.pageSize) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 第一列表第二列表筛选控制
     */
    private void clearWithOut() {
        showLoadingDialog();
        isUpdate = true;
        page = 0;
        gender = "";
        genderId = 0;
        serviceMode = "";
        serviceModeId = 0;
        chineseLevelType = "";
        chineseLevelId = 0;
        counselorExperanceType = "";
        counselorExperanceId = 0;
        organizationType = "";
        organizationId = 0;
    }

    /**
     * 清楚数据
     */
    private void clear() {
        countryType = "";
        countryId = 0;
        gender = "";
        genderId = 0;
        serviceType = "";
        serviceId = 0;
        serviceMode = "";
        serviceModeId = 0;
        chineseLevelType = "";
        chineseLevelId = 0;
        counselorExperanceType = "";
        counselorExperanceId = 0;
        organizationType = "";
        organizationId = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sumbit:
                showLoadingDialog();
                isAll = false;
                page = 0;
                isUpdate = true;
                getTranslateCounselor(page, countryType, gender, serviceType, serviceMode, chineseLevelType, counselorExperanceType, organizationType);
                mDropDownMenu.closeMenu();
                break;
            case R.id.tv_reset:
                clear();
                setData();
                break;
            case R.id.back:
                //退出activity前关闭菜单
                if (mDropDownMenu.isShowing()) {
                    mDropDownMenu.closeMenu();
                } else {
                    super.onBackPressed();
                }
                finish();
                break;
            case R.id.search:
                startActivity(new Intent(this, CounselorSearchActivity.class));
                break;
        }
    }
}
