package com.urgoo.live.adapter;

import android.app.AlertDialog;
import android.content.Context;
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
import com.urgoo.live.model.ReviewComments;
import com.urgoo.live.view.CommentInputBox;
import com.urgoo.live.view.CommentInputToolBoxListener;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class CommentListAdapter extends UltimateViewAdapter<CommentListAdapter.ViewHolder> implements View.OnClickListener {

    private List<ReviewComments> comments;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CommentListAdapter(Context context, List<ReviewComments> comments) {
        this.context = context;
        this.comments = comments;
    }

    public ReviewComments getItem(int position) {
        return comments.get(position);
    }

    public void addData(List<ReviewComments> comments) {
        for (ReviewComments comment : comments) {
            insert(this.comments, comment, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(comments);
        notifyDataSetChanged();
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            ReviewComments comment = getItem(position);
            holder.sdvavatar.setImageURI(Uri.parse(comment.getUserIcon()));
            holder.tvNickname.setText(comment.getNickName());
            holder.tvCountent.setText(comment.getContent());
            if (comment.getShowDel().equals("1")) {
                holder.tvDel.setVisibility(View.VISIBLE);
            } else {
                holder.tvDel.setVisibility(View.GONE);
            }
            holder.tvDel.setTag(position);
            holder.tvDel.setOnClickListener(this);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
                        LiveManager.getInstance(context).delComment(targetId, new StringRequestCallBack() {

                                    @Override
                                    public void onSuccess(EventCode eventCode, String response) {
                                        try {
                                            JSONObject jsonObj = new JSONObject(response);
                                            JSONObject jsonobject = new JSONObject(jsonObj.getString("header"));
                                            if (jsonobject.getString("code").equals("200")) {
                                                Util.shortToast(context, "删除成功");
                                                comments.remove(position);
                                                notifyDataSetChanged();
                                            } else {
                                                Util.shortToast(context, jsonobject.getString("message"));
                                            }
                                        } catch (JSONException e) {
                                            Util.shortToast(context, "服务器异常");
                                        }
                                    }

                                    @Override
                                    public void onFailure(EventCode eventCode, Call call) {
                                        Util.shortToast(context, "操作失败");
                                    }
                                }
                        );
                    }
                }

        ).show();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        final ReviewComments comment = getItem(position);
        switch (v.getId()) {
            case R.id.tv_del:
                onDelete(v, String.valueOf(comment.getCommentId()));
                break;
        }
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvavatar;
        public TextView tvNickname;
        public TextView tvCountent;
        public TextView tvDel;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvavatar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
                tvCountent = (TextView) itemView.findViewById(R.id.tv_countent);
                tvDel = (TextView) itemView.findViewById(R.id.tv_del);
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
