package com.urgoo.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.message.biz.RobotOption;

import java.util.List;

/**
 * Created by bb on 2016/11/1.
 */
public class VerticalAdapter extends BaseAdapter {
    private List<RobotOption> robotOptions;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public VerticalAdapter(Context context, List<RobotOption> robotOptions) {
        this.mContext = context;
        this.robotOptions = robotOptions;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return robotOptions.size();
    }

    @Override
    public RobotOption getItem(int position) {
        return robotOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        RobotOption robotOption = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.chatting_vertiacal_item, parent, false);
            viewHolder.button = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button.setText(robotOption.getText());
        if (robotOptions.get(position).isChecked()) {
            viewHolder.button.setBackgroundResource(R.drawable.btn_enable_checked_bg);
        } else {
            viewHolder.button.setBackgroundResource(R.drawable.btn_checked_bg);
        }
        if (onItemClickListener != null) {
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
        return convertView;
    }

    private static class ViewHolder {
        private Button button;
    }
}
