package com.urgoo.counselor.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.model.CounselorDetail;
import com.urgoo.counselor.model.CounselorServiceList;
import com.urgoo.order.OrderActivity;
import com.zw.express.tool.Util;

import java.util.List;

/**
 * Created by bb on 2016/9/28.
 */
public class CounselorServerAdapter extends BaseAdapter {
    private Context mContext;
    private List<CounselorServiceList> counselorServiceList;
    private OnItemClickListener onItemClickListener;

    public CounselorServerAdapter(Context context, List<CounselorServiceList> counselorServiceList) {
        this.mContext = context;
        this.counselorServiceList = counselorServiceList;
    }

    public void addData(List<CounselorServiceList> counselorService) {
        this.counselorServiceList.addAll(counselorService);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return counselorServiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return counselorServiceList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_counselor_server, parent, false);
            viewHolder.tvService = (TextView) convertView.findViewById(R.id.tv_service);
            viewHolder.tvRmb = (TextView) convertView.findViewById(R.id.tv_rmb);
            viewHolder.btnDetail = (Button) convertView.findViewById(R.id.btn_detail);
            viewHolder.ivLine = (ImageView) convertView.findViewById(R.id.line);
            if (onItemClickListener != null) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, position);
                    }
                });
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.ivLine.setVisibility(View.GONE);
        } else {
            viewHolder.ivLine.setVisibility(View.VISIBLE);
        }
        viewHolder.tvService.setText(counselorServiceList.get(position).getServiceName());
        viewHolder.tvRmb.setText("总费用 RMB " + counselorServiceList.get(position).getServicePrice());
        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //   立即聘用的点击事件，可在此处执行操作
                onItemClickListener.onItemClick(v, position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvService;
        private TextView tvRmb;
        private Button btnDetail;
        private ImageView ivLine;
    }
}
