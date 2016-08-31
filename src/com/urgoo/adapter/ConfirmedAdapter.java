package com.urgoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.domain.confirmedmyorderBean;
import com.urgoo.schedule.activites.PrecontractMyOrderContent;
import com.urgoo.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/6/15.
 */
public class ConfirmedAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<confirmedmyorderBean> mList;
    private Context mContext;

    public ConfirmedAdapter(Context context, ArrayList<confirmedmyorderBean> list) {
        layoutInflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
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
            convertView = layoutInflater.inflate(R.layout.mine_myorder_confirmed_item, parent, false);
            viewHolder.img_myorder_tobo_icon = (CircleImageView) convertView.findViewById(R.id.img_myorder_tobo_icon);
            viewHolder.tv_myorder_tobo_user = (TextView) convertView.findViewById(R.id.tv_myorder_tobo_user);
            viewHolder.tv_myorder_tobe_time = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time);
            viewHolder.tv_myorder_tobe_time_USA = (TextView) convertView.findViewById(R.id.tv_myorder_tobe_time_USA);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PrecontractMyOrderContent.class);
                intent.putExtra("status", "2");
                if (mList.get(position).getAdvanceId() != null) {
                    intent.putExtra("advanceId", mList.get(position).getAdvanceId() + "");
                    mContext.startActivity(intent);
                } else
                    Toast.makeText(mContext, "请确定信息是否完整", Toast.LENGTH_SHORT).show();
            }
        });
        if (mList.get(position).getEnName() != null) {
            viewHolder.tv_myorder_tobo_user.setText(mList.get(position).getEnName());
        }
        viewHolder.tv_myorder_tobo_user.setText(mList.get(position).getCnName());
        viewHolder.tv_myorder_tobe_time.setText(mList.get(position).getCnStartTime() + "-" + mList.get(position).getCnEndTime() + "  " + mList.get(position).getAdvanceDate());
        viewHolder.tv_myorder_tobe_time_USA.setText("(美国东部时间 " + mList.get(position).getOtherStartTime() + "-" + mList.get(position).getOtherEndTime() + " )");

        if (mList.get(position).getUserIcon() != null)
            new imageLoadBusiness().imageLoadByURL(mList.get(position).getUserIcon(), viewHolder.img_myorder_tobo_icon);
        return convertView;
    }


    private static class ViewHolder {
        private TextView tv_myorder_tobo_user;
        private TextView tv_myorder_tobe_time;
        private TextView tv_myorder_tobe_time_USA;
        private CircleImageView img_myorder_tobo_icon;
    }
}
