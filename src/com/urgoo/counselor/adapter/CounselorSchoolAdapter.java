package com.urgoo.counselor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.counselor.model.EduList;

import java.util.List;

/**
 * Created by bb on 2016/9/28.
 */
public class CounselorSchoolAdapter extends BaseAdapter {
    private Context mContext;
    private List<EduList> eduList;

    public CounselorSchoolAdapter(Context context, List<EduList> eduList) {
        this.mContext = context;
        this.eduList = eduList;
    }

    public void addData(List<EduList> eduList) {
        this.eduList.addAll(eduList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eduList.size();
    }

    @Override
    public Object getItem(int position) {
        return eduList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_counselor_workyears, parent, false);
            viewHolder.tv_content_position = (TextView) convertView.findViewById(R.id.tv_content_position);
            viewHolder.tv_content_timedata = (TextView) convertView.findViewById(R.id.tv_content_timedata);
            viewHolder.tv_content_companyName = (TextView) convertView.findViewById(R.id.tv_content_companyName);
            viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.item_img.setVisibility(View.GONE);
        } else
            viewHolder.item_img.setVisibility(View.VISIBLE);
        viewHolder.tv_content_companyName.setText(eduList.get(position).getEducationName());
        viewHolder.tv_content_position.setText(eduList.get(position).getMajor());
        viewHolder.tv_content_timedata.setText(eduList.get(position).getStartTime() + " - " + eduList.get(position).getEndTime());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_content_companyName;
        private TextView tv_content_position;
        private TextView tv_content_timedata;
        private ImageView item_img;
    }
}