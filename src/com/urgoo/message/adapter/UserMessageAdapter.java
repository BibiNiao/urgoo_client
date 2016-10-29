package com.urgoo.message.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMMessage;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.message.model.MessageItem;
import com.urgoo.message.model.UserMessage;

import java.util.List;

/**
 * Created by bb on 2016/10/26.
 */
public class UserMessageAdapter extends UltimateViewAdapter<UserMessageAdapter.ViewHolder> {
    private Context context;
    private List<UserMessage> userMessageList;
    private OnItemClickListener onItemClickListener;

    public UserMessageAdapter(Context context, List<UserMessage> userMessageList) {
        this.context = context;
        this.userMessageList = userMessageList;
    }

    public UserMessage getItem(int position) {
        return userMessageList.get(position);
    }

    public void addData(List<UserMessage> userMessageList) {
        for (UserMessage userMessage : userMessageList) {
            insert(this.userMessageList, userMessage, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(userMessageList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public UserMessageAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public UserMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return userMessageList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            UserMessage userMessage = getItem(position);
            holder.tvContent.setText(userMessage.getContent());
            holder.tvTitle.setText(userMessage.getTitle());
            holder.sdvIcon.setImageURI(Uri.parse(userMessage.getLogo()));
            if (userMessage.getUnread() == 1) {
                holder.ivUnread.setVisibility(View.GONE);
            } else {
                holder.ivUnread.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvIcon;
        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivUnread;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvIcon = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                ivUnread = (ImageView) itemView.findViewById(R.id.iv_unread);
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
