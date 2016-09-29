package com.urgoo.collect.adapter;

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
import com.urgoo.collect.model.Video;

import java.util.List;

/**
 * Created by bb on 2016/9/23.
 */
public class FollowVideoAdapter extends UltimateViewAdapter<FollowVideoAdapter.ViewHolder> {
    private Context context;
    private List<Video> videoList;
    private OnItemClickListener onItemClickListener;

    public FollowVideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    public Video getItem(int position) {
        return videoList.get(position);
    }

    public void addData(List<Video> videoList) {
        for (Video video : videoList) {
            insert(this.videoList, video, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(videoList);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return videoList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(FollowVideoAdapter.ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            Video video = getItem(position);
            holder.sdvLive.setImageURI(Uri.parse(video.getPic()));
            holder.tvTitle.setText(video.getPerson());
            holder.tvDes.setText(video.getTitle());
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
        public SimpleDraweeView sdvLive;
        public TextView tvTitle;
        public TextView tvDes;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvLive = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
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
