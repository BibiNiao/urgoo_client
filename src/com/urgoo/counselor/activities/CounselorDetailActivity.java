package com.urgoo.counselor.activities;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.collect.event.FollowEvent;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.adapter.CounselorExperienceAdapter;
import com.urgoo.counselor.adapter.CounselorSchoolAdapter;
import com.urgoo.counselor.adapter.CounselorServerAdapter;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.CounselorDetail;
import com.urgoo.counselor.model.CounselorServiceList;
import com.urgoo.counselor.model.EduList;
import com.urgoo.counselor.model.experienceList;
import com.urgoo.domain.ShareDetail;
import com.urgoo.live.activities.LiveDetailActivity;
import com.urgoo.net.EventCode;
import com.urgoo.order.OrderActivity;
import com.urgoo.view.MyListView;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/26.
 */
public class CounselorDetailActivity extends NavToolBarActivity implements View.OnClickListener {
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";
    public static final String EXTRA_TITLE = "title";
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int IntroductionState = SHRINK_UP_STATE;//默认收起状态
    private static int SuccessState = SHRINK_UP_STATE;//默认收起状态
    private static int RequiresState = SHRINK_UP_STATE;//默认收起状态
    private static final int CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行

    private MyListView lvService;
    private MyListView lvGraduate;
    private MyListView lvWork;
    private Button btnVideo;
    private CardView cvWorks;
    private CardView cvIntroduction;
    private CardView cvSuccess;
    private CardView cvRequires;
    private LinearLayout llOrganization;
    private ImageView ivInopen;
    private ImageView ivInclose;
    private ImageView ivSuopen;
    private ImageView ivSuclose;
    private ImageView ivReopen;
    private ImageView ivReclose;
    private TextView tvGraduate;
    private TextView tvNationality;
    private TextView tvCheck;
    private TextView tvAdvance;
    private TextView tvCollect;
    private TextView tvEducation;
    private TextView tvExperience;
    private TextView tvGuidance;
    private TextView tvApprove;
    private TextView tvIntroduction;
    private TextView tvSuccess;
    private TextView tvRequires;

    private String counselorId;
    private CounselorDetail mCounselorDetail;
    private ShareDetail shareDetail;
    private ArrayList<CounselorServiceList> mCounselorServiceLis = new ArrayList<>();
    private ArrayList<EduList> mEduList = new ArrayList<>();
    private ArrayList<experienceList> mExperienceList = new ArrayList<>();
    private CounselorServerAdapter serverAdapter;
    private CounselorSchoolAdapter schoolAdapter;
    private CounselorExperienceAdapter experienceAdapter;

    @Override
    protected View createContentView() {
        View mRootView = inflaterViewWithLayoutID(R.layout.activity_counselor, null);
        counselorId = getIntent().getStringExtra(EXTRA_COUNSELOR_ID);
        initViews(mRootView);
        getCounselorDetail();
        getCounselorServer();
        return mRootView;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, ZWConfig.URGOOURL_BASE + shareDetail.url);
                break;
            case R.id.collect:
                onFavoriteArticle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getOptionMenuRes() {
        return R.menu.client_menu;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem collectMenu = menu.findItem(R.id.collect);
        if (mCounselorDetail != null) {
            if (mCounselorDetail.getIsAttention().equals("1")) {
                if (collectMenu != null) collectMenu.setIcon(R.drawable.ic_iscollected);
            } else {
                if (collectMenu != null) collectMenu.setIcon(R.drawable.ic_collect);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void initViews(View view) {
        tvAdvance = (TextView) view.findViewById(R.id.tv_advance);
        tvCheck = (TextView) view.findViewById(R.id.tv_check);
        tvCollect = (TextView) view.findViewById(R.id.tv_collect);
        tvGraduate = (TextView) view.findViewById(R.id.tv_graduate);
        tvEducation = (TextView) view.findViewById(R.id.tv_education);
        tvNationality = (TextView) view.findViewById(R.id.tv_nationality);
        tvExperience = (TextView) view.findViewById(R.id.tv_experience);
        tvGuidance = (TextView) view.findViewById(R.id.tv_guidance);
        tvApprove = (TextView) view.findViewById(R.id.tv_approve);
        tvIntroduction = (TextView) view.findViewById(R.id.tv_introduction);
        tvSuccess = (TextView) view.findViewById(R.id.tv_success);
        tvRequires = (TextView) view.findViewById(R.id.tv_requires);

        lvService = (MyListView) view.findViewById(R.id.lv_service);
        lvGraduate = (MyListView) view.findViewById(R.id.lv_graduate);
        lvWork = (MyListView) view.findViewById(R.id.lv_work);
        ivInopen = (ImageView) view.findViewById(R.id.iv_inopen);
        ivInclose = (ImageView) view.findViewById(R.id.iv_inclose);
        ivSuopen = (ImageView) view.findViewById(R.id.iv_suopen);
        ivSuclose = (ImageView) view.findViewById(R.id.iv_suclose);
        ivReopen = (ImageView) view.findViewById(R.id.iv_reopen);
        ivReclose = (ImageView) view.findViewById(R.id.iv_reclose);
        btnVideo = (Button) view.findViewById(R.id.btn_video);
        btnVideo.setOnClickListener(this);
        cvWorks = (CardView) view.findViewById(R.id.cv_works);
        cvRequires = (CardView) view.findViewById(R.id.cv_requires);
        cvRequires.setOnClickListener(this);
        cvIntroduction = (CardView) view.findViewById(R.id.cv_introduction);
        cvIntroduction.setOnClickListener(this);
        cvSuccess = (CardView) view.findViewById(R.id.cv_success);
        cvSuccess.setOnClickListener(this);
        llOrganization = (LinearLayout) view.findViewById(R.id.ll_organization);
        setNavTitleText(getIntent().getStringExtra(EXTRA_TITLE));
        serverAdapter = new CounselorServerAdapter(this, mCounselorServiceLis);
        schoolAdapter = new CounselorSchoolAdapter(this, mEduList);
        experienceAdapter = new CounselorExperienceAdapter(this, mExperienceList);
        lvService.setAdapter(serverAdapter);
        lvGraduate.setAdapter(schoolAdapter);
        lvWork.setAdapter(experienceAdapter);
        serverAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                if (!Util.isEmpty(mCounselorDetail.getExtraService().trim())) {
                    bundle.putString("extraService", String.valueOf(mCounselorDetail.getExtraService()));
                }
                bundle.putString("serviceId", String.valueOf(mCounselorServiceLis.get(position).getServiceId()));
                bundle.putString("counselorId", String.valueOf(mCounselorDetail.getCounselorId()));
                bundle.putString("servicePrice", String.valueOf(mCounselorServiceLis.get(position).getServicePrice()));
                Util.openActivityWithBundle(CounselorDetailActivity.this, OrderActivity.class, bundle);
            }
        });
    }

    private void onFavoriteArticle() {
        if (mCounselorDetail != null) {
            showLoadingDialog();
            if (mCounselorDetail.getIsAttention().equals("1")) {
                CounselorManager.getInstance(this).getCancleFollow(this, counselorId, "1");
            } else {
                CounselorManager.getInstance(this).getAddFollow(this, counselorId, "1");
            }
        }
    }

    private void getCounselorDetail() {
        showLoadingDialog();
        CounselorManager.getInstance(this).getCounselorInfo(this, counselorId);
    }

    private void getCounselorServer() {
        CounselorManager.getInstance(this).getCounselorServer(this, counselorId);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeCancleFollow:
                showToastSafe("取消收藏");
                mCounselorDetail.setIsAttention("0");
                onPrepareOptionsMenu(mToolbar.getMenu());
                EventBus.getDefault().post(new FollowEvent(counselorId, "0"));
                break;
            case EventCodeAddFollow:
                showToastSafe("收藏成功");
                mCounselorDetail.setIsAttention("1");
                onPrepareOptionsMenu(mToolbar.getMenu());
                EventBus.getDefault().post(new FollowEvent(counselorId, "1"));
                break;
            case EventCodeSelectCounselorServiceList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mCounselorServiceLis = gson.fromJson(jsonObject.getJSONArray("counselorServiceList").toString(), new TypeToken<ArrayList<CounselorServiceList>>() {
                    }.getType());
                    if (mCounselorServiceLis != null) {
                        serverAdapter.addData(mCounselorServiceLis);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            case EventCodeFindCounselorDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mCounselorDetail = gson.fromJson(jsonObject.getJSONObject("counselorDetail").toString(), new TypeToken<CounselorDetail>() {
                    }.getType());
                    onPrepareOptionsMenu(mToolbar.getMenu());
                    tvAdvance.setText(getString(R.string.is_advanced, mCounselorDetail.getAdvanceCount()));
                    tvCheck.setText(getString(R.string.is_checked, mCounselorDetail.getCounselorReadCount()));
                    tvCollect.setText(getString(R.string.is_collected, mCounselorDetail.getAttentionCount()));
                    tvNationality.setText(mCounselorDetail.getCountyName());
                    tvEducation.setText(mCounselorDetail.getLevelEducation());
                    tvExperience.setText(mCounselorDetail.getWorkYear());
                    if (Util.isEmpty(mCounselorDetail.getSelfBioTranz())) {
                        tvIntroduction.setText(mCounselorDetail.getSelfBio());
                    } else {
                        tvIntroduction.setText("友情翻译:  " + mCounselorDetail.getSelfBioTranz() + "\n" + "\n" + mCounselorDetail.getSelfBio());
                    }
                    if (Util.isEmpty(mCounselorDetail.getSuccessCase())) {
                        cvSuccess.setVisibility(View.GONE);
                    } else {
                        if (Util.isEmpty(mCounselorDetail.getSuccessCaseTranz())) {
                            tvSuccess.setText(mCounselorDetail.getSuccessCase());
                        } else {
                            tvSuccess.setText("友情翻译:  " + mCounselorDetail.getSuccessCaseTranz() + "\n" + "\n" + mCounselorDetail.getSuccessCase());
                        }
                    }
                    if (Util.isEmpty(mCounselorDetail.getRequires())) {
                        cvRequires.setVisibility(View.GONE);
                    } else {
                        tvRequires.setText(mCounselorDetail.getRequires());
                    }
                    if (Util.isEmpty(mCounselorDetail.getOrganization())) {
                        llOrganization.setVisibility(View.GONE);
                    } else {
                        tvApprove.setText(mCounselorDetail.getOrganization());
                    }
                    if (!Util.isEmpty(mCounselorDetail.getServiceMode())) {
                        tvGuidance.setText(mCounselorDetail.getServiceMode());
                    } else {
                        tvGuidance.setVisibility(View.GONE);
                    }
                    if (Util.isEmpty(mCounselorDetail.getLiveId())) {
                        btnVideo.setVisibility(View.GONE);
                    } else {
                        btnVideo.setVisibility(View.VISIBLE);
                    }
                    shareDetail = gson.fromJson(jsonObject.getJSONObject("shareDetail").toString(), new TypeToken<ShareDetail>() {
                    }.getType());
//                    mLabelList = gson.fromJson(jsonObject.getJSONArray("labelList").toString(), new TypeToken<ArrayList<LabelList>>() {
//                    }.getType());
                    mExperienceList = gson.fromJson(jsonObject.getJSONArray("experienceList").toString(), new TypeToken<ArrayList<experienceList>>() {
                    }.getType());
                    if (mExperienceList.size() > 0) {
                        experienceAdapter.addData(mExperienceList);
                    } else {
                        cvWorks.setVisibility(View.GONE);
                    }
//                    mServiceLongList = gson.fromJson(jsonObject.getJSONArray("serviceLongList").toString(), new TypeToken<ArrayList<ServiceLongList>>() {
//                    }.getType());
//                    mWorksList = gson.fromJson(jsonObject.getJSONArray("works").toString(), new TypeToken<ArrayList<Works>>() {
//                    }.getType());
                    mEduList = gson.fromJson(jsonObject.getJSONArray("eduList").toString(), new TypeToken<ArrayList<EduList>>() {
                    }.getType());
                    if (mEduList.size() > 0) {
                        schoolAdapter.addData(mEduList);
                    } else {
                        tvGraduate.setVisibility(View.GONE);
                        lvGraduate.setVisibility(View.GONE);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_video:
                Bundle bundle = new Bundle();
                bundle.putString(LiveDetailActivity.EXTRA_LIVE_ID, mCounselorDetail.getLiveId());
                Util.openActivityWithBundle(this, LiveDetailActivity.class, bundle);
                break;
            case R.id.cv_requires:
                if (RequiresState == SPREAD_STATE) {
                    tvRequires.setMaxLines(CONTENT_DESC_MAX_LINE);
                    tvRequires.requestLayout();
                    ivReopen.setVisibility(View.GONE);
                    ivReclose.setVisibility(View.VISIBLE);
                    RequiresState = SHRINK_UP_STATE;
                } else if (RequiresState == SHRINK_UP_STATE) {
                    tvRequires.setMaxLines(Integer.MAX_VALUE);
                    tvRequires.requestLayout();
                    ivReopen.setVisibility(View.VISIBLE);
                    ivReclose.setVisibility(View.GONE);
                    RequiresState = SPREAD_STATE;
                }
                break;
            case R.id.cv_success:
                if (SuccessState == SPREAD_STATE) {
                    tvSuccess.setMaxLines(CONTENT_DESC_MAX_LINE);
                    tvSuccess.requestLayout();
                    ivSuopen.setVisibility(View.GONE);
                    ivSuclose.setVisibility(View.VISIBLE);
                    SuccessState = SHRINK_UP_STATE;
                } else if (SuccessState == SHRINK_UP_STATE) {
                    tvSuccess.setMaxLines(Integer.MAX_VALUE);
                    tvSuccess.requestLayout();
                    ivSuopen.setVisibility(View.VISIBLE);
                    ivSuclose.setVisibility(View.GONE);
                    SuccessState = SPREAD_STATE;
                }
                break;
            case R.id.cv_introduction:
                if (IntroductionState == SPREAD_STATE) {
                    tvIntroduction.setMaxLines(CONTENT_DESC_MAX_LINE);
                    tvIntroduction.requestLayout();
                    ivInopen.setVisibility(View.GONE);
                    ivInclose.setVisibility(View.VISIBLE);
                    IntroductionState = SHRINK_UP_STATE;
                } else if (IntroductionState == SHRINK_UP_STATE) {
                    tvIntroduction.setMaxLines(Integer.MAX_VALUE);
                    tvIntroduction.requestLayout();
                    ivInopen.setVisibility(View.VISIBLE);
                    ivInclose.setVisibility(View.GONE);
                    IntroductionState = SPREAD_STATE;
                }
                break;
        }
    }
}
