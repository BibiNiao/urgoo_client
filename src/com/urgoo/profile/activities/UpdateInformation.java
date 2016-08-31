package com.urgoo.profile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.Person;
import com.urgoo.net.EventCode;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by lijie on 2016/4/13.
 */
public class UpdateInformation extends ActivityBase implements AdapterView.OnItemClickListener {
    private ListView mListView;
    public static final String EXTRA_GENDER = "EXTRA_GENDER";
    public static final String EXTRA_GRADE = "EXTRA_GRADE";
    public static final String EXTRA_CITY = "EXTRA_CITY";
    public static final String EXTRA_TARGET_COUNTRY = "EXTRA_TARGET_COUNTRY";
    public static final String RESULT = "RESULT";
    private String STATUS = "";
    private Person person;

    private String[] gender = {"男", "女"};
    private String[] grade = {"1年级", "2年级", "3年级", "4年级", "5年级"};
    private ProgressDialog progressBar;

    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mList = new ArrayList<String>();
    private ArrayList<Person> mDatas = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_information);
        progressBar = new ProgressDialog(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInformation.this.finish();
            }
        });

        mListView = $(R.id.listView);
        mListView.setOnItemClickListener(this);
        if (getIntent().hasExtra(EXTRA_GENDER)) {
            STATUS = EXTRA_GENDER;
            person = (Person) getIntent().getSerializableExtra(EXTRA_GENDER);
            mList.addAll(getDatas(gender));
        }
        if (getIntent().hasExtra(EXTRA_GRADE)) {
            STATUS = EXTRA_GRADE;
            mList.addAll(getDatas(grade));
            person = (Person) getIntent().getSerializableExtra(EXTRA_GRADE);
        }
        if (getIntent().hasExtra(EXTRA_CITY)) {
            STATUS = EXTRA_CITY;
            person = (Person) getIntent().getSerializableExtra(EXTRA_CITY);
            getCityList();
        }
        if (getIntent().hasExtra(EXTRA_TARGET_COUNTRY)) {
            STATUS = EXTRA_TARGET_COUNTRY;
            person = (Person) getIntent().getSerializableExtra(EXTRA_TARGET_COUNTRY);
            getCountryList();
        }


        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_text_1, mList);
        mListView.setAdapter(mArrayAdapter);
        mArrayAdapter.notifyDataSetChanged();
    }


    private ArrayList<String> getDatas(String[] strings) {
        ArrayList<String> list = new ArrayList<String>();
        if (strings == null || strings.length == 0) return list;
        for (String string : strings) {
            list.add(string);
        }
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (person == null) person = new Person();
        Intent intent = new Intent();
        if (STATUS.equals(EXTRA_CITY)) {
            person.setCityName(mDatas.get(position).getCityName());
            person.setCityId(mDatas.get(position).getCityId());
            intent.putExtra(RESULT, person);
        }
        if (STATUS.equals(EXTRA_GENDER)) {
            person.setGenderName(mList.get(position));
            person.setGender(String.valueOf(position + 1));
            intent.putExtra(RESULT, person);
        }
        if (STATUS.equals(EXTRA_GRADE)) {
            person.setGrade(mList.get(position));
            intent.putExtra(RESULT, person);
        }
        if (STATUS.equals(EXTRA_TARGET_COUNTRY)) {
            Log.d("mDatas.get(position).getCountryName()", "" + mDatas.get(position).getCountryName());
            person.setCountryName(mDatas.get(position).getCountryName());
            person.setCountryId(mDatas.get(position).getCountryId());
            intent.putExtra(RESULT, person);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getCountryList() {
        progressBar.setMessage("Loading...");
        progressBar.show();
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", spManager.getToken());
        OkHttpClientManager.postAsyn(ZWConfig.Action_selectCountryList, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.d("getCountryList", response);
                if (!UpdateInformation.this.isFinishing() && progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONObject("body").getJSONArray("countryList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        Person data = new Person();
                        data.setCountryId(String.valueOf(jo.getInt("countryId")));
                        data.setCountryName(jo.getString("name"));
                        mDatas.add(data);
                    }
                    Log.d("mDatas", mDatas.toString());
                    mList.addAll(parseBeanToList(mDatas));
                    mArrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        }, maps);
    }

    private void getCityList() {
        progressBar.setMessage("Loading...");
        progressBar.show();
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", spManager.getToken());
        OkHttpClientManager.postAsyn(ZWConfig.Action_selectCityList, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.d("getCityList", response);
                if (!UpdateInformation.this.isFinishing() && progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONObject("body").getJSONArray("cityList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        Person data = new Person();
                        data.setCityId(String.valueOf(jo.getInt("id")));
                        data.setCityName(jo.getString("cityName"));
                        mDatas.add(data);
                    }
                    Log.d("mDatas", mDatas.toString());
                    mList.addAll(parseBeanToList(mDatas));
                    mArrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        }, maps);
    }

    private ArrayList<String> parseBeanToList(ArrayList<Person> lists) {
        ArrayList<String> list = new ArrayList<String>();
        if (lists == null || lists.size() == 0) return list;
        for (Person data : lists) {
            list.add(TextUtils.isEmpty(data.getCityName()) ? data.getCountryName() : data.getCityName());
        }
        return list;
    }
}
