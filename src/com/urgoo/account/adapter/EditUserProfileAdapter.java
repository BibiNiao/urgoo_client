package com.urgoo.account.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.account.model.UserBean;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置适配器
 */
public class EditUserProfileAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private static List<SettingItem> items = new ArrayList<>();
    private UserBean user;

    public EditUserProfileAdapter(Context context, UserBean user) {
        this.mContext = context;
        this.user = user;
        mInflater = LayoutInflater.from(context);
        if (items.isEmpty()) {
            items.add(new SettingItem(mContext.getString(R.string.setting_nickname), SPManager.getInstance(mContext).getNickName()));
            items.add(new SettingItem(mContext.getString(R.string.setting_modify), ""));

//            items.add(new SettingItem(mContext.getString(R.string.setting_school), user.getSchoolName()));
//            items.add(new SettingItem(mContext.getString(R.string.setting_type), user.getSchoolCourse()));
//            items.add(new SettingItem(mContext.getString(R.string.setting_grade), user.getGradeName()));
//            items.add(new SettingItem(mContext.getString(R.string.setting_score), user.getSchoolScoreName()));
//            items.add(new SettingItem(mContext.getString(R.string.setting_school_score), user.getExamType().equals("1") ? "已填写" : "未填写"));
//            items.add(new SettingItem(mContext.getString(R.string.setting_awards), user.getAcademicAwardsType().equals("1")  ? "已填写" : "未填写"));
//            items.add(new SettingItem(mContext.getString(R.string.setting_activities), user.getExtracurricularActivitiesStatus().equals("1") ? "已填写" : "未填写"));
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
            convertView = mInflater.inflate(R.layout.setting_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvSubTitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
            holder.ivNext = (ImageView) convertView.findViewById(R.id.iv_next);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SettingItem item = getItem(position);
        holder.tvTitle.setText(item.title);
        if (TextUtils.isEmpty(item.subTitle)) {
            holder.tvSubTitle.setVisibility(View.GONE);
        } else {
            holder.tvSubTitle.setVisibility(View.VISIBLE);
            holder.tvSubTitle.setText(item.subTitle);
        }

        switch (position) {
            case 0:
                holder.tvSubTitle.setText(SPManager.getInstance(mContext).getNickName());
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;
        ImageView ivNext;
    }

    class SettingItem {
        String title;
        String subTitle;

        public SettingItem(String title, String subTitle) {
            this.title = title;
            this.subTitle = subTitle;
        }
    }
}
