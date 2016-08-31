package com.urgoo.plan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.client.R;

/**
 * Created by bb on 2016/8/8.
 */
public class PlanNostartAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    public PlanNostartAdapter(Context context) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nostart_plan, null);
        }
//        TextView txt = (TextView) convertView.findViewById(R.id.text);
//        txt.setText("第" + getItem(position) + "个item");
        return convertView;
    }
}
