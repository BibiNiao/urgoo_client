package com.urgoo.live.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.Interface.Constants;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorDetailActivity;
import com.urgoo.counselor.activities.CounselorMainActivity;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.domain.ShareDetail;
import com.urgoo.live.adapter.LiveCommentAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.event.FollowVideoEvent;
import com.urgoo.live.model.Comment;
import com.urgoo.live.model.LiveDetail;
import com.urgoo.live.view.CommentInputBox;
import com.urgoo.live.view.CommentInputToolBoxListener;
import com.urgoo.live.view.UniversalMediaController;
import com.urgoo.live.view.UniversalVideoView;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

/**
 * Created by bb on 2016/10/13.
 */
public class LiveDetailActivity extends BaseActivity implements Constants, ZoomSDKInitializeListener, MeetingServiceListener, UniversalVideoView.VideoViewCallback, View.OnClickListener {
    public static final String EXTRA_LIVE_ID = "live_id";
    public static final String EXTRA_FROM = "is_from_detail";
    private String liveId;
    private boolean isFromDetail;
    private ShareDetail shareDetail;
    private LiveDetail liveDetail;
    private List<Comment> comments = new ArrayList<>();
    private int page = 0;
    private ZoomSDK sdk;

    private UltimateRecyclerView recyclerView;
    private LiveCommentAdapter adapter;
    private SimpleDraweeView sdvBack;
    private View mHeaderCounselorView;
    private SimpleDraweeView sdvAvater;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvDes;
    private TextView tvNotice;
    private ImageView ivShare;
    private ImageView ivCollect;
    private ImageView ivBack;
    private ImageView ivPlay;
    private CommentInputBox commentInputBox;
    private View mVideoLayout;
    private View mFmVideo;
    private View llNotice;
    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        sdk = ZoomSDK.getInstance();
        if (savedInstanceState == null) {
            sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
            sdk.setGoogleDriveClientId(this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }
        liveId = getIntent().getStringExtra(EXTRA_LIVE_ID);
        isFromDetail = getIntent().getBooleanExtra(EXTRA_FROM, false);
        initViews();
        getZoomLiveDetail();
        getCommentList();
    }

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    public void MyJoinInstantMeeting(String meetingNo, String clientName) {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }
        MeetingService meetingService = zoomSDK.getMeetingService();
        int code = meetingService.joinMeeting(this, meetingNo, clientName);
        Log.d("meeting3", "code->" + code);
    }

    @Override
    public void onMeetingEvent(int i, int i1, int i2) {

    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
            registerMeetingServiceListener();
            //getZoomAccount();
        }
    }

    private void getZoomLiveDetail() {
        LiveManager.getInstance(this).getZoomLiveDetail(liveId, this);
    }

    private void getCommentList() {
        LiveManager.getInstance(this).getCommentList(liveId, page, "1", this);
    }

    private void postComment(String id, String content, String targetId) {
        LiveManager.getInstance(this).postComment(id, content, "1", targetId, this);
    }

    /**
     * 刷新评论列表
     * <p/>
     * <p>如果评论数小于一页或者已加载完所有，则刷新</p>
     */
    public void refreshCommentList() {
        if (adapter != null) {
            if (adapter.getAdapterItemCount() < 10) {
                recyclerView.setRefreshing(true);
                getCommentList();
            } else {
                /**
                 * 如果之前已经调过disableLoadmore()，则可以调此方法重新启用加载更多
                 * ps:如果直接之前调过disableLoadmore(),再调用enableLoadmore()重新启用加载更多的话
                 *    会出现列表向下滑动以后才能触发上拉加载
                 */
                if (!recyclerView.isLoadMoreEnabled()) {
                    recyclerView.reenableLoadmore();
                }
            }
        }
    }

    private void initViews() {
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoLayout = findViewById(R.id.video_layout);
        mFmVideo = findViewById(R.id.fm_video);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoViewCallback(this);
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
                postComment(liveId, inputMsg.toString(), "0");
            }
        });

        mHeaderCounselorView = LayoutInflater.from(this).inflate(R.layout.activity_live_detail_header, recyclerView, false);
        sdvAvater = (SimpleDraweeView) mHeaderCounselorView.findViewById(R.id.sdv_avatar);
        sdvAvater.setOnClickListener(this);
        tvName = (TextView) mHeaderCounselorView.findViewById(R.id.tv_name);
        tvDate = (TextView) mHeaderCounselorView.findViewById(R.id.tv_date);
        tvDes = (TextView) mHeaderCounselorView.findViewById(R.id.tv_des);
        tvNotice = (TextView) mHeaderCounselorView.findViewById(R.id.tv_notice);
        llNotice = mHeaderCounselorView.findViewById(R.id.ll_notice);

        adapter = new LiveCommentAdapter(this, comments, liveId);
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
        if (liveDetail != null) {
            if (liveDetail.getIsFollowed().equals("1")) {
                ivCollect.setImageResource(R.drawable.ic_collected_white);
            } else {
                ivCollect.setImageResource(R.drawable.ic_collect_white);
            }
        }
    }

    private void onFavoriteArticle() {
        if (liveDetail != null) {
            showLoadingDialog();
            if (liveDetail.getIsFollowed().equals("1")) {
                CounselorManager.getInstance(this).getCancleFollow(this, liveId, "6");
            } else {
                CounselorManager.getInstance(this).getAddFollow(this, liveId, "6");
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
                liveDetail.setIsFollowed("0");
                setCollect();
                EventBus.getDefault().post(new FollowVideoEvent(Integer.parseInt(liveId)));
                break;
            case EventCodeAddFollow:
                showToastSafe("收藏成功");
                liveDetail.setIsFollowed("1");
                setCollect();
                EventBus.getDefault().post(new FollowVideoEvent(Integer.parseInt(liveId)));
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
            case EventCodeZoomLiveDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    liveDetail = gson.fromJson(jsonObject.getJSONObject("liveDetail").toString(), new TypeToken<LiveDetail>() {
                    }.getType());
                    sdvAvater.setImageURI(Uri.parse(liveDetail.getUserIcon()));
                    sdvBack.setImageURI(Uri.parse(liveDetail.getMasterPic()));
                    tvName.setText(getString(R.string.host_name, liveDetail.getEnName()));
                    tvDate.setText(getString(R.string.host_time, liveDetail.getLiveStartTime()));
                    tvDes.setText(liveDetail.getIntroduce());
                    mVideoView.setVideoPath(liveDetail.getVideo());
                    if (Util.isEmpty(liveDetail.getLiveNotice())) {
                        llNotice.setVisibility(View.GONE);
                    } else {
                        tvNotice.setText(liveDetail.getLiveNotice());
                        llNotice.setVisibility(View.VISIBLE);
                    }
                    setCollect();
                    shareDetail = gson.fromJson(jsonObject.getJSONObject("shareDetail").toString(), new TypeToken<ShareDetail>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

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
                if (!liveDetail.getTargetId().equals("0")) {
                    if (isFromDetail) {
                        finish();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, liveDetail.getTargetId());
                        bundle.putString(CounselorDetailActivity.EXTRA_TITLE, liveDetail.getEnName());
                        bundle.putBoolean(CounselorDetailActivity.EXTRA_FROM, true);
                        Util.openActivityWithBundle(this, CounselorDetailActivity.class, bundle);
                    }
                }
                break;
            case R.id.iv_play:
                if (liveDetail.getStatus().equals("1")) {
                    if (liveDetail.getBalanceTime() > 0) {
                        showToastSafe("直播尚未开始,时间到了之后请刷新页面重试");
                    } else {
                        MyJoinInstantMeeting(liveDetail.getZoomNo(), spManager.getNickName() == null ? "某XX" : spManager.getNickName());
                    }
                } else if (liveDetail.getStatus().equals("2")) {
                    showToastSafe("直播视频还未准备好,请耐心等待");
                } else if (liveDetail.getStatus().equals("3")) {
                    sdvBack.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.GONE);
                    mFmVideo.setVisibility(View.VISIBLE);
                    mVideoView.start();
                    mVideoView.requestFocus();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                if (shareDetail != null) {
                    ShareUtil.share(this, shareDetail.title, shareDetail.text, shareDetail.pic, shareDetail.url);
                }
                break;
            case R.id.iv_collect:
                onFavoriteArticle();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else if (getIntent().getBooleanExtra(SplashActivity.EXTRA_FROM_PUSH, false)) {
            Bundle extras = new Bundle();
            extras.putInt(MainActivity.EXTRA_TAB, 1);
            Util.openActivityWithBundle(LiveDetailActivity.this, MainActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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