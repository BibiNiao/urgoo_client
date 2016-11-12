package com.urgoo.live.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.domain.ShareDetail;
import com.urgoo.live.adapter.AlbumDetailAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.event.FollowVideoEvent;
import com.urgoo.live.model.AlbumDetail;
import com.urgoo.live.model.AlbumDetailList;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/10/12.
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_ALBUM_ID = "album_id";
    private int albumId;
    private int page = 0;
    private float RATIO = 0.514f;
    private float RATIO2 = 0.13f;
    private UltimateRecyclerView recyclerView;
    private View mHeaderCounselorView;
    private AlbumDetail albumDetail;
    private List<AlbumDetailList> albumDetailList = new ArrayList<>();
    private ShareDetail shareDetail;
    private AlbumDetailAdapter adapter;

    private SimpleDraweeView sdvAvater;
    private SimpleDraweeView sdvBack;
    private RelativeLayout rlBack;
    private TextView tvTitle;
    private TextView tvVideo;
    private TextView tvDes;
    private TextView tvTime;
    private ImageView ivShare;
    private ImageView ivCollect;
    private ImageView ivBack;
    private ImageView ivPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        albumId = getIntent().getIntExtra(EXTRA_ALBUM_ID, 0);
        initViews();
    }

    private void initViews() {
        int width = Util.getDeviceWidth(this);
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        mHeaderCounselorView = LayoutInflater.from(this).inflate(R.layout.activity_album_head, recyclerView, false);
        sdvAvater = (SimpleDraweeView) mHeaderCounselorView.findViewById(R.id.sdv_avatar);
        sdvBack = (SimpleDraweeView) mHeaderCounselorView.findViewById(R.id.sdv_back);
        tvTitle = (TextView) mHeaderCounselorView.findViewById(R.id.tv_title);
        tvVideo = (TextView) mHeaderCounselorView.findViewById(R.id.tv_video);
        tvTime = (TextView) mHeaderCounselorView.findViewById(R.id.tv_time_all);
        tvDes = (TextView) mHeaderCounselorView.findViewById(R.id.tv_des);
        ivShare = (ImageView) mHeaderCounselorView.findViewById(R.id.iv_share);
        ivShare.setOnClickListener(this);
        ivCollect = (ImageView) mHeaderCounselorView.findViewById(R.id.iv_collect);
        ivCollect.setOnClickListener(this);
        ivBack = (ImageView) mHeaderCounselorView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ivPlay = (ImageView) mHeaderCounselorView.findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        sdvBack.setLayoutParams(new RelativeLayout.LayoutParams(width, (int) (RATIO * width)));
        rlBack = (RelativeLayout) mHeaderCounselorView.findViewById(R.id.rl_back);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (RATIO * width) + ((int) (RATIO2 * width) / 2));
        rlBack.setLayoutParams(layoutParams);

        adapter = new AlbumDetailAdapter(this, albumDetailList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        recyclerView.setNormalHeader(mHeaderCounselorView);
        adapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.bottom_progressbar, null));
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getAlbumDetail();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getAlbumDetail();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle extras = new Bundle();
                extras.putString(VideoDetailActivity.EXTRA_VIDEO_ID, adapter.getItem(position).getAlbumId());
                Util.openActivityWithBundle(AlbumActivity.this, VideoDetailActivity.class, extras);
            }
        });
        getAlbumDetail();
    }

    private void getAlbumDetail() {
        LiveManager.getInstance(this).getAlbumDetail(albumId, page, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeCancleFollow:
                showToastSafe("取消收藏");
                albumDetail.setIsFollowed("0");
                checkCollect();
                EventBus.getDefault().post(new FollowVideoEvent(albumId));
                break;
            case EventCodeAddFollow:
                showToastSafe("收藏成功");
                albumDetail.setIsFollowed("1");
                checkCollect();
                EventBus.getDefault().post(new FollowVideoEvent(albumId));
                break;
            case EventCodeGetAlbumDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    albumDetail = gson.fromJson(jsonObject.toString(), new TypeToken<AlbumDetail>() {
                    }.getType());
                    sdvBack.setImageURI(Uri.parse(albumDetail.getCoverBack()));
                    sdvAvater.setImageURI(Uri.parse(albumDetail.getCover()));
                    tvTitle.setText(albumDetail.getTitle());
                    tvDes.setText(albumDetail.getDes());
                    tvVideo.setText(getString(R.string.alubm_video, albumDetail.getTotalVideoUrlList().size()));
                    shareDetail = albumDetail.getShareDetail();
                    tvTime.setText(getString(R.string.alubm_time, Util.secToTime(albumDetail.getTimeLong())));
                    checkCollect();
                    List<AlbumDetailList> albumDetailList = gson.fromJson(jsonObject.getJSONArray("albumDetailList").toString(), new TypeToken<List<AlbumDetailList>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(albumDetailList);
                    } else {
                        if (!albumDetailList.isEmpty()) {
                            adapter.addData(albumDetailList);
                        }
                    }
                    if (albumDetailList.size() < 10) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 设置收藏图片
     */
    private void checkCollect() {
        if (albumDetail != null && albumDetail.getIsFollowed().equals("1")) {
            ivCollect.setImageResource(R.drawable.ic_iscollected);
        } else {
            ivCollect.setImageResource(R.drawable.ic_collect);
        }
    }

    private void onFavoriteArticle() {
        if (albumDetail != null) {
            showLoadingDialog();
            if (albumDetail.getIsFollowed().equals("1")) {
                CounselorManager.getInstance(this).getCancleFollow(this, String.valueOf(albumId), "4");
            } else {
                CounselorManager.getInstance(this).getAddFollow(this, String.valueOf(albumId), "4");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(albumDetail.getTotalVideoUrlList()), "video/mp4");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_collect:
                onFavoriteArticle();
                break;
            case R.id.iv_share:
                ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, shareDetail.url, shareDetail.weibo, shareDetail.pengyouquan);
                break;
        }
    }
}
