package com.urgoo.plan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;

/**
 * Created by bb on 2016/8/8.
 */
public class PlanFinishAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public PlanFinishAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_finish_plan, null);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        }

        return convertView;
    }
}
