package com.urgoo.profile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.profile.model.InformationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/9/8.
 */
public class MessageListAdapter extends UltimateViewAdapter<MessageListAdapter.ViewHolder> {
    private Context context;
    private List<InformationEntity> informationEntities = new ArrayList<>();
    private int type;

    public MessageListAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public void addData(List<InformationEntity> informationEntities) {
        this.informationEntities.addAll(informationEntities);
        notifyDataSetChanged();
    }

    public void clear() {
        informationEntities.clear();
        notifyDataSetChanged();
    }

    public InformationEntity getItem(int position) {
        return informationEntities.get(position);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return informationEntities.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            InformationEntity informationEntity = getItem(position);
            if (informationEntity.getUnread() == 2) {
                holder.ivRed.setVisibility(View.VISIBLE);
            } else {
                holder.ivRed.setVisibility(View.GONE);
            }
            if (type == 1) {
                holder.tvTitle.setText(informationEntity.getTitle());
            } else {
                holder.tvTitle.setText(informationEntity.getTypeName());
            }
            holder.tvTime.setText(informationEntity.getInsertDatetime());
            holder.tvCountent.setText(informationEntity.getContent());
        }
    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        TextView tvTitle;
        TextView tvCountent;
        TextView tvTime;
        ImageView ivRed;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvCountent = (TextView) itemView.findViewById(R.id.tv_countent);
                ivRed = (ImageView) itemView.findViewById(R.id.iv_red);
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
