package com.urgoo.live.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.live.adapter.CommentAdapter;
import com.urgoo.live.adapter.CommentListAdapter;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.model.Comment;
import com.urgoo.live.model.ReviewComments;
import com.urgoo.live.view.CommentInputBox;
import com.urgoo.live.view.CommentInputToolBoxListener;
import com.urgoo.net.EventCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/10/18.
 */
public class CommentListActivity extends NavToolBarActivity {
    public static final String EXTRA_VIDEO_ID = "video_id";
    public static final String EXTRA_COMMENT_ID = "comment_id";
    public static final String EXTRA_FLAG = "flag";
    private String flag;
    private String liveId;
    private String commentId;
    private int page = 0;
    private UltimateRecyclerView recyclerView;
    private CommentInputBox commentInputBox;
    private CommentListAdapter adapter;
    private List<ReviewComments> comments = new ArrayList<>();

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_comment_list, null);
        initViews(view);
        liveId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        commentId = getIntent().getStringExtra(EXTRA_COMMENT_ID);
        flag = getIntent().getStringExtra(EXTRA_FLAG);
        setNavTitleText("评论");
        getReplayList();
        return view;
    }

    private void initViews(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        commentInputBox = (CommentInputBox) view.findViewById(R.id.ll_comment);
        commentInputBox.setInputHint("评论...");
        commentInputBox.setCommentInputToolBoxListener(new CommentInputToolBoxListener() {
            @Override
            public void onSend(CharSequence inputMsg) {
                showLoadingDialog();
                postComment(liveId, inputMsg.toString(), commentId);
            }
        });

        adapter = new CommentListAdapter(this, comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setRefreshing(true);
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
                getReplayList();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                getReplayList();
            }
        });
    }

    private void getReplayList() {
        LiveManager.getInstance(this).getReplayList(liveId, flag, commentId, page, this);
    }

    private void postComment(String id, String content, String targetId) {
        LiveManager.getInstance(this).postComment(id, content, "2", targetId, this);
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
                getReplayList();
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

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodePostComment:
                dismissLoadingDialog();
                showToastSafe("评论成功");
                if (commentInputBox != null) {
                    commentInputBox.loseFocus();
                    commentInputBox.setInputHint("评论...");
                    commentInputBox.resetToolBox();
                }
                refreshCommentList();
                break;
            case EventCodeReplyList:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    comments = gson.fromJson(jsonObject.getJSONArray("listSubComment").toString(), new TypeToken<List<ReviewComments>>() {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
