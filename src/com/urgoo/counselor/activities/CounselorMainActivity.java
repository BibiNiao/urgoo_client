package com.urgoo.counselor.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.collect.event.FollowCounselorEvent;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.Counselor;
import com.urgoo.message.activities.ChatRobotActivity;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/10/20.
 */
public class CounselorMainActivity extends NavToolBarActivity implements View.OnClickListener {
    public static final String EXTRA_COUNSELOR_ID = "counselor_id";
    private String counselorId;
    private float RATIO = 0.862f;

    private RelativeLayout rlAvatar;
    private ImageView ivPlay;
    private ImageView ivIn;
    private TextView tvEvaluate;
    private TextView tvLocation;
    private TextView tvName;
    private TextView tvSchool;
    private TextView tvTag1;
    private TextView tvTag2;
    private SimpleDraweeView sdvAvatar;
    private TextView tvFraction;
    private Button btnData;
    private Button btnContact;
    private Counselor counselor;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_counselor_main, null);
        counselorId = getIntent().getStringExtra(EXTRA_COUNSELOR_ID);
        setNavTitleText("顾问简介");
        initViews(view);
        if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
            setSwipeBackEnable(false);
        }
        return view;
    }

    private void initViews(View view) {
        rlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        ivIn = (ImageView) view.findViewById(R.id.iv_in);
        tvEvaluate = (TextView) view.findViewById(R.id.tv_evaluate);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSchool = (TextView) view.findViewById(R.id.tv_school);
        tvTag1 = (TextView) view.findViewById(R.id.tv_tag1);
        tvTag2 = (TextView) view.findViewById(R.id.tv_tag2);
        sdvAvatar = (SimpleDraweeView) view.findViewById(R.id.sdv_avatar);
        tvFraction = (TextView) view.findViewById(R.id.tv_fraction);
        btnContact = (Button) view.findViewById(R.id.btn_contact);
        btnData = (Button) view.findViewById(R.id.btn_data);
        btnData.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        tvEvaluate.setOnClickListener(this);
        getMyCounselorInfo();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (counselor != null) {
                    ShareUtil.share(this, counselor.getShareDetail().title, counselor.getShareDetail().text,
                            counselor.getShareDetail().pic, ZWConfig.URGOOURL_BASE + counselor.getShareDetail().url, counselor.getShareDetail().weibo, counselor.getShareDetail().pengyouquan);
                }
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
        if (counselor != null) {
            if (counselor.getIsAttention().equals("1")) {
                if (collectMenu != null) collectMenu.setIcon(R.drawable.ic_iscollected);
            } else {
                if (collectMenu != null) collectMenu.setIcon(R.drawable.ic_collect);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void onFavoriteArticle() {
        if (counselor != null) {
            showLoadingDialog();
            if (counselor.getIsAttention().equals("1")) {
                CounselorManager.getInstance(this).getCancleFollow(this, counselorId, "1");
            } else {
                CounselorManager.getInstance(this).getAddFollow(this, counselorId, "1");
            }
        }
    }

    private void getMyCounselorInfo() {
        CounselorManager.getInstance(this).getCounselorInfoMain(this, counselorId);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeCancleFollow:
                showToastSafe("取消收藏");
                counselor.setIsAttention("0");
                onPrepareOptionsMenu(mToolbar.getMenu());
                EventBus.getDefault().post(new FollowCounselorEvent(counselorId, "0"));
                break;
            case EventCodeAddFollow:
                showToastSafe("收藏成功");
                counselor.setIsAttention("1");
                onPrepareOptionsMenu(mToolbar.getMenu());
                EventBus.getDefault().post(new FollowCounselorEvent(counselorId, "1"));
                break;
            case EventCodeMyCounselor:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    counselor = gson.fromJson(jsonObject.getJSONObject("counselorDetail").toString(), new TypeToken<Counselor>() {
                    }.getType());
                    onPrepareOptionsMenu(mToolbar.getMenu());
                    if (Util.isEmpty(counselor.getLinkedin())) {
                        ivIn.setVisibility(View.GONE);
                    } else {
                        ivIn.setVisibility(View.VISIBLE);
                    }
                    if (Util.isEmpty(counselor.getShareVedio())) {
                        ivPlay.setVisibility(View.GONE);
                    } else {
                        ivPlay.setVisibility(View.VISIBLE);
                    }
                    if (counselor.getStudentWords().equals("0")) {
                        tvEvaluate.setVisibility(View.GONE);
                    } else {
                        tvEvaluate.setVisibility(View.VISIBLE);
                        tvEvaluate.setText(getString(R.string.find_pingjia, counselor.getStudentWords()));
                    }
                    tvName.setText(counselor.getEnName());
                    tvLocation.setText(counselor.getHabitualResidence());
                    tvSchool.setText(counselor.getSchool());
                    int width = Util.getDeviceWidth(this);
                    rlAvatar.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (RATIO * width)));
                    sdvAvatar.setImageURI(Uri.parse(counselor.getUserIcon()));
                    tvFraction.setText(counselor.getStarMark());
                    showTag(tvTag1, tvTag2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 显示TAG
     *
     * @param tvTag1
     * @param tvTag2
     */
    private void showTag(TextView tvTag1, TextView tvTag2) {
        if (counselor.getOrgs() != null && counselor.getOrgs().size() > 0) {
            if (counselor.getOrgs().size() == 1) {
                tvTag1.setText(counselor.getOrgs().get(0));
                tvTag2.setVisibility(View.GONE);
            } else {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
                tvTag1.setText(counselor.getOrgs().get(0));
                tvTag2.setText(counselor.getOrgs().get(1));
            }
        } else {
            tvTag1.setVisibility(View.GONE);
            tvTag2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle;
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_contact:
                bundle = new Bundle();
                bundle.putString(ChatRobotActivity.EXTRA_COUNSELOR_ID, counselorId);
                Util.openActivityWithBundle(this, ChatRobotActivity.class, bundle);
                break;
            case R.id.iv_in:
                Uri uri = Uri.parse(counselor.getLinkedin());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.tv_evaluate:
                bundle = new Bundle();
                bundle.putString(EXTRA_COUNSELOR_ID, counselor.getCounselorId());
                Util.openActivityWithBundle(this, StuEvaluationAcitivity.class, bundle);
                break;
            case R.id.btn_data:
                bundle = new Bundle();
                bundle.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, counselor.getCounselorId());
                bundle.putString(CounselorDetailActivity.EXTRA_TITLE, counselor.getEnName());
                bundle.putBoolean(CounselorDetailActivity.EXTRA_FROM, false);
                Util.openActivityWithBundle(this, CounselorDetailActivity.class, bundle);
                break;
            case R.id.iv_play:
                if (counselor != null) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(counselor.getShareVedio()), "video/mp4");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
            Bundle extras = new Bundle();
            extras.putInt(MainActivity.EXTRA_TAB, 0);
            Util.openActivityWithBundle(CounselorMainActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNavLeftClick(View v) {
        onBackPressed();
    }


    //  监听返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
