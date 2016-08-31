package com.urgoo.adapter;

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
 * Created by bb on 2016/6/28.
 */
public class PaySelectAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private static List<PayItem> items = new ArrayList<PayItem>();

    public PaySelectAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        if (items.isEmpty()) {
            items.add(new PayItem(R.drawable.ic_alipay, mContext.getString(R.string.pay_alipay)));
            items.add(new PayItem(R.drawable.ic_wechat, mContext.getString(R.string.pay_wechat)));
        }
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public PayItem getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.select_pay_item, null);
            holder = new ViewHolder();
            holder.ivPay = (ImageView) convertView.findViewById(R.id.iv_pay);
            holder.tvPayPath = (TextView) convertView.findViewById(R.id.tv_pay_path);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PayItem item = getItem(position);
        holder.ivPay.setImageResource(item.imPay);
        holder.tvPayPath.setText(item.payPath);
        return convertView;
    }

    class ViewHolder {
        ImageView ivPay;
        TextView tvPayPath;
    }

    class PayItem {
        Integer imPay;
        String payPath;

        public PayItem(Integer imPay, String payPath) {
            this.imPay = imPay;
            this.payPath = payPath;
        }
    }
}
