package com.urgoo.main.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.adapter.CounselorInfoAdapter;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.activities.CounselorSearchActivity;
import com.urgoo.domain.CounselorBannerListEntiy;
import com.urgoo.domain.TranslateCounselorEntiy;
import com.urgoo.domain.ZoomLiveEntiy;
import com.urgoo.main.biz.MainManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/7/11.
 */
public class CounselorFragment extends BaseFragment implements StringRequestCallBack, View.OnClickListener {
    private UltimateRecyclerView recyclerView;
    private CounselorInfoAdapter counselorInfoAdapter;
    private ArrayList<TranslateCounselorEntiy> tcEntiys = new ArrayList<>();
    private List<ZoomLiveEntiy> zoomLiveEntiys = new ArrayList<>();
    private List<CounselorBannerListEntiy> cblEntiys = new ArrayList<>();
    private View mHeaderCounselorView;
//    private CounselorBtn btnGw1;
//    private CounselorBtn btnGw2;
//    private CounselorBtn btnGw3;
//    private LinearLayout llLive;
    private Button btnSearch;
    private RelativeLayout rlTitleBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.counselor_activity, container, false);
        initViews();
        return viewContent;
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) viewContent.findViewById(R.id.recycler_view);
        rlTitleBar = (RelativeLayout) viewContent.findViewById(R.id.rl_title_bar);
        btnSearch = (Button) viewContent.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);


        mHeaderCounselorView = LayoutInflater.from(getActivity()).inflate(R.layout.select_counselor, recyclerView, false);
//        llLive = (LinearLayout) mHeaderCounselorView.findViewById(R.id.image_gallery);

//        //杨德成 20160729
//        llZhibo = (LinearLayout) mHeaderCounselorView.findViewById(R.id.ll_zhibo);
//        llZhibo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ZhiBoListManageActivity.class));
//            }
//        });

//        btnGw1 = (CounselorBtn) mHeaderCounselorView.findViewById(R.id.btn_gw1);
//        btnGw2 = (CounselorBtn) mHeaderCounselorView.findViewById(R.id.btn_gw2);
//        btnGw3 = (CounselorBtn) mHeaderCounselorView.findViewById(R.id.btn_gw3);
//        btnGw1.setOnClickListener(this);
//        btnGw2.setOnClickListener(this);
//        btnGw3.setOnClickListener(this);

        counselorInfoAdapter = new CounselorInfoAdapter(getActivity(), tcEntiys);
        counselorInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(CounselorActivity.COUNSELOR_ID, counselorInfoAdapter.getItem(position).getCounselorId());
                Util.openActivityWithBundle(getActivity(), CounselorActivity.class, extras);
            }
        });
        recyclerView.setAdapter(counselorInfoAdapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalDy = 0;
                totalDy = recyclerView.getChildAt(0).getTop();
                if (mHeaderCounselorView.getHeight() > 0) {
                    //define it for scroll height
                    if (totalDy < -5) {
                        rlTitleBar.setBackgroundResource(R.color.common_botton_bar_blue);
                        setBtnSearchColor(R.drawable.btn_search_fff, R.color.tvcfcfcf, R.drawable.ic_search);
                    } else {
                        rlTitleBar.setBackgroundResource(R.color.transparent);
                        setBtnSearchColor(R.drawable.btn_search, R.color.ffffff, R.drawable.ic_search_touming);
                    }
                }
            }
        });
        recyclerView.setNormalHeader(mHeaderCounselorView);

//        getCounselorList();
//        getZoomLiveList();
        getMyCounselorListTop();
    }

    /**
     * 设置滑动搜索按钮的颜色
     *
     * @param backColor
     * @param textColor
     * @param draw
     */
    private void setBtnSearchColor(int backColor, int textColor, int draw) {
        btnSearch.setBackgroundResource(backColor);
        btnSearch.setTextColor(getResources().getColor(textColor));
        Drawable drawable = getResources().getDrawable(draw);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnSearch.setCompoundDrawables(drawable, null, null, null);
    }

    private void getCounselorList() {
        MainManager.getInstance(getActivity()).selectCounselorBannerList(this);
    }

    private void getZoomLiveList() {
        MainManager.getInstance(getActivity()).selectZoomLiveList(this);
    }

    private void getMyCounselorListTop() {
        MainManager.getInstance(getActivity()).getMyCounselorListTop(this);
    }

//    /**
//     * 设置banner下面按钮默认图片
//     */
//    private void setGwBtn() {
//        btnGw1.setText(getString(R.string.main_mggw));
//        btnGw1.setImgResource("res:// /" + R.drawable.ic_mg_counselor);
//        btnGw2.setText(getString(R.string.main_mbgw));
//        btnGw2.setImgResource("res:// /" + R.drawable.ic_mb_counselor);
//        btnGw3.setText(getString(R.string.main_allgw));
//        btnGw3.setImgResource("res:// /" + R.drawable.ic_all_counselor);
//    }

    /**
     * 直播滑动页
     */
