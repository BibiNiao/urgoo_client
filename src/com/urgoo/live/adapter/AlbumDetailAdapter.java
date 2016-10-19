package com.urgoo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.live.model.AlbumDetailList;

import java.util.List;

/**
 * Created by bb on 2016/10/12.
 */
public class AlbumDetailAdapter extends UltimateViewAdapter<AlbumDetailAdapter.ViewHolder> {
    private Context context;
    private List<AlbumDetailList> albumDetailLists;
    private OnItemClickListener onItemClickListener;

    public AlbumDetailAdapter(Context context, List<AlbumDetailList> albumDetailLists) {
        this.context = context;
        this.albumDetailLists = albumDetailLists;
    }

    public AlbumDetailList getItem(int position) {
        if (customHeaderView != null && position > 0) {
            position--;
        } else if (position < 0) {
            position = 0;
        }
        return albumDetailLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(List<AlbumDetailList> albumDetailLists) {
        this.albumDetailLists.addAll(albumDetailLists);
        notifyDataSetChanged();
    }

    public void clear() {
        clear(albumDetailLists);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_albumlist, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return albumDetailLists.size();
    }

    private boolean headerCheck(int position) {
        if ((customHeaderView != null ? position <= albumDetailLists.size() : position < albumDetailLists.size()) && (customHeaderView != null ? position > 0 : true)) {
            return true;
        }
        return false;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(AlbumDetailAdapter.ViewHolder holder, int position) {
        if (position < getItemCount() && headerCheck(position)) {
            AlbumDetailList albumDetail = getItem(position);
            holder.sdvAvatar.setImageURI(Uri.parse(albumDetail.getCoverSmall()));
            holder.tvDes.setText(albumDetail.getTitle());
            holder.tvTitle.setText(albumDetail.getEnName());
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
        public SimpleDraweeView sdvAvatar;
        public TextView tvTitle;
        public TextView tvDes;
        public TextView tvTime;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvAvatar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvDes = (TextView) itemView.findViewById(R.id.tv_des);
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
