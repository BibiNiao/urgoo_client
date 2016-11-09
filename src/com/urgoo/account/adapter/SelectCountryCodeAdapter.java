package com.urgoo.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urgoo.account.model.RegionCode;
import com.urgoo.client.R;
import com.urgoo.view.PinnedSectionListView;

import java.util.List;

/**
 * 选择国家适配器
 *
 * @author qiang.w
 */
public class SelectCountryCodeAdapter extends BaseAdapter implements
        PinnedSectionListView.PinnedSectionListAdapter {

    private Context context;
    private List<RegionCode> data;
    private LayoutInflater inflater;

    public SelectCountryCodeAdapter(Context context, List<RegionCode> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RegionCode getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        //进行filter搜索时防止下标越界
        if (position < getCount()) {
            return getItem(position).type;
        } else {
            return RegionCode.ITEM;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItem(position).type == RegionCode.SECTION) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == RegionCode.SECTION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.register_country_code_item, null);
            holder.tvCountry = (TextView) convertView.findViewById(R.id.tv_country);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_code);
            holder.tvSection = (TextView) convertView.findViewById(R.id.tv_section);
            holder.llCountry = convertView.findViewById(R.id.ll_country);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RegionCode info = data.get(position);
        if (info != null) {
            if (info.type == RegionCode.SECTION) {
                holder.llCountry.setVisibility(View.GONE);
                holder.tvSection.setVisibility(View.VISIBLE);
                holder.tvSection.setText(data.get(position).getFirstLetterPY()
                        .toUpperCase());
            } else {
                holder.tvSection.setVisibility(View.GONE);
                holder.llCountry.setVisibility(View.VISIBLE);
                holder.tvCountry.setText(data.get(position).getName());
                holder.tvCode.setText(data.get(position).getCode());
            }
        }
        return convertView;
    }

    /**
     * 为城市数据增加首字母分组(为了实现当前分组悬浮效果)
     */
    public void initAdapter() {
        RegionCode info = null;
        for (int i = 0; i < getCount(); i++) {
            if (info == null || !info.getFirstLetterPY().equals(
                    data.get(i).getFirstLetterPY())) {
                info = new RegionCode();
                info.type = RegionCode.SECTION;
                info.setFirstLetterPY(data.get(i).getFirstLetterPY());
                data.add(i, info);
                info = data.get(i);
            }
        }
        notifyDataSetChanged();
    }

    class Holder {
        private TextView tvCountry;
        private TextView tvCode;
        private TextView tvSection;
        private View llCountry;
    }

    /**
     * 根据搜索框关键字更新适配器
     *
     * @param list
     */
    public void updateData(List<RegionCode> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = data.get(i).getFirstLetterPY();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

}
