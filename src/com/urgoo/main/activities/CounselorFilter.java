package com.urgoo.main.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.Filter;
import com.zw.express.tool.KeywordContainer;
import com.zw.express.tool.LinearLineWrapLayout;
import com.zw.express.tool.Util;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by lijie on 2016/5/27.
 */
public class CounselorFilter extends ActivityBase implements View.OnClickListener {
    private Filter f;
    private KeywordContainer nationality_container, gender_container, coach_container, specialty_container;
    private ArrayList<View> views = new ArrayList<View>();
    private SearchFilter s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counselor_filter);
        nationality_container = $(R.id.nationality_container);
        gender_container = $(R.id.gender_container);
        coach_container = $(R.id.coach_container);
        specialty_container = $(R.id.specialty_container);
        getFilter();
        initViews();
    }

    private void initViews() {
        $(R.id.commit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s != null)
                    EventBus.getDefault().post(s);
                CounselorFilter.this.finish();
            }
        });
        $(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CounselorFilter.this.finish();
            }
        });
    }


    private void getFilter() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(this).getToken() == null ? "" : SPManager.getInstance(this).getToken());
        OkHttpClientManager.postAsyn(ZWConfig.Action_searchInfo, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                    f = (new Gson()).fromJson(jsonObject.toString(), Filter.class);
                    if (f == null) return;
                    if (f.getServiceModeList() != null) {
                        for (int i = 0; i < f.getServiceModeList().size(); i++) {
                            TextView textView = createBtn();
                            textView.setText(f.getServiceModeList().get(i).getServiceModeName());
                            views.add(textView);
                            coach_container.addView(textView);
                        }
                    }

                    if (f.getGenderList() != null) {
                        for (int i = 0; i < f.getGenderList().size(); i++) {
                            TextView textView = createBtn();
                            textView.setText(f.getGenderList().get(i).getGenderName());
                            views.add(textView);
                            gender_container.addView(textView);
                        }
                    }
                    if (f.getCountryTypeList() != null) {
                        for (int i = 0; i < f.getCountryTypeList().size(); i++) {
                            TextView textView = createBtn();
                            textView.setText(f.getCountryTypeList().get(i).getCountryName());
                            views.add(textView);
                            nationality_container.addView(textView);
                        }
                    }
                    if (f.getLableList() != null) {
                        for (int i = 0; i < f.getLableList().size(); i++) {
                            TextView textView = createBtn();
                            textView.setText(f.getLableList().get(i).getLableCnName());
                            views.add(textView);
                            specialty_container.addView(textView);
                        }
                    }
                    coach_container.measure(0, 0);
                    specialty_container.measure(0, 0);
                    nationality_container.measure(0, 0);
                    gender_container.measure(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);

    }

    private TextView createBtn() {
        TextView textView = new TextView(CounselorFilter.this);
        LinearLineWrapLayout.LayoutParams layoutParams =
                new LinearLineWrapLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 30, 30);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(
                Util.dp2px(this, 20),
                Util.dp2px(this, 6),
                Util.dp2px(this, 20),
                Util.dp2px(this, 6)
        );
        textView.setTextColor(getResources().getColor(R.color.gray_text_color));
        textView.setBackgroundResource(R.drawable.checked_btn1);
        textView.setOnClickListener(CounselorFilter.this);
        return textView;
    }

    private void clickBtn(View view) {

        for (int i = 0; i < views.size(); i++) {
            if (view instanceof TextView) {
                ((TextView) views.get(i)).setTextColor(getResources().getColor(R.color.gray_text_color));
                ((TextView) views.get(i)).setBackgroundResource(R.drawable.checked_btn1);
            }
        }
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            ((TextView) view).setBackgroundResource(R.drawable.btn_selector);
        }
    }


    @Override
    public void onClick(View view) {
        s = new SearchFilter();
      //  Toast.makeText(this, "((TextView) view).getText():" + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        if (f.getServiceModeList() != null) {
            for (int i = 0; i < f.getServiceModeList().size(); i++) {
                if (((TextView) view).getText().toString().trim().equals(f.getServiceModeList().get(i).getServiceModeName())) {
                    s.setServiceMode(f.getServiceModeList().get(i).getServiceModeType());
                }
            }
        }

        if (f.getGenderList() != null) {
            for (int i = 0; i < f.getGenderList().size(); i++) {
                if (((TextView) view).getText().toString().trim().equals(f.getGenderList().get(i).getGenderName())) {
                    s.setIntgender(f.getGenderList().get(i).getGenderType());
                }
            }
        }
        if (f.getCountryTypeList() != null) {
            for (int i = 0; i < f.getCountryTypeList().size(); i++) {
                if (((TextView) view).getText().toString().trim().equals(f.getCountryTypeList().get(i).getCountryName())) {
                    s.setCountryType(f.getCountryTypeList().get(i).getCountryType());
                }
            }
        }
        if (f.getLableList() != null) {
            for (int i = 0; i < f.getLableList().size(); i++) {
                if (((TextView) view).getText().toString().trim().equals(f.getLableList().get(i).getLableCnName())) {
                    s.setLableId(f.getLableList().get(i).getLableId());
                }
            }
        }


        clickBtn(view);
    }
}
