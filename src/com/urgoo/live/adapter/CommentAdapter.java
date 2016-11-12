package com.urgoo.live.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.live.activities.CommentListActivity;
import com.urgoo.live.activities.VideoDetailActivity;
import com.urgoo.live.biz.LiveManager;
import com.urgoo.live.model.Comment;
import com.urgoo.live.view.CommentInputBox;
import com.urgoo.live.view.CommentInputToolBoxListener;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class CommentAdapter extends UltimateViewAdapter<CommentAdapter.ViewHolder> implements View.OnClickListener {

    private List<Comment> comments;
    private VideoDetailActivity context;
    private OnItemClickListener onItemClickListener;
    private int toReplyPosition;
    private CommentInputBox commentToolBox;
    private String videoId;

    public CommentAdapter(VideoDetailActivity context, List<Comment> comments, String videoId) {
        this.context = context;
        this.comments = comments;
        this.videoId = videoId;
    }

    public Comment getItem(int position) {
        if (customHeaderView != null && position > 0) {
            position--;
        } else if (position < 0) {
            position = 0;
        }
        return comments.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;

    }

    @Override
    public int getAdapterItemCount() {
        return comments.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    private boolean headerCheck(int position) {
        if ((customHeaderView != null ? position <= comments.size() : position < comments.size()) && (customHeaderView != null ? position > 0 : true)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getItemCount() && headerCheck(position) && !comments.isEmpty()) {
            Comment comment = getItem(position);
            if (position == 1) {
                holder.tv.setVisibility(View.VISIBLE);
            } else {
                holder.tv.setVisibility(View.GONE);
            }
            holder.sdvavatar.setImageURI(Uri.parse(comment.getUserIcon()));
            holder.tvDate.setText(comment.getInsertDatetime());
            holder.tvNickname.setText(comment.getNickName());
            holder.tvCountent.setText(comment.getContent());
            if (comment.getShowDel().equals("0")) {
                holder.tvDel.setVisibility(View.GONE);
            } else {
                holder.tvDel.setVisibility(View.VISIBLE);
            }
            holder.tvDel.setTag(position);
            holder.tvDel.setOnClickListener(this);
            holder.tvCount.setText(String.valueOf(comment.getReplySize()));
            holder.tvCount.setTag(position);
            holder.tvCount.setOnClickListener(this);
            if (comment.getReplySize() > 3) {
                holder.tvAll.setVisibility(View.VISIBLE);
            } else {
                holder.tvAll.setVisibility(View.GONE);
            }
            holder.tvAll.setTag(position);
            holder.tvAll.setOnClickListener(this);
            if (comment.getReplySize() > 0) {
                holder.llReview.setVisibility(View.VISIBLE);
                if (comment.getReplySize() > 2) {
                    holder.rlComment1.setVisibility(View.VISIBLE);
                    holder.rlComment2.setVisibility(View.VISIBLE);
                    holder.rlComment3.setVisibility(View.VISIBLE);
                    holder.sdvIcon1.setImageURI(Uri.parse(comment.getListSubComment().get(0).getUserIcon()));
                    holder.sdvIcon2.setImageURI(Uri.parse(comment.getListSubComment().get(1).getUserIcon()));
                    holder.sdvIcon3.setImageURI(Uri.parse(comment.getListSubComment().get(2).getUserIcon()));
                    holder.tvNickName1.setText(comment.getListSubComment().get(0).getNickName());
                    holder.tvNickName2.setText(comment.getListSubComment().get(1).getNickName());
                    holder.tvNickName3.setText(comment.getListSubComment().get(2).getNickName());
                    holder.tvComment1.setText(comment.getListSubComment().get(0).getContent());
                    holder.tvComment2.setText(comment.getListSubComment().get(1).getContent());
                    holder.tvComment3.setText(comment.getListSubComment().get(2).getContent());
                    if (comment.getListSubComment().get(0).getShowDel().equals("1")) {
                        holder.tvDel1.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvDel1.setVisibility(View.GONE);
                    }
                    if (comment.getListSubComment().get(1).getShowDel().equals("1")) {
                        holder.tvDel2.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvDel2.setVisibility(View.GONE);
                    }
                    if (comment.getListSubComment().get(2).getShowDel().equals("1")) {
                        holder.tvDel3.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvDel3.setVisibility(View.GONE);
                    }
                }
                switch (comment.getReplySize()) {
                    case 1:
                        if (comment.getListSubComment().get(0).getShowDel().equals("1")) {
                            holder.tvDel1.setVisibility(View.VISIBLE);
                        } else {
                            holder.tvDel1.setVisibility(View.GONE);
                        }
                        holder.rlComment1.setVisibility(View.VISIBLE);
                        holder.rlComment2.setVisibility(View.GONE);
                        holder.rlComment3.setVisibility(View.GONE);
                        holder.sdvIcon1.setImageURI(Uri.parse(comment.getListSubComment().get(0).getUserIcon()));
                        holder.tvNickName1.setText(comment.getListSubComment().get(0).getNickName());
                        holder.tvComment1.setText(comment.getListSubComment().get(0).getContent());
                        break;
                    case 2:
                        if (comment.getListSubComment().get(0).getShowDel().equals("1")) {
                            holder.tvDel1.setVisibility(View.VISIBLE);
                        } else {
                            holder.tvDel1.setVisibility(View.GONE);
                        }
                        if (comment.getListSubComment().get(1).getShowDel().equals("1")) {
                            holder.tvDel2.setVisibility(View.VISIBLE);
                        } else {
                            holder.tvDel2.setVisibility(View.GONE);
                        }
                        holder.rlComment1.setVisibility(View.VISIBLE);
                        holder.rlComment2.setVisibility(View.VISIBLE);
                        holder.rlComment3.setVisibility(View.GONE);
                        holder.sdvIcon1.setImageURI(Uri.parse(comment.getListSubComment().get(0).getUserIcon()));
                        holder.sdvIcon2.setImageURI(Uri.parse(comment.getListSubComment().get(1).getUserIcon()));
                        holder.tvNickName1.setText(comment.getListSubComment().get(0).getNickName());
                        holder.tvNickName2.setText(comment.getListSubComment().get(1).getNickName());
                        holder.tvComment1.setText(comment.getListSubComment().get(0).getContent());
                        holder.tvComment2.setText(comment.getListSubComment().get(1).getContent());
                        break;
                }
            } else {
                holder.llReview.setVisibility(View.GONE);
            }
            holder.tvDel1.setTag(position);
            holder.tvDel1.setOnClickListener(this);
            holder.tvDel2.setTag(position);
            holder.tvDel2.setOnClickListener(this);
            holder.tvDel3.setTag(position);
            holder.tvDel3.setOnClickListener(this);
        }
    }

    public void addData(List<Comment> comments) {
        for (Comment comment : comments) {
            insert(this.comments, comment, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(comments);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setCommentToolBox(CommentInputBox commentToolBox) {
        this.commentToolBox = commentToolBox;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        final Comment comment = getItem(position);
        switch (v.getId()) {
            case R.id.tv_all:
                Bundle bundle = new Bundle();
                bundle.putString(CommentListActivity.EXTRA_COMMENT_ID, String.valueOf(comment.getCommentId()));
                bundle.putString(CommentListActivity.EXTRA_VIDEO_ID, videoId);
                bundle.putString(CommentListActivity.EXTRA_FLAG, "2");
                Util.openActivityWithBundle(context, CommentListActivity.class, bundle);
                break;
            case R.id.tv_count:
                toReplyPosition = (Integer) v.getTag();
                commentToolBox.setReplyTarget(String.valueOf(comment.getCommentId()));
                commentToolBox.setInputHint("回复" + comment.getNickName() + ":");
                commentToolBox.showReplyInputToolBox();
                commentToolBox.setCommentInputToolBoxListener(new CommentInputToolBoxListener() {
                    @Override
                    public void onSend(CharSequence inputMsg) {
                        context.showLoadingDialog();
                        context.postComment(videoId, inputMsg.toString(), commentToolBox.getReplyTarget());
//                        LiveManager.getInstance(context).postComment(videoId, inputMsg.toString(), "2", String.valueOf(comment.getCommentId()), new StringRequestCallBack() {
//                            @Override
//                            public void onSuccess(EventCode eventCode, String response) {
//                                context.dismissLoadingDialog();
//                                context.showToastSafe("评论成功");
//                                if (commentToolBox != null) {
//                                    commentToolBox.loseFocus();
//                                    commentToolBox.setInputHint("评论...");
//                                    commentToolBox.resetToolBox();
//                                }
//                                context.refreshCommentList();
//                            }
//
//                            @Override
//                            public void onFailure(EventCode eventCode, Call call) {
//                                context.dismissLoadingDialog();
//                                context.showToastSafe("评论失败");
//                            }
//                        });
                    }
                });
                break;
            case R.id.tv_del:
                onDelete(v, String.valueOf(comment.getCommentId()));
                break;
            case R.id.tv_del1:
                onDelete(v, String.valueOf(comment.getListSubComment().get(0).getCommentId()), 0);
                break;
            case R.id.tv_del2:
                onDelete(v, String.valueOf(comment.getListSubComment().get(1).getCommentId()), 1);
                break;
            case R.id.tv_del3:
                onDelete(v, String.valueOf(comment.getListSubComment().get(2).getCommentId()), 2);
                break;
        }
    }

    /**
     * 删除回复
     *
     * @param v
     * @param targetId
     */
    private void onDelete(View v, final String targetId, final int flag) {
        final int position = (Integer) v.getTag();
        new AlertDialog.Builder(context).setTitle("提示").setMessage("确定删除吗？").
                setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.showLoadingDialog();
                        LiveManager.getInstance(context).delComment(targetId, new StringRequestCallBack() {

                                    @Override
                                    public void onSuccess(EventCode eventCode, String response) {
                                        context.dismissLoadingDialog();
                                        try {
                                            JSONObject jsonObj = new JSONObject(response);
                                            JSONObject jsonobject = new JSONObject(jsonObj.getString("header"));
                                            if (jsonobject.getString("code").equals("200")) {
                                                context.showToastSafe("删除成功");
                                                remove(position, flag);
                                            } else {
                                                context.showToastSafe(jsonobject.getString("message"));
                                            }
                                        } catch (JSONException e) {
                                            context.showToastSafe("服务器异常");
                                        }
                                    }

                                    @Override
                                    public void onFailure(EventCode eventCode, Call call) {
                                        context.dismissLoadingDialog();
                                    }
                                }
                        );
                    }
                }

        ).show();
    }

    /**
     * 删除评论
     *
     * @param v
     * @param targetId
     */
    private void onDelete(View v, final String targetId) {
        final int position = (Integer) v.getTag();
        new AlertDialog.Builder(context).setTitle("提示").setMessage("确定删除吗？").
                setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.showLoadingDialog();
                        LiveManager.getInstance(context).delComment(targetId, new StringRequestCallBack() {

                                    @Override
                                    public void onSuccess(EventCode eventCode, String response) {
                                        context.dismissLoadingDialog();
                                        try {
                                            JSONObject jsonObj = new JSONObject(response);
                                            JSONObject jsonobject = new JSONObject(jsonObj.getString("header"));
                                            if (jsonobject.getString("code").equals("200")) {
                                                context.showToastSafe("删除成功");
                                                remove(position);
                                            } else {
                                                context.showToastSafe(jsonobject.getString("message"));
                                            }
                                        } catch (JSONException e) {
                                            context.showToastSafe("服务器异常");
                                        }
                                    }

                                    @Override
                                    public void onFailure(EventCode eventCode, Call call) {
                                        context.dismissLoadingDialog();
                                    }
                                }
                        );
                    }
                }

        ).show();
    }

    public void remove(int position, int flag) {
        if (customHeaderView != null) {
            position--;
            comments.get(position).getListSubComment().remove(flag);
            comments.get(position).setReplySize(comments.get(position).getReplySize() - 1);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (customHeaderView != null) {
            position--;
            comments.remove(position);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvavatar;
        public TextView tvNickname;
        public TextView tvDate;
        public TextView tvCountent;
        public TextView tvCount;
        public TextView tvAll;
        public TextView tvDel;
        public LinearLayout llReview;
        public SimpleDraweeView sdvIcon1;
        public SimpleDraweeView sdvIcon2;
        public SimpleDraweeView sdvIcon3;
        public TextView tvComment1;
        public TextView tvComment2;
        public TextView tvComment3;
        public TextView tvNickName1;
        public TextView tvNickName2;
        public TextView tvNickName3;
        public TextView tvDel1;
        public TextView tvDel2;
        public TextView tvDel3;
        public RelativeLayout rlComment1;
        public RelativeLayout rlComment2;
        public RelativeLayout rlComment3;
        public TextView tv;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvavatar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
                tvDate = (TextView) itemView.findViewById(R.id.tv_date);
                tvCountent = (TextView) itemView.findViewById(R.id.tv_countent);
                tvCount = (TextView) itemView.findViewById(R.id.tv_count);
                llReview = (LinearLayout) itemView.findViewById(R.id.ll_review);
                tvAll = (TextView) itemView.findViewById(R.id.tv_all);
                tvDel = (TextView) itemView.findViewById(R.id.tv_del);
                sdvIcon1 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_icon1);
                sdvIcon2 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_icon2);
                sdvIcon3 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_icon3);
                tvComment1 = (TextView) itemView.findViewById(R.id.tv_comment1);
                tvComment2 = (TextView) itemView.findViewById(R.id.tv_comment2);
                tvComment3 = (TextView) itemView.findViewById(R.id.tv_comment3);
                tvNickName1 = (TextView) itemView.findViewById(R.id.tv_nickname1);
                tvNickName2 = (TextView) itemView.findViewById(R.id.tv_nickname2);
                tvNickName3 = (TextView) itemView.findViewById(R.id.tv_nickname3);
                tvDel1 = (TextView) itemView.findViewById(R.id.tv_del1);
                tvDel2 = (TextView) itemView.findViewById(R.id.tv_del2);
                tvDel3 = (TextView) itemView.findViewById(R.id.tv_del3);
                rlComment1 = (RelativeLayout) itemView.findViewById(R.id.rl_comment1);
                rlComment2 = (RelativeLayout) itemView.findViewById(R.id.rl_comment2);
                rlComment3 = (RelativeLayout) itemView.findViewById(R.id.rl_comment3);
                tv = (TextView) itemView.findViewById(R.id.tv);
                if (onItemClickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(v, getAdapterPosition());
                        }
                    });
                }
            }
        }
    }
}
