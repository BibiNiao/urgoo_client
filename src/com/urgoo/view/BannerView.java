package com.urgoo.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.domain.BannerItem;
import com.urgoo.flashview.FlashView;
import com.urgoo.flashview.listener.FlashViewListener;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.live.activities.ZhiBodDetailActivity;
import com.zw.express.tool.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class BannerView extends FlashView implements StringRequestCallBack, FlashViewListener {

    // 750*580 banner的图片尺寸 580/750=0.653f
    public static final float RATIO = 0.653f;

    private Context mContext;
    // banner信息
    private List<BannerItem> mBannerItemsList;

    public BannerView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public void init() {
        setOnPageClickListener(this);
        fetchBannerInfo();
//		try {
//            JSONObject jsonObject = new JSONObject("{\"body\":{\"totalSize\":3,\"bannerList\":[{\"picUrl\":\"http:\\/\\/urgootest.oss-cn-qingdao.aliyuncs.com\\/usericon\\/100_1467959819889.jpg\",\"targetId\":3,\"type\":\"3\"},{\"picUrl\":\"http:\\/\\/urgootest.oss-cn-qingdao.aliyuncs.com\\/usericon\\/100_1467959819889.jpg\",\"targetId\":2,\"type\":\"2\"},{\"picUrl\":\"http:\\/\\/urgootest.oss-cn-qingdao.aliyuncs.com\\/usericon\\/100_1467959819889.jpg\",\"targetId\":1,\"type\":\"1\"}]},\"header\":{\"code\":\"200\",\"serverTime\":\"1468307847\",\"message\":\"success\"}}");
//            JSONArray array = new JSONObject(jsonObject.getString("body")).getJSONArray("bannerList");
//			mBannerItemsList = parseBanner(array);
//			loadBannerInfo();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }

    /**
     * 向后台获取banner的各种信息
     */
    public void fetchBannerInfo() {
        HashMap<String, String> params = new HashMap<>();
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetBanner, ZWConfig.URL_requestGetBanner, params, this);
    }

    public void resetBannerSize() {
        final int width = Util.getDeviceWidth(mContext);
        setLayoutParams(new LinearLayout.LayoutParams(width, (int) (RATIO * width)));
    }


    private void loadBannerInfo() {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (BannerItem item : mBannerItemsList) {
            imageUrls.add(item.getPicUrl());
        }
        setRatio(0.653f);
        setImageUris(imageUrls);
    }

    private List<BannerItem> parseBanner(JSONArray jsonArray) {
        List<BannerItem> result = new ArrayList<>();
        BannerItem item;
        JSONObject jsonObj;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                item = new BannerItem();
                item.setType(jsonObj.getString("type"));
                item.setPicUrl(jsonObj.getString("picUrl"));
                item.setTargetId(jsonObj.getString("targetId"));
                result.add(item);
            }
        } catch (JSONException e) {
            return result;
        }
        return result;
    }

    @Override
    public void onClick(int position) {
        if (mBannerItemsList == null) return;
        BannerItem bannerItem = mBannerItemsList.get(position);
        Bundle extras = new Bundle();
        String type = bannerItem.getType();
        switch (type) {
            case BannerItem.ACTION_OPEN_H5:
                extras.putString(BaseWebViewActivity.EXTRA_URL, String.valueOf(bannerItem.getTargetId()));
                Util.openActivityWithBundle(mContext, BaseWebViewActivity.class, extras);
                break;
            case BannerItem.ACTION_OPEN_ZOOM:
                extras.putString("liveId", String.valueOf(bannerItem.getTargetId()));
                Util.openActivityWithBundle(mContext, ZhiBodDetailActivity.class, extras);
                break;
            case BannerItem.ACTION_OPEN_GUWEN:
                extras.putString(CounselorActivity.COUNSELOR_ID, String.valueOf(bannerItem.getTargetId()));
                Util.openActivityWithBundle(mContext, CounselorActivity.class, extras);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(EventCode eventCode, String response) {
        if (eventCode == EventCode.EventCodeGetBanner) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                JSONArray array = new JSONObject(jsonObj.getString("body")).getJSONArray("bannerList");
                if (mBannerItemsList == null) {
                    mBannerItemsList = parseBanner(array);
                    loadBannerInfo();
                } else {
                    List<BannerItem> newList = parseBanner(array);
                    if (!mBannerItemsList.toString().equals(newList.toString())) {
                        mBannerItemsList = newList;
                        loadBannerInfo();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(EventCode eventCode, Call call) {

    }
}