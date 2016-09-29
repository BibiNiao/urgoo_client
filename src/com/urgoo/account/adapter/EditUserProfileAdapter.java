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
            items.add(new SettingItem(mContext.getString(R.string.setting_school), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_type), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_grade), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_score), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_school_score), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_awards), ""));
            items.add(new SettingItem(mContext.getString(R.string.setting_activities), ""));
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

//        if (position == 1) {
//            holder.tvSubTitle.setText(SPManager.getInstance(mContext).getBetterCode());
//            if (SPManager.getInstance(mContext).getBetterCodeStatus() == 1) {
//                holder.ivNext.setVisibility(View.GONE);
//            } else {
//                holder.ivNext.setVisibility(View.VISIBLE);
//            }
//        } else {
//            holder.ivNext.setVisibility(View.VISIBLE);
//        }
//        switch (position) {
//            case 0:
//                holder.tvSubTitle.setText(SPManager.getInstance(mContext).getNickName());
//                break;
//            case 2:
//                if (!Utils.isEmpty(user.getCountryName()) || !Utils.isEmpty(user.getAreaName())) {
//                    holder.tvSubTitle.setText(Utils.getRegionDisplay(user.getCountryName(), user.getAreaName()));
//                }
//                break;
//            case 3:
//                if (user.getSex() == 1) {
//                    holder.tvSubTitle.setText(mContext.getString(R.string.man));
//                } else {
//                    holder.tvSubTitle.setText(mContext.getString(R.string.girl));
//                }
//                break;
//            case 5:
//                holder.tvSubTitle.setText(SPManager.getInstance(mContext).getIntroduction());
//                break;
//        }
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
