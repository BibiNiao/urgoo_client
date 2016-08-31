package com.urgoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.domain.tobeconfirmedmyorderBean;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;
import com.urgoo.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/6/15.
 */
public class TobeconfirAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<tobeconfirmedmyorderBean> mList;
    private Context mContext;
    private boolean figs = true;

    public TobeconfirAdapter(Context context, ArrayList<tobeconfirmedmyorderBean> list, boolean mFigs) {
        layoutInflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
        figs = mFigs;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.mine_myorder_tobe_item, parent, false);
            viewHolder.img_myorder_tobo_icon = (CircleImageView) convertView.findViewById(R.id.img_myorder_tobo_icon);
            viewHolder.tv_myorder_tobo_user = (TextView) convertView.findViewById(R.id.tv_myorder_tobo_user);
            viewHolder.tv_myorder_tobe_time = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time);
            viewHolder.tv_myorder_tobe_time_USA = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time_USA);
            viewHolder.tv_myorder_tobe_time2 = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time2);
            viewHolder.tv_myorder_tobe_time_USA2 = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time_USA2);
            viewHolder.tv_myorder_tobe_time3 = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time3);
            viewHolder.tv_myorder_tobe_time_USA3 = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time_USA3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tobe_adapter", "点击的当前的ID ： " + mList.get(position).getAdvanceId());
                Intent intent = new Intent(mContext, PrecontractMyOrderContent.class);
                if (figs) {
                    intent.putExtra("status", "1");
                } else {
                    intent.putExtra("status", "3");
                }
                intent.putExtra("advanceId", mList.get(position).getAdvanceId() + "");
                intent.putExtra("type", mList.get(position).getType() + "");
                mContext.startActivity(intent);
//                mContext.getResources();
            }
        });
        if (mList.get(position).getEnName() != null) {
            viewHolder.tv_myorder_tobo_user.setText(mList.get(position).getEnName());
        }

        if (mList.get(position).getAdvanceDetailList() != null) {
            switch (mList.get(position).getAdvanceDetailList().size()) {
                case 0:
                    viewHolder.tv_myorder_tobe_time.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time2.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA2.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time3.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA3.setVisibility(View.GONE);
                    break;
                case 1:
                    dataone(position, viewHolder);
                    viewHolder.tv_myorder_tobe_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time2.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA2.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time3.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA3.setVisibility(View.GONE);
                    break;
                case 2:
                    dataone(position, viewHolder);
                    datatow(position, viewHolder);
                    viewHolder.tv_myorder_tobe_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time2.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA2.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time3.setVisibility(View.GONE);
                    viewHolder.tv_myorder_tobe_time_USA3.setVisibility(View.GONE);
                    break;
                case 3:
                    viewHolder.tv_myorder_tobe_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time2.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA2.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time3.setVisibility(View.VISIBLE);
                    viewHolder.tv_myorder_tobe_time_USA3.setVisibility(View.VISIBLE);
                    dataone(position, viewHolder);
                    datatow(position, viewHolder);
                    datathree(position, viewHolder);
                    break;
            }
        }
        if (mList.get(position).getUserIcon() != null)
            new imageLoadBusiness().imageLoadByURL(mList.get(position).getUserIcon(), viewHolder.img_myorder_tobo_icon);
        return convertView;
    }

    private void datathree(int position, ViewHolder mViewHolder) {
        mViewHolder.tv_myorder_tobe_time3.setText(mList.get(position).getAdvanceDetailList().get(2).getCnStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(2).getCnEndTime() + "  " + mList.get(position).getAdvanceDetailList().get(2).getAdvanceDate());
        mViewHolder.tv_myorder_tobe_time_USA3.setText("(美国东部时间 " + mList.get(position).getAdvanceDetailList().get(2).getOtherStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(2).getOtherEndTime() + " )");
    }

    private void datatow(int position, ViewHolder mViewHolder) {
        mViewHolder.tv_myorder_tobe_time2.setText(mList.get(position).getAdvanceDetailList().get(1).getCnStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(1).getCnEndTime() + "  " + mList.get(position).getAdvanceDetailList().get(1).getAdvanceDate());
        mViewHolder.tv_myorder_tobe_time_USA2.setText("(美国东部时间 " + mList.get(position).getAdvanceDetailList().get(1).getOtherStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(1).getOtherEndTime() + " )");
    }

    private void dataone(int position, ViewHolder mViewHolder) {
        mViewHolder.tv_myorder_tobe_time.setText(mList.get(position).getAdvanceDetailList().get(0).getCnStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(0).getCnEndTime() + "  " + mList.get(position).getAdvanceDetailList().get(0).getAdvanceDate());
        mViewHolder.tv_myorder_tobe_time_USA.setText("(美国东部时间 " + mList.get(position).getAdvanceDetailList().get(0).getOtherStartTime() + "-" + mList.get(position).getAdvanceDetailList().get(0).getOtherEndTime() + " )");
    }

    private static class ViewHolder {
        private TextView tv_myorder_tobo_user;
        private TextView tv_myorder_tobe_time;
        private TextView tv_myorder_tobe_time_USA;
        private TextView tv_myorder_tobe_time2;
        private TextView tv_myorder_tobe_time_USA2;
        private TextView tv_myorder_tobe_time3;
        private TextView tv_myorder_tobe_time_USA3;
        private CircleImageView img_myorder_tobo_icon;
    }
}
