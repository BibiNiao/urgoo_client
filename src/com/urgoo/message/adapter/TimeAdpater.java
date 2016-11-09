package com.urgoo.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.urgoo.client.R;
import com.urgoo.message.biz.RobotOption;

import java.util.List;

/**
 * Created by bb on 2016/11/1.
 */
public class TimeAdpater extends RecyclerView.Adapter<TimeHolder> implements AbsListView.OnScrollListener {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    private List<RobotOption> robotOptions;
    private int lastPoition;

    public TimeAdpater(Context context, List<RobotOption> robotOptions) {
        this.mContext = context;
        this.robotOptions = robotOptions;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return robotOptions.size();
    }

    @Override
    public void onBindViewHolder(final TimeHolder holder, final int position) {
        // TODO Auto-generated method stub
        lastPoition = position;
        holder.button.setText(robotOptions.get(position).getText());
        if (robotOptions.get(position).isChecked()) {
            holder.button.setBackgroundResource(R.drawable.btn_enable_checked_bg);
        } else {
            holder.button.setBackgroundResource(R.drawable.btn_checked_bg);
        }
        if (mOnItemClickLitener != null) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.button, position);
                }
            });
        }
    }

    @Override
    public TimeHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        View view = mInflater.inflate(R.layout.chatting_time_item, arg0, false);
        TimeHolder viewHolder = new TimeHolder(view);
        return viewHolder;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 1) {

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

class TimeHolder extends RecyclerView.ViewHolder {
    Button button;

    public TimeHolder(View arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
        button = (Button) arg0.findViewById(R.id.button);
    }
}
