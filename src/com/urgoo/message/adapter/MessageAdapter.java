package com.urgoo.message.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.message.model.MessageItem;

import java.util.List;

/**
 * Created by bb on 2016/10/26.
 */
public class MessageAdapter extends UltimateViewAdapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<MessageItem> messageItems;
    private OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, List<MessageItem> messageItems) {
        this.context = context;
        this.messageItems = messageItems;
    }

    public MessageItem getItem(int position) {
        return messageItems.get(position);
    }

    public void addData(List<MessageItem> messageItems) {
        for (MessageItem messageItem : messageItems) {
            insert(this.messageItems, messageItem, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(messageItems);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MessageAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return messageItems.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < getAdapterItemCount()) {
            MessageItem messageItem = getItem(position);
            holder.tvContent.setText(messageItem.getContent());
            holder.tvTitle.setText(messageItem.getTypeName());
            holder.sdvIcon.setImageURI(Uri.parse(messageItem.getIconUrl()));
            if (messageItem.getUnreadCount().equals("0")) {
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
