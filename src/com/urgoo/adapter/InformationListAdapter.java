package com.urgoo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.domain.InformationBean;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/3/29.
 */
public class InformationListAdapter extends BaseAdapter {

    private ArrayList<InformationBean> mList;
    private LayoutInflater mLayoutInflater;

    public InformationListAdapter(ArrayList<InformationBean> mList, Context context) {
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.information_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title);
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.time);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleTv.setText(TextUtils.isEmpty(mList.get(position).getTitle())
                ? "" : mList.get(position).getTitle());
        viewHolder.timeTv.setText(TextUtils.isEmpty(mList.get(position).getTime())
                ? "" : mList.get(position).getTime());
        viewHolder.contentTv.setText(TextUtils.isEmpty(mList.get(position).getContent())
                ? "" : mList.get(position).getContent());
        return convertView;
    }

    private static class ViewHolder {
        private TextView titleTv;
        private TextView timeTv;
        private TextView contentTv;
    }


    public void addList(ArrayList<InformationBean> list) {
        if (mList == null)
            mList = new ArrayList<InformationBean>();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
