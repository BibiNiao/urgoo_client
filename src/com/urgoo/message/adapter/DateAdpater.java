package com.urgoo.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.message.biz.RobotOption;

import java.util.List;

/**
 * Created by bb on 2016/11/1.
 */
public class DateAdpater extends RecyclerView.Adapter<MyViewHolder> implements AbsListView.OnScrollListener {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    private List<RobotOption> robotOptions;
    private int lastPoition;

    public DateAdpater(Context context, List<RobotOption> robotOptions) {
        this.mContext = context;
        this.robotOptions = robotOptions;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return robotOptions.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // TODO Auto-generated method stub
        lastPoition = position;
        holder.title.setText(robotOptions.get(position).getText());
        holder.text.setText(robotOptions.get(position).getPara());
        if (robotOptions.get(position).isChecked()) {
            holder.button.setBackgroundResource(R.drawable.btn_enable_checked_bg);
        } else {
            holder.button.setBackgroundResource(R.drawable.btn_checked_bg);
        }
        if (mOnItemClickLitener != null) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.button, position);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        View view = mInflater.inflate(R.layout.chatting_date_item, arg0, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 1) {

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    Button button;
    TextView title;
    TextView text;

    public MyViewHolder(View arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
        button = (Button) arg0.findViewById(R.id.button);
        title = (TextView) arg0.findViewById(R.id.title);
        text = (TextView) arg0.findViewById(R.id.text);
    }
}

//public class DateAdpater extends BaseAdapter {
//    private List<RobotOption> robotOptions;
//    private Context mContext;
//    private OnItemClickListener onItemClickListener;
//
//    public DateAdpater(Context context, List<RobotOption> robotOptions) {
//        this.mContext = context;
//        this.robotOptions = robotOptions;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    @Override
//    public int getCount() {
//        return robotOptions.size();
//    }
//
//    @Override
//    public RobotOption getItem(int position) {
//        return robotOptions.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        RobotOption robotOption = getItem(position);
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.chatting_date_item, parent, false);
//            viewHolder.button = (Button) convertView.findViewById(R.id.button);
//            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
//            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.title.setText(robotOption.getText());
//        viewHolder.text.setText(robotOption.getPara());
//        if (onItemClickListener != null) {
//            viewHolder.button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemClick(v, position);
//                }
//            });
//        }
//        return convertView;
//    }
//
//    private static class ViewHolder {
//        private Button button;
//        private TextView title;
//        private TextView text;
//    }
//}
