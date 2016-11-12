package com.urgoo.live.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.counselor.activities.CounselorDetailActivity;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.domain.ShareDetail;
import com.urgoo.live.adapter.CommentAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.event.FollowVideoEvent;
import com.urgoo.live.model.Comment;
import com.urgoo.live.model.VideoDetial;
import com.urgoo.live.view.CommentInputBox;
import com.urgoo.live.view.CommentInputToolBoxListener;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by bb on 2016/10/13.
 */
public class VideoDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_VIDEO_ID = "video_id";
    private String videoId;
    private ShareDetail shareDetail;
    private VideoDetial videoDetial;
    private List<Comment> comments = new ArrayList<>();
    private int page = 0;

    private UltimateRecyclerView recyclerView;
    private CommentAdapter adapter;
    private SimpleDraweeView sdvBack;
    private View mHeaderCounselorView;
    private SimpleDraweeView sdvAvater;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvDes;
    private ImageView ivShare;
    private ImageView ivCollect;
    private ImageView ivBack;
    private ImageView ivPlay;
    private CommentInputBox commentInputBox;
    private View mVideoLayout;
    private View mFmVideo;
    private JCVideoPlayerStandard mVideoView;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private SensorManager sensorManager;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        setContentView(R.layout.activity_video_detail);
        videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        initViews();
        getVideoDetail();
        getCommentList();
    }

        @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void getVideoDetail() {
        LiveManager.getInstance(this).getVideoDetail(videoId, this);
    }

    private void getCommentList() {
        LiveManager.getInstance(this).getCommentList(videoId, page, "2", this);
    }

    public void postComment(String id, String content, String targetId) {
        LiveManager.getInstance(this).postComment(id, content, "2", targetId, this);
    }

    /**
     * 刷新评论列表
     * <p/>
     * <p>如果评论数小于一页或者已加载完所有，则刷新</p>
     */
    public void refreshCommentList() {
        if (adapter != null) {
            page = 0;
            recyclerView.setRefreshing(true);
            getCommentList();
//            if (adapter.getAdapterItemCount() < 10) {
//                recyclerView.setRefreshing(true);
//                getCommentList();
//            } else {
//                /**
//                 * 如果之前已经调过disableLoadmore()，则可以调此方法重新启用加载更多
//                 * ps:如果直接之前调过disableLoadmore(),再调用enableLoadmore()重新启用加载更多的话
//                 *    会出现列表向下滑动以后才能触发上拉加载
//                 */
//                if (!recyclerView.isLoadMoreEnabled()) {
//                    recyclerView.reenableLoadmore();
//                }
//            }
        }
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.videoView);
        mVideoLayout = findViewById(R.id.video_layout);
        mFmVideo = findViewById(R.id.fm_video);
        setVideoAreaSize();

        commentInputBox = (CommentInputBox) findViewById(R.id.ll_comment);
        commentInputBox.setInputHint("评论...");

        sdvBack = (SimpleDraweeView) findViewById(R.id.sdv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivCollect = (ImageView) findViewById(R.id.iv_collect);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
        commentInputBox.setCommentInputToolBoxListener(new CommentInputToolBoxListener() {
            @Override
            public void onSend(CharSequence inputMsg) {
                showLoadingDialog();
                postComment(videoId, inputMsg.toString(), "0");
            }
        });

        mHeaderCounselorView = LayoutInflater.from(this).inflate(R.layout.activity_video_detail_header, recyclerView, false);
        sdvAvater = (SimpleDraweeView) mHeaderCounselorView.findViewById(R.id.sdv_avatar);
        sdvAvater.setOnClickListener(this);
        tvName = (TextView) mHeaderCounselorView.findViewById(R.id.tv_name);
        tvDate = (TextView) mHeaderCounselorView.findViewById(R.id.tv_date);
        tvDes = (TextView) mHeaderCounselorView.findViewById(R.id.tv_des);

        adapter = new CommentAdapter(this, comments, videoId);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
        recyclerView.setNormalHeader(mHeaderCounselorView);
        recyclerView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState) {
                if (comments != null) {
                    commentInputBox.showReplyInputToolBox();
                }
                commentInputBox.loseFocus();
                commentInputBox.setInputHint("评论...");
            }
        });
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getCommentList();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getCommentList();
            }
        });
    }

    /**
     * 设置收藏图片
     */
    private void setCollect() {
        if (videoDetial != null) {
            if (videoDetial.getIsFollowed().equals("1")) {
                ivCollect.setImageResource(R.drawable.ic_collected_white);
            } else {
                ivCollect.setImageResource(R.drawable.ic_collect_white);
            }
        }
    }

    private void onFavoriteArticle() {
        if (videoDetial != null) {
            showLoadingDialog();
            if (videoDetial.getIsFollowed().equals("1")) {
                CounselorManager.getInstance(this).getCancleFollow(this, videoId, "5");
            } else {
                CounselorManager.getInstance(this).getAddFollow(this, videoId, "5");
            }
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodePostComment:
                showToastSafe("评论成功");
                if (commentInputBox != null) {
                    commentInputBox.loseFocus();
                    commentInputBox.setInputHint("评论...");
                    commentInputBox.resetToolBox();
                }
                refreshCommentList();
                break;
            case EventCodeCancleFollow:
                showToastSafe("取消收藏");
                videoDetial.setIsFollowed("0");
                setCollect();
                EventBus.getDefault().post(new FollowVideoEvent(Integer.parseInt(videoId)));
                break;
            case EventCodeAddFollow:
                showToastSafe("收藏成功");
                videoDetial.setIsFollowed("1");
                setCollect();
                EventBus.getDefault().post(new FollowVideoEvent(Integer.parseInt(videoId)));
                break;
            case EventCodeGetCommentList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    comments = gson.fromJson(jsonObject.getJSONArray("liveCommentList").toString(), new TypeToken<List<Comment>>() {
                    }.getType());
                    if (recyclerView.mSwipeRefreshLayout.isRefreshing()) {
                        adapter.clear();
                        adapter.addData(comments);
                    } else {
                        if (!comments.isEmpty()) {
                            adapter.addData(comments);
                        }
                    }
                    if (comments.size() < 10) {
                        recyclerView.disableLoadmore();
                    } else {
                        recyclerView.enableLoadmore();
                    }
                    adapter.setCommentToolBox(commentInputBox);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetVideoDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    videoDetial = gson.fromJson(jsonObject.getJSONObject("albumDetail").toString(), new TypeToken<VideoDetial>() {
                    }.getType());
                    sdvAvater.setImageURI(Uri.parse(videoDetial.getUserIcon()));
                    sdvBack.setImageURI(Uri.parse(videoDetial.getCover()));
                    tvName.setText(getString(R.string.host_name, videoDetial.getEnName()));
                    tvDate.setText(getString(R.string.host_time, videoDetial.getVideoDate()));
                    tvDes.setText(videoDetial.getDes());
                    mVideoView.setUp(videoDetial.getVideo(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
                    setCollect();
                    shareDetail = gson.fromJson(jsonObject.getJSONObject("shareDetail").toString(), new TypeToken<ShareDetail>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_avatar:
                if (!videoDetial.getUserInfoSubId().equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, videoDetial.getUserInfoSubId());
                    bundle.putString(CounselorDetailActivity.EXTRA_TITLE, videoDetial.getEnName());
                    bundle.putBoolean(CounselorDetailActivity.EXTRA_FROM, true);
                    Util.openActivityWithBundle(this, CounselorDetailActivity.class, bundle);
                }
                break;
            case R.id.iv_play:
                sdvBack.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
                mFmVideo.setVisibility(View.VISIBLE);
                mVideoView.startButton.performClick();//模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                if (shareDetail != null) {
                    ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, shareDetail.url, shareDetail.weibo, shareDetail.pengyouquan);
                }
                break;
            case R.id.iv_collect:
                onFavoriteArticle();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
            Bundle extras = new Bundle();
            extras.putInt(MainActivity.EXTRA_TAB, 1);
            Util.openActivityWithBundle(VideoDetailActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();
        } else {
            super.onBackPressed();
        }
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
