package com.urgoo.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zfz
 */
public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private static List<SettingItem> items = new ArrayList<>();

    public MyAdapter(Context context, int systemCount, int individualCount, int advanceCount) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        if (items.isEmpty()) {
            items.add(new SettingItem(mContext.getString(R.string.my_message), R.drawable.ic_my_message, individualCount));
            items.add(new SettingItem(mContext.getString(R.string.my_advance), R.drawable.ic_my_advance, advanceCount));
            items.add(new SettingItem(mContext.getString(R.string.my_order), R.drawable.ic_my_order, 0));
            items.add(new SettingItem(mContext.getString(R.string.my_sys_message), R.drawable.ic_my_sys_message, systemCount));
//            items.add(new SettingItem(mContext.getString(R.string.my_activities), R.drawable.ic_my_activity, 0));
            items.add(new SettingItem(mContext.getString(R.string.my_share), R.drawable.ic_my_share, 0));
            items.add(new SettingItem(mContext.getString(R.string.my_tel), R.drawable.ic_my_tel, 0));
            items.add(new SettingItem(mContext.getString(R.string.my_setting), R.drawable.ic_my_setting, 0));
        }
    }

    public void setCount(int systemCount, int individualCount, int advanceCount) {
        items.get(0).count = individualCount;
        items.get(1).count = advanceCount;
        items.get(3).count = systemCount;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public SettingItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.my_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv);
            holder.ivRed = (ImageView) convertView.findViewById(R.id.iv_unread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SettingItem item = getItem(position);
        holder.tvTitle.setText(item.title);
        holder.imageView.setBackgroundResource(item.image);
        if (item.count > 0) {
            holder.ivRed.setVisibility(View.VISIBLE);
        } else {
            holder.ivRed.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        ImageView ivRed;
    }

    class SettingItem {
        String title;
        Integer image;
        int count;

        public SettingItem(String title, Integer image, int count) {
            this.title = title;
            this.image = image;
            this.count = count;
        }
    }
}
