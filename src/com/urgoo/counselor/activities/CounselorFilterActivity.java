package com.urgoo.counselor.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.counselor.adapter.FilterAdatper;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.SearchData;
import com.urgoo.domain.CounselorFilterInfo;
import com.urgoo.net.EventCode;
import com.urgoo.view.MyListView;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bb on 2016/10/22.
 */
public class CounselorFilterActivity extends BaseActivity {
    private CounselorFilterInfo counselorFilterInfo;
    private MyListView lvSurprise;
    private MyListView lvService;
    private MyListView lvFrom;
    private MyListView lvSex;
    private MyListView lvLocation;
    private MyListView lvYear;
    private EditText etSearch;
    private Button btnTrue;
    private Button btnRest;
    private List<Map<String, String>> surpriseList = new ArrayList<>();
    private List<Map<String, String>> serviceList = new ArrayList<>();
    private List<Map<String, String>> fromList = new ArrayList<>();
    private List<Map<String, String>> sexList = new ArrayList<>();
    private List<Map<String, String>> locationList = new ArrayList<>();
    private List<Map<String, String>> yearList = new ArrayList<>();

    private FilterAdatper surpriseAdapter;
    private FilterAdatper serviceAdapter;
    private FilterAdatper fromAdapter;
    private FilterAdatper sexAdapter;
    private FilterAdatper locationAdapter;
    private FilterAdatper yearAdapter;

    private SearchData searchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_filter);
        initViews();
        searchData = new SearchData();
        searchData.clearData();
        getCounselorFilter();
    }

    private void restView() {
        lvSurprise.setItemChecked(searchData.getSurpriseId(), false);
        lvService.setItemChecked(searchData.getServiceId(), false);
        lvFrom.setItemChecked(searchData.getCountryId(), false);
        lvSex.setItemChecked(searchData.getGenderId(), false);
        lvLocation.setItemChecked(searchData.getPositionId(), false);
        lvYear.setItemChecked(searchData.getCounselorExperanceId(), false);
        searchData.clearData();
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etSearch = (EditText) findViewById(R.id.edittext);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转activity
                    Bundle bundle = new Bundle();
                    bundle.putString("path", "name");
                    bundle.putString("name", etSearch.getText().toString().trim());
                    Util.openActivityWithBundle(CounselorFilterActivity.this, SearchResultAcivity.class, bundle);
                    searchData.clearData();
                    return true;
                }
                return false;
            }
        });
        btnRest = (Button) findViewById(R.id.btn_rest);
        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restView();
            }
        });
        btnTrue = (Button) findViewById(R.id.btn_true);
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isEmpty(etSearch.getText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", "search");
                    bundle.putParcelable("search", searchData);
                    Util.openActivityWithBundle(CounselorFilterActivity.this, SearchResultAcivity.class, bundle);
                    restView();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", "name");
                    bundle.putString("name", etSearch.getText().toString().trim());
                    Util.openActivityWithBundle(CounselorFilterActivity.this, SearchResultAcivity.class, bundle);
                    searchData.clearData();
                }
            }
        });
        lvSurprise = (MyListView) findViewById(R.id.lv_surprise);
        lvSurprise.setSelected(true);
        lvSurprise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.clearData();
                searchData.setSurpriseType(surpriseList.get(position).get("type"));
                searchData.setSurpriseId(position);
                restView();
                Bundle bundle = new Bundle();
                bundle.putString("path", "search");
                bundle.putParcelable("search", searchData);
                Util.openActivityWithBundle(CounselorFilterActivity.this, SearchResultAcivity.class, bundle);
                System.out.println(surpriseList.get(position).get("type"));
            }
        });
        lvService = (MyListView) findViewById(R.id.lv_service);
        lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.setServiceType(serviceList.get(position).get("type"));
                searchData.setServiceId(position);
                System.out.println(serviceList.get(position).get("type"));
            }
        });
        lvFrom = (MyListView) findViewById(R.id.lv_from);
        lvFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.setCountryType(fromList.get(position).get("type"));
                searchData.setCountryId(position);
                System.out.println(fromList.get(position).get("type"));
            }
        });
        lvSex = (MyListView) findViewById(R.id.lv_sex);
        lvSex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.setGender(sexList.get(position).get("type"));
                searchData.setGenderId(position);
                System.out.println(sexList.get(position).get("type"));
            }
        });
        lvLocation = (MyListView) findViewById(R.id.lv_location);
        lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.setPositionType(locationList.get(position).get("type"));
                searchData.setPositionId(position);
                System.out.println(locationList.get(position).get("type"));
            }
        });
        lvYear = (MyListView) findViewById(R.id.lv_year);
        lvYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchData.setCounselorExperanceType(yearList.get(position).get("type"));
                searchData.setCounselorExperanceId(position);
                System.out.println(yearList.get(position).get("type"));
            }
        });
        surpriseAdapter = new FilterAdatper(this, surpriseList);
        serviceAdapter = new FilterAdatper(this, serviceList);
        fromAdapter = new FilterAdatper(this, fromList);
        sexAdapter = new FilterAdatper(this, sexList);
        yearAdapter = new FilterAdatper(this, yearList);
        locationAdapter = new FilterAdatper(this, locationList);
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
                    for (int i = 0; i < counselorFilterInfo.getSurpriseList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getSurpriseList().get(i).getSurpriseName());          //名字
                        map.put("type", counselorFilterInfo.getSurpriseList().get(i).getSurpriseType());        //类型
                        surpriseList.add(map);
                    }
                    lvSurprise.setAdapter(surpriseAdapter);
                    surpriseAdapter.notifyDataSetChanged();

                    for (int i = 0; i < counselorFilterInfo.getServiceList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getServiceList().get(i).getServiceName());          //名字
                        map.put("type", counselorFilterInfo.getServiceList().get(i).getServiceType());        //类型
                        serviceList.add(map);
                    }
                    lvService.setAdapter(serviceAdapter);
                    serviceAdapter.notifyDataSetChanged();

                    for (int i = 0; i < counselorFilterInfo.getCountryTypeList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getCountryTypeList().get(i).getCountryName());          //名字
                        map.put("type", counselorFilterInfo.getCountryTypeList().get(i).getCountryType());        //类型
                        fromList.add(map);
                    }
                    lvFrom.setAdapter(fromAdapter);
                    fromAdapter.notifyDataSetChanged();

                    for (int i = 0; i < counselorFilterInfo.getGenderList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getGenderList().get(i).getGenderName());          //名字
                        map.put("type", counselorFilterInfo.getGenderList().get(i).getGenderType());        //类型
                        sexList.add(map);
                    }
                    lvSex.setAdapter(sexAdapter);
                    sexAdapter.notifyDataSetChanged();

                    for (int i = 0; i < counselorFilterInfo.getPositonList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getPositonList().get(i).getPositionName());          //名字
                        map.put("type", counselorFilterInfo.getPositonList().get(i).getPositionType());        //类型
                        locationList.add(map);
                    }
                    lvLocation.setAdapter(locationAdapter);
                    locationAdapter.notifyDataSetChanged();

                    for (int i = 0; i < counselorFilterInfo.getCounselorExperanceList().size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", counselorFilterInfo.getCounselorExperanceList().get(i).getCounselorExperanceName());          //名字
                        map.put("type", counselorFilterInfo.getCounselorExperanceList().get(i).getCounselorExperanceType());        //类型
                        yearList.add(map);
                    }
                    lvYear.setAdapter(yearAdapter);
                    yearAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}


