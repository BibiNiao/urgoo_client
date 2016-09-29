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
public class SettingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private static List<SettingItem> items = new ArrayList<>();

    public SettingAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        if (items.isEmpty()) {
            items.add(new SettingItem(mContext.getString(R.string.setting_aboutus), R.drawable.ic_setting_aboutus));
            items.add(new SettingItem(mContext.getString(R.string.setting_logout), R.drawable.ic_setting_logout));
        }
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
            holder.ivNext = (ImageView) convertView.findViewById(R.id.iv_next);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SettingItem item = getItem(position);
        if (position == 1) {
            holder.ivNext.setVisibility(View.GONE);
        } else {
            holder.ivNext.setVisibility(View.VISIBLE);
        }
        holder.tvTitle.setText(item.title);
        holder.imageView.setBackgroundResource(item.image);
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        ImageView ivNext;
    }

    class SettingItem {
        String title;
        Integer image;

        public SettingItem(String title, Integer image) {
            this.title = title;
            this.image = image;
        }
    }
}