//    private void setLiveImg() {
//        for (int i = 0; i < zoomLiveEntiys.size(); i++) {
//            LayoutInflater myInflater = LayoutInflater.from(mHeaderCounselorView.getContext());
//            View view = myInflater.inflate(R.layout.live_item, llLive, false); //使用inflate获取phtoview布局里面的myGallery视窗
//            final int finalI = i;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle extras = new Bundle();
//                    extras.putString("liveId", zoomLiveEntiys.get(finalI).getLiveId());
//                    Util.openActivityWithBundle(getActivity(), ZhiBodDetailActivity.class, extras);
//                }
//            });
//            SimpleDraweeView sdvAvatar = (SimpleDraweeView) view.findViewById(R.id.img_live);
//            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//            TextView tvTitleSub = (TextView) view.findViewById(R.id.tv_msg);
//            tvTitle.setText(zoomLiveEntiys.get(i).getTitle());
//            tvTitleSub.setText(zoomLiveEntiys.get(i).getTitleSub());
//            Util.setImage(sdvAvatar, zoomLiveEntiys.get(i).getMasterPic());
////            sdvAvatar.setImageURI(Uri.parse(zoomLiveEntiys.get(i).getMasterPic()));
//            llLive.addView(view);  //把添加过资源后的view视图重新添加到myGallery中
//        }
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.setMargins(30, 0, 0, 0);
//        Button button = new Button(mHeaderCounselorView.getContext());
//        button.setBackgroundResource(R.drawable.btn_half_corner);
//        button.setText(getString(R.string.main_more));
//        button.setPadding(40, 0, 40, 0);
//        button.setTextSize(12);
//        button.setTextColor(getResources().getColor(R.color.ffffff));
//        button.setEms(1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ZhiBoListManageActivity.class));
//            }
//        });
//        llLive.addView(button, lp);
//    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetMyCounselorListTop:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    tcEntiys = gson.fromJson(jsonObject.getJSONArray("counselorListInfoList").toString(), new TypeToken<List<TranslateCounselorEntiy>>() {
                    }.getType());
                    if (tcEntiys != null) {
                        counselorInfoAdapter.clear();
                        counselorInfoAdapter.addData(tcEntiys);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
//            case EventCodeSelectZoomLive:
//                try {
//                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
//                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
//                    zoomLiveEntiys = gson.fromJson(jsonObject.getJSONArray("liveList").toString(), new TypeToken<List<ZoomLiveEntiy>>() {
//                    }.getType());
//                    if (zoomLiveEntiys != null) {
//                        setLiveImg();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case EventCodeSelectCounselor:
//                try {
//                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
//                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
//                    cblEntiys = gson.fromJson(jsonObject.getJSONArray("counselorBannerList").toString(), new TypeToken<List<CounselorBannerListEntiy>>() {
//                    }.getType());
//                    if (cblEntiys.size() > 0) {
//                        btnGw1.setText(cblEntiys.get(0).getDes());
//                        btnGw1.setImgResource(cblEntiys.get(0).getPicUrl());
//                        btnGw2.setText(cblEntiys.get(1).getDes());
//                        btnGw2.setImgResource(cblEntiys.get(1).getPicUrl());
//                        btnGw3.setText(cblEntiys.get(2).getDes());
//                        btnGw3.setImgResource(cblEntiys.get(2).getPicUrl());
//                    } else {
//                        setGwBtn();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }

    /**
     * 获取消息中的扩展 weichat是否存在并返回jsonObject
     * @param message
     * @return
     */
    private JSONObject getWeichatJSONObject(EMMessage message){
        JSONObject weichatJson = null;
        try {
            String weichatString = message.getStringAttribute("weichat", null);
            if(weichatString == null){
                weichatJson = new JSONObject();
            }else{
                weichatJson = new JSONObject(weichatString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weichatJson;
    }


    @Override
    public void onClick(View v) {
        Bundle extras = new Bundle();
        switch (v.getId()) {
//            case R.id.btn_gw1:
//                if (cblEntiys != null) {
//                    extras.putString(CounselorList.SERVICE_TYPE, cblEntiys.get(0).getType());
//                    Util.openActivityWithBundle(getActivity(), CounselorList.class, extras);
//                }
//                break;
//            case R.id.btn_gw2:
//                if (cblEntiys != null) {
//                    extras.putString(CounselorList.SERVICE_TYPE, cblEntiys.get(1).getType());
//                    Util.openActivityWithBundle(getActivity(), CounselorList.class, extras);
//                }
//                break;
//            case R.id.btn_gw3:
//                if (cblEntiys != null) {
//                    extras.putString(CounselorList.SERVICE_TYPE, cblEntiys.get(2).getType());
//                    Util.openActivityWithBundle(getActivity(), CounselorList.class, extras);
//                }
//                break;


            case R.id.btn_search:
                startActivity(new Intent(getActivity(), CounselorSearchActivity.class));
//                try {
//                    EMMessage message = EMMessage.createTxtSendMessage("测试测试测试", ZWConfig.ACTION_CustomerService);
//
//
//                    JSONObject weichatJson = getWeichatJSONObject(message);
//
//                    JSONObject agentJson = getWeichatJSONObject(message);
//                    agentJson.put("userNickname", "aaaaaaaaaaaaaaaaaaa");
//                    agentJson.put("avatar", "http://urgooprd.oss-cn-qingdao.aliyuncs.com/zhibo/zhibo_1471661216718.jpg");
//                    weichatJson.put("agent", agentJson);
//
//
//                    weichatJson.put("agentUsername", "lisa.zhu@urgoo.cn");
//                    message.setAttribute("weichat", weichatJson);
//                    message.setChatType(EMMessage.ChatType.Chat);
//                    EMClient.getInstance().chatManager().sendMessage(message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }
}
