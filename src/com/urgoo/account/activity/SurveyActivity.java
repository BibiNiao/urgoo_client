package com.urgoo.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.account.model.SurveyContent;
import com.urgoo.account.model.SurveyInfo;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/8/15.
 */
public class SurveyActivity extends BaseActivity implements View.OnClickListener {
    private List<SurveyInfo> surveyInfos;
    private List<SurveyContent> questions;
    private int page = 0;
    private LinearLayout llQuestion;
    private Button btnSubmit;
    private JSONArray questionArray = new JSONArray();
    private String json;
    private int chooseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        getQuestionList();
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeRegistContent:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case EventCodeQuestionList:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    surveyInfos = gson.fromJson(jsonObject.getString("questionList"), new TypeToken<List<SurveyInfo>>() {
                    }.getType());
                    initView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void initView() {
        questions = new ArrayList<>();
        findViewById(R.id.back).setOnClickListener(this);
        llQuestion = (LinearLayout) findViewById(R.id.ll_question);
        btnSubmit = (Button) findViewById(R.id.iv_submit);
        btnSubmit.setOnClickListener(this);
        if (page == surveyInfos.size() - 1) {
            btnSubmit.setText("完成");
        }
        if (surveyInfos.get(page).getSelectType().equals("0")) {
            btnSubmit.setVisibility(View.GONE);
        }
        if (surveyInfos.get(page).getSelectType().equals("0")) {
            btnSubmit.setVisibility(View.GONE);
        }
        try {
            if (!Util.isEmpty(String.valueOf(questionArray.get(page))) && !surveyInfos.get(page).getSelectType().equals("0")) {
                btnSubmit.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.tv_title)).setText(surveyInfos.get(page).getTitle());

        for (int i = 0; i < surveyInfos.get(page).getContent().size(); i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 0);
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText(surveyInfos.get(page).getContent().get(i).getName());
            checkBox.setTextSize(14);
            checkBox.setBackgroundResource(R.drawable.selector_cb_bg);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setTextColor(getResources().getColor(R.color.tv616161));
            checkBox.setPadding(0, 40, 0, 40);
            checkBox.setButtonDrawable(null);
            try {
                if (questionArray.length() > 0) {
                    JSONObject jsonObject = new JSONObject(String.valueOf(questionArray.get(page)));
                    JSONArray anId = (JSONArray) jsonObject.get(surveyInfos.get(page).getType());
                    SurveyContent surveyContent = new SurveyContent();
                    for (int j = 0; j < anId.length(); j++) {
                        JSONObject jsonobject = (JSONObject) anId.get(j);
                        if (jsonobject.get("anId").equals(surveyInfos.get(page).getContent().get(i).getAnId())) {
                            if (surveyInfos.get(page).getSelectType().equals("1")) {
                                surveyContent.setAnId(surveyInfos.get(page).getContent().get(i).getAnId());
                                questions.add(surveyContent);
                            }
                            checkBox.setChecked(true);
                            checkBox.setTextColor(getResources().getColor(R.color.ffffff));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (surveyInfos.get(page).getSelectType().equals("0")) {
                        btnSubmit.setVisibility(View.GONE);
                    } else {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                    SurveyContent surveyContent = new SurveyContent();
                    chooseId = finalI;
                    if (isChecked) {
                        checkBox.setTextColor(getResources().getColor(R.color.ffffff));
                        surveyContent.setAnId(surveyInfos.get(page).getContent().get(chooseId).getAnId());
                        questions.add(surveyContent);
                        json = changeArrayDateToJson(questions, surveyInfos.get(page).getType());
                        try {
                            questionArray.put(page, json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (surveyInfos.get(page).getSelectType().equals("0")) {
                            toRegist();
                        }
                    } else {
                        checkBox.setTextColor(getResources().getColor(R.color.tv616161));
                        if (questions.size() > 0) {
                            for (int i = 0; i < questions.size(); i++) {
                                if (questions.get(i).getAnId().equals(surveyInfos.get(page).getContent().get(chooseId).getAnId())) {
                                    questions.remove(i);
                                }
                            }
                            json = changeArrayDateToJson(questions, surveyInfos.get(page).getType());
                        } else {
                            questions.clear();
                            json = "";
                        }
                    }
                }
            });
            llQuestion.addView(checkBox, lp);
        }
    }

    /**
     * 将数组转换为JSON格式的数据。
     *
     * @return JSON格式的数据
     */
    public static String changeArrayDateToJson(List<SurveyContent> contents, String listName) {
        try {
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            int length = contents.size();
            for (int i = 0; i < length; i++) {
                SurveyContent content = contents.get(i);
                String anId = content.getAnId();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("anId", anId);
                array.put(jsonObject);
            }
            object.put(listName, array);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断是否最后一页
     */
    private void toRegist() {
        if (page == surveyInfos.size() - 1) {
            setRegistContent(spManager.getUserId(), questionArray.toString());

//            Bundle bundle = new Bundle();
//            bundle.putString("question", questionArray.toString());
//            Util.openActivityWithBundle(SurveyActivity.this, RegistActivity.class, bundle);
//            finish();
        } else {
            page++;
            llQuestion.removeAllViews();
            initView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (page == 0) {
                finish();
            } else {
                page--;
                llQuestion.removeAllViews();
                initView();
            }
        }
        return false;
    }

    private void getQuestionList() {
        showLoadingDialog();
        AccountManager.getInstance(this).selectQuestionListAll(this);
    }

    private void setRegistContent(String userId, String questionJson) {
        AccountManager.getInstance(this).setRegistContent(userId, questionJson, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_submit:
                if (questions.size() <= 0) {
                    showToastSafe("必须选择一项");
                } else {
                    toRegist();
                }
                break;
            case R.id.back:
                if (page == 0) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                    page--;
                    llQuestion.removeAllViews();
                    initView();
                }
                break;
        }
    }
}
