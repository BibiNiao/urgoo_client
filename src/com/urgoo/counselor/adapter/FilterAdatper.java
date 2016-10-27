package com.urgoo.counselor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.client.R;

import java.util.List;
import java.util.Map;

/**
 * Created by bb on 2016/10/22.
 */
public class FilterAdatper extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Map<String, String>> listItems;

    public FilterAdatper(Context context, List<Map<String, String>> listItems) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public void addData(List<Map<String, String>> listItems) {
        this.listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_filter, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(listItems.get(position).get("name"));
        return convertView;
    }

    private static class ViewHolder {
        private TextView tvName;
    }
}
