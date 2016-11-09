package com.urgoo.account.adapter;

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
import com.urgoo.common.DataUtil;
import com.urgoo.live.model.Live;

import java.util.List;

/**
 * Created by bb on 2016/9/22.
 */
public class MyActivitesAdapter extends UltimateViewAdapter<MyActivitesAdapter.ViewHolder> {
    private Context context;
    private List<Live> lives;
    private OnItemClickListener onItemClickListener;

    public MyActivitesAdapter(Context context, List<Live> lives) {
        this.context = context;
        this.lives = lives;
    }

    public Live getItem(int position) {
        return lives.get(position);
    }

    public void addData(List<Live> lives) {
        for (Live live : lives) {
            insert(this.lives, live, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(lives);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyActivitesAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public MyActivitesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return lives.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            Live live = getItem(position);
            holder.sdvLive.setImageURI(Uri.parse(live.getMasterPic()));
            holder.tvTitle.setText(live.getTitle());
            holder.tvDes.setText(live.getDes());
            if (live.getStatus().equals("1")) {
                holder.tvTime.setBackgroundResource(R.drawable.bg_live_time_black);
                holder.tvTime.setTextColor(context.getResources().getColor(R.color.tvf75657));
                if (live.getBalanceTime() > 0) {
                    holder.tvTime.setText(DataUtil.formatDuring2(Integer.parseInt(live.getBalanceTime() + "")) + "后开始");
                } else {
                    holder.tvTime.setText("正在直播");
                }
            } else {
                holder.tvTime.setBackgroundResource(R.drawable.bg_live_time_green);
                holder.tvTime.setTextColor(context.getResources().getColor(R.color.tab_color));
                holder.tvTime.setText("往期回顾");
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
        public SimpleDraweeView sdvLive;
        public TextView tvTitle;
        public TextView tvDes;
        public TextView tvTime;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvLive = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
                tvDes = (TextView) itemView.findViewById(R.id.tv_des);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
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
