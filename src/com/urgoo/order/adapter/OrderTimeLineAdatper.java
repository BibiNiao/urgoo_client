package com.urgoo.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.order.model.PayTimeListEntity;

import java.util.List;

/**
 * Created by bb on 2016/8/12.
 */
public class OrderTimeLineAdatper extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PayTimeListEntity> payTimeList;

    public OrderTimeLineAdatper(Context context, List<PayTimeListEntity> payTimeList) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.payTimeList = payTimeList;
    }

    @Override
    public int getCount() {
        return payTimeList.size();
    }

    @Override
    public Object getItem(int position) {
        return payTimeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_ordertimeline, parent,false);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(payTimeList.get(position).getPayTime() + payTimeList.get(position).getDescb());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_content;
    }
}
