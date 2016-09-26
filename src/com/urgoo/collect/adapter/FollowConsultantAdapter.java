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
import com.urgoo.collect.model.CounselorEntiy;

import java.util.List;

/**
 * Created by bb on 2016/9/23.
 */
public class FollowConsultantAdapter extends UltimateViewAdapter<FollowConsultantAdapter.ViewHolder> {
    private Context context;
    private List<CounselorEntiy> counselorEntiys;
    private OnItemClickListener onItemClickListener;

    public FollowConsultantAdapter(Context context, List<CounselorEntiy> counselorEntiys) {
        this.context = context;
        this.counselorEntiys = counselorEntiys;
    }

    public CounselorEntiy getItem(int position) {
        return counselorEntiys.get(position);
    }

    public void addData(List<CounselorEntiy> counselorEntiys) {
        for (CounselorEntiy counselorEntiy : counselorEntiys) {
            insert(this.counselorEntiys, counselorEntiy, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(counselorEntiys);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return counselorEntiys.size();
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
    public void onBindViewHolder(FollowConsultantAdapter.ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            CounselorEntiy counselorEntiy = getItem(position);
            holder.sdvLive.setImageURI(Uri.parse(counselorEntiy.getUserIcon()));
            holder.tvTitle.setText(counselorEntiy.getCnName());
            holder.tvRegion.setText(counselorEntiy.getHabitualResidence());
        }
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvLive;
        public TextView tvTitle;
        public TextView tvRegion;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvLive = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
                tvRegion = (TextView) itemView.findViewById(R.id.tv_region);
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
