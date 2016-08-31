package com.urgoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.domain.ServiceList;
import com.zw.express.tool.Util;

import java.util.List;

/**
 * Created by bb on 2016/7/18.
 */
public class ServiceListDropDownAdapter extends BaseAdapter {
    private Context context;
    private List<ServiceList> list;
    private int checkItemPosition = 0;

    public ServiceListDropDownAdapter(Context context, List<ServiceList> list, int serviceId) {
        this.context = context;
        this.list = list;
        checkItemPosition = serviceId;
    }

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drop_down, null);
            holder = new ViewHolder();
            holder.tvChoose = (TextView) convertView.findViewById(R.id.text);
            holder.ivChoose = (ImageView) convertView.findViewById(R.id.img_choose);
            convertView.setTag(holder);
        }

        holder.tvChoose.setText(list.get(position).getServiceName());
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                holder.tvChoose.setTextColor(context.getResources().getColor(R.color.tv0fb7a3));
                holder.ivChoose.setVisibility(View.VISIBLE);
            } else {
                holder.tvChoose.setTextColor(context.getResources().getColor(R.color.tv666666));
                holder.ivChoose.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvChoose;
        ImageView ivChoose;
    }

}
