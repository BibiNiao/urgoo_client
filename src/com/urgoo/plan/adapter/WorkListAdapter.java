package com.urgoo.plan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import com.urgoo.plan.model.Plan;
import com.urgoo.service.activities.PlanContentActivity;
import com.urgoo.service.activities.ReportActivity;
import com.urgoo.service.activities.TaskContentActivity;
import com.zw.express.tool.Util;

import java.util.List;

/**
 * Created by bb on 2016/10/21.
 */
public class WorkListAdapter extends UltimateViewAdapter<WorkListAdapter.ViewHolder> {
    private Context context;
    private List<Plan> planList;
    private OnItemClickListener onItemClickListener;

    public WorkListAdapter(Context context, List<Plan> planList) {
        this.context = context;
        this.planList = planList;
    }

    public Plan getItem(int position) {
        return planList.get(position);
    }

    public void addData(List<Plan> planList) {
        for (Plan plan : planList) {
            insert(this.planList, plan, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(planList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public WorkListAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public WorkListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return planList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < getAdapterItemCount()) {
            final Plan plan = getItem(position);
            holder.sdvCricle.setBackgroundColor(Color.parseColor("#" + plan.getColor()));
            holder.sdvCricle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", String.valueOf(plan.getType()));
                    bundle.putString("title", plan.getDes());
                    Util.openActivityWithBundle(context, PlanContentActivity.class, bundle);
                }
            });

            holder.tvTitle.setText(plan.getDes());
            holder.tvComplete.setText(context.getString(R.string.plan_complete, plan.getDone()));
            holder.tvPlan.setText(context.getString(R.string.plan_doing, plan.getDoing()));
            holder.tvDes.setText(plan.getTitle());
            holder.tvTime.setText(context.getString(R.string.plan_time, plan.getDeadLine()));
            holder.tvDay.setText(context.getString(R.string.plan_day, plan.getDeadDay()));
            holder.vHorizontal.setBackgroundColor(Color.parseColor("#" + plan.getColor()));
            holder.vVertical.setBackgroundColor(Color.parseColor("#" + plan.getColor()));
            if (position == 0) {
                holder.llReport.setVisibility(View.VISIBLE);
            } else {
                holder.llReport.setVisibility(View.GONE);
            }
            holder.llDetial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", String.valueOf(plan.getTaskId()));
                    Util.openActivityWithBundle(context, TaskContentActivity.class, bundle);
                }
            });
            holder.llReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.openActivity(context, ReportActivity.class);
                }
            });
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
        public SimpleDraweeView sdvCricle;
        public TextView tvTitle;
        public TextView tvComplete;
        public TextView tvPlan;
        public TextView tvDes;
        public TextView tvTime;
        public TextView tvDay;
        public View vHorizontal;
        public View vVertical;
        public View llDetial;
        public View llReport;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvCricle = (SimpleDraweeView) itemView.findViewById(R.id.sdv_cricle);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvComplete = (TextView) itemView.findViewById(R.id.tv_complete);
                tvPlan = (TextView) itemView.findViewById(R.id.tv_plan);
                tvDes = (TextView) itemView.findViewById(R.id.tv_des);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvDay = (TextView) itemView.findViewById(R.id.tv_day);
                vHorizontal = itemView.findViewById(R.id.view_horizontal);
                vVertical = itemView.findViewById(R.id.view_vertical);
                llDetial = itemView.findViewById(R.id.ll_detail);
                llReport = itemView.findViewById(R.id.ll_report);
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