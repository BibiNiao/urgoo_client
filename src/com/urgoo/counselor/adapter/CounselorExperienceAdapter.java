package com.urgoo.counselor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.counselor.model.experienceList;

import java.util.List;

/**
 * Created by bb on 2016/9/28.
 */
public class CounselorExperienceAdapter extends BaseAdapter {
    private Context mContext;
    private List<experienceList> experienceList;

    public CounselorExperienceAdapter(Context context, List<experienceList> experienceList) {
        this.mContext = context;
        this.experienceList = experienceList;
    }

    public void addData(List<experienceList> experienceList) {
        this.experienceList.addAll(experienceList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return experienceList.size();
    }

    @Override
    public Object getItem(int position) {
        return experienceList.get(position);
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
        viewHolder.tv_content_companyName.setText(experienceList.get(position).getCompanyName());
        viewHolder.tv_content_position.setText(experienceList.get(position).getPosition());
        viewHolder.tv_content_timedata.setText(experienceList.get(position).getStartDate() + " - " + experienceList.get(position).getEndDate());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_content_companyName;
        private TextView tv_content_position;
        private TextView tv_content_timedata;
        private ImageView item_img;
    }
}