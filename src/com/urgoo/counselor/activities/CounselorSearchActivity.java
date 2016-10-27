package com.urgoo.counselor.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.data.SPManager;
import com.urgoo.domain.CountryTypeList;
import com.urgoo.domain.GenderList;
import com.urgoo.domain.ServiceList;
import com.urgoo.collect.model.CounselorEntiy;
import com.urgoo.net.EventCode;
import com.urgoo.view.FlowRadioGroup;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CounselorSearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText etSearch;
    private TextView tvCancle;
    private FlowRadioGroup frgSearch;
    private ListView lvHistory;
    private Button btnHistory;
    private LinearLayout llSearch;
    private UltimateRecyclerView recyclerView;
    private CounselorServerAdapter mAdapter;
    private List<ServiceList> serviceList;
    private List<CountryTypeList> countryTypeList;
    private List<GenderList> genderList;
    private CounselorListAdapter counselorListAdapter;
    private ArrayList<CounselorEntiy> tcEntiys = new ArrayList<>();
    private TextView tvNoSearch;
    private String serviceType = "";
    private String gender = "";
    private String countryType = "";
    private int page = 0;
    private boolean isUpdate;

    /**
     * 区分按钮的ID
     */
    private static final int SERVICELIST = 100;
    private static final int GENDERLIST = 200;
    private static final int COUNTRYLIST = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_search);
        initviews();
    }

    private void initviews() {
        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        tvNoSearch = (TextView) findViewById(R.id.tv_nosearch);
        etSearch = (EditText) findViewById(R.id.et_search);
        frgSearch = (FlowRadioGroup) findViewById(R.id.frg_search);
        lvHistory = (ListView) findViewById(R.id.lv_history);
        btnHistory = (Button) findViewById(R.id.btn_history);
        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        btnHistory.setOnClickListener(this);
        tvCancle.setOnClickListener(this);

        mAdapter = new CounselorServerAdapter();
        lvHistory.setAdapter(mAdapter);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mAdapter.getItem(position).toString();
                page = 0;
                isUpdate = true;
                showLoadingDialog();
//                getTranslateCounselor(page, "", "", "", name);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || event != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    if (!Util.isEmpty(etSearch.getText())) {
                        SPManager.getInstance(CounselorSearchActivity.this).searchList.add(etSearch.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        page = 0;
                        isUpdate = true;
                        showLoadingDialog();
//                        getTranslateCounselor(page, countryType, gender, serviceType, etSearch.getText().toString());
                    }
                    etSearch.setText("");
                    return true;
                }
                return false;
            }
        });
        counselorListAdapter = new CounselorListAdapter(this, tcEntiys);
        counselorListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(CounselorActivity.COUNSELOR_ID, counselorListAdapter.getItem(position).getCounselorId());
                Util.openActivityWithBundle(CounselorSearchActivity.this, CounselorActivity.class, extras);
            }
        });
        recyclerView.setAdapter(counselorListAdapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(false);
        recyclerView.enableLoadmore();
//        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 0;
//                showLoadingDialog();
//                getTranslateCounselor(page, countryType, gender, serviceType, etSearch.getText().toString());
//            }
//        });
        counselorListAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.bottom_progressbar, null));
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                isUpdate = false;
//                getTranslateCounselor(page, countryType, gender, serviceType, etSearch.getText().toString());
            }
        });

        getHotFilter();
    }

    private void setData() {
//        int size = serviceList.size() + genderList.size() + countryTypeList.size();
        if (serviceList != null && serviceList.size() > 0) {
            for (int i = 0; i < serviceList.size(); i++) {
                View mInflate = LayoutInflater.from(this).inflate(R.layout.search_view_item, null);
                RadioButton rbSearch = (RadioButton) mInflate.findViewById(R.id.rb_search);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 30, 30, 0);
                rbSearch.setLayoutParams(lp);
                rbSearch.setText(serviceList.get(i).getServiceName());
                rbSearch.setId(SERVICELIST + i);
                frgSearch.addView(mInflate);
            }
        }
        if (genderList != null && genderList.size() > 0) {
            for (int i = 0; i < genderList.size(); i++) {
                View mInflate = LayoutInflater.from(this).inflate(R.layout.search_view_item, null);
                RadioButton rbSearch = (RadioButton) mInflate.findViewById(R.id.rb_search);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 30, 30, 0);
                rbSearch.setLayoutParams(lp);
                rbSearch.setText(genderList.get(i).getGenderName());
                rbSearch.setId(GENDERLIST + i);
                frgSearch.addView(mInflate);
            }
        }
        if (countryTypeList != null && countryTypeList.size() > 0) {
            for (int i = 0; i < countryTypeList.size(); i++) {
                View mInflate = LayoutInflater.from(this).inflate(R.layout.search_view_item, null);
                RadioButton rbSearch = (RadioButton) mInflate.findViewById(R.id.rb_search);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 30, 30, 0);
                rbSearch.setLayoutParams(lp);
                rbSearch.setText(countryTypeList.get(i).getCountryName());
                rbSearch.setId(COUNTRYLIST + i);
                frgSearch.addView(mInflate);
            }
        }

        frgSearch.setOnCheckedChangeListener(new FlowRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                if (radioButton.getId() < GENDERLIST) {
                    serviceType = serviceList.get(checkedId - SERVICELIST).getServiceType();
                } else if (radioButton.getId() >= GENDERLIST && radioButton.getId() < COUNTRYLIST) {
                    gender = genderList.get(checkedId - GENDERLIST).getGenderType();
                } else if (radioButton.getId() >= COUNTRYLIST) {
                    countryType = countryTypeList.get(checkedId - COUNTRYLIST).getCountryType();
                }
                page = 0;
                isUpdate = true;
                showLoadingDialog();
//                getTranslateCounselor(page, countryType, gender, serviceType, etSearch.getText().toString());
            }

        });
    }

    private void getHotFilter() {
        showLoadingDialog();
        CounselorManager.getInstance(this).getHotFilter(this);
    }

//    private void getTranslateCounselor(int page, String countryType, String gender, String serviceType, String name) {
//        CounselorManager.getInstance(this).getCounselorList(this, page, countryType, gender, serviceType, "", "", "", "", name);
//    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetCounselorList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    tcEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<CounselorEntiy>>() {
                    }.getType());
                    btnHistory.setVisibility(View.GONE);
                    llSearch.setVisibility(View.GONE);
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing() || isUpdate) {
                        if (tcEntiys.size() > 0) {
                            tvNoSearch.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            counselorListAdapter.clear();
                            counselorListAdapter.addData(tcEntiys);
                        } else {
                            tvNoSearch.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetHotFilter:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    if (jsonObject.has("serviceList")) {
                        serviceList = gson.fromJson(jsonObject.getJSONArray("serviceList").toString(), new TypeToken<List<ServiceList>>() {
                        }.getType());
                    }
                    if (jsonObject.has("genderList")) {
                        genderList = gson.fromJson(jsonObject.getJSONArray("genderList").toString(), new TypeToken<List<GenderList>>() {
                        }.getType());
                    }
                    if (jsonObject.has("countryTypeList")) {
                        countryTypeList = gson.fromJson(jsonObject.getJSONArray("countryTypeList").toString(), new TypeToken<List<CountryTypeList>>() {
                        }.getType());
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.btn_history:
                SPManager.getInstance(CounselorSearchActivity.this).searchList.clear();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private class CounselorServerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return SPManager.getInstance(CounselorSearchActivity.this).searchList.size();
        }

        @Override
        public Object getItem(int position) {
            return SPManager.getInstance(CounselorSearchActivity.this).searchList.get(position);
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
                convertView = LayoutInflater.from(CounselorSearchActivity.this).inflate(R.layout.search_list_item, null);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvName.setText(SPManager.getInstance(CounselorSearchActivity.this).searchList.get(position).toString());
            return convertView;
        }

        private class ViewHolder {
            private TextView tvName;
        }
    }
}
