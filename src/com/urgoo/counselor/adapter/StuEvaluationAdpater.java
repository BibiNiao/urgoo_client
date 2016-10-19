package com.urgoo.counselor.adapter;

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
import com.urgoo.counselor.model.Evaluation;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bb on 2016/9/23.
 */
public class StuEvaluationAdpater extends UltimateViewAdapter<StuEvaluationAdpater.ViewHolder> {
    private Context context;
    private List<Evaluation> evaluationList;
    private OnItemClickListener onItemClickListener;

    public StuEvaluationAdpater(Context context, List<Evaluation> evaluationList) {
        this.context = context;
        this.evaluationList = evaluationList;
    }

    public Evaluation getItem(int position) {
        return evaluationList.get(position);
    }

    public void addData(List<Evaluation> evaluationList) {
        for (Evaluation evaluation : evaluationList) {
            insert(this.evaluationList, evaluation, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(evaluationList);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return evaluationList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(StuEvaluationAdpater.ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            Evaluation evaluation = getItem(position);
            holder.tvGrade.setText(evaluation.getGrade());
            holder.tvTitle.setText(evaluation.getName());
            holder.sdvAvatar.setImageURI(Uri.parse(evaluation.getIcon()));
            holder.tvContent.setText(evaluation.getWords());
            holder.tvSeries.setText(evaluation.getMajor());
            holder.tvTime.setText(evaluation.getDate());
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
        public TextView tvContent;
        public TextView tvTime;
        public TextView tvGrade;
        public TextView tvSeries;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvAvatar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvGrade = (TextView) itemView.findViewById(R.id.tv_grade);
                tvSeries = (TextView) itemView.findViewById(R.id.tv_series);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
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
