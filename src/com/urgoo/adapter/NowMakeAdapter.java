package com.urgoo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.client.R;
import com.urgoo.db.MakeManager;
import com.urgoo.domain.NowMakeBean;
import com.urgoo.schedule.activites.Precontract;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/6/15.
 */
public class NowMakeAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<NowMakeBean.AdvanceBean.ListBean> mList;
    private ArrayList<NowMakeBean.AdvanceBean> mTobeconfir;
    private Context mContext;
    public static final String TAG = "makes : ";
    private int type;
    private MakeManager mgr;
    private int Id;
    private Boolean figs = true, T = true;


    public NowMakeAdapter(Context context, ArrayList<NowMakeBean.AdvanceBean.ListBean> list, int mType, MakeManager mMgr, ArrayList<NowMakeBean.AdvanceBean> lists) {
        layoutInflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
        type = mType;
        mgr = mMgr;
        mTobeconfir = lists;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.schedule_item, parent, false);
            viewHolder.schedule_time = (TextView) convertView.findViewById(R.id.schedule_time);
            viewHolder.schedule_timeusa = (TextView) convertView.findViewById(R.id.schedule_timeusa);
            viewHolder.schedule_img = (ImageView) convertView.findViewById(R.id.schedule_img);
            viewHolder.schedule_img2 = (ImageView) convertView.findViewById(R.id.schedule_img2);
            viewHolder.schedule_img3 = (ImageView) convertView.findViewById(R.id.schedule_img3);
            viewHolder.rrls = (RelativeLayout) convertView.findViewById(R.id.rrls);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.schedule_time.setText(mList.get(position).getCnStartTime() + "-" + mList.get(position).getCnEndTime());
//        viewHolder.schedule_timeusa.setText("(美国东部时间 " + mList.get(position).getOtherStartTime() + "-" + mList.get(position).getOtherEndTime() + ")");

        Id = Integer.parseInt(mList.get(position).getAdvanceTimeId());

        // 不显示 不可选的时候，判断是否可选的条件也就不需要了
//        if (mList.get(position).getType().equals("2")) {
////            viewHolder.rrls.setVisibility(View.GONE);
//            viewHolder.schedule_time.setTextColor(mContext.getResources().getColor(R.color.b_b7b7b7));
//            viewHolder.schedule_timeusa.setTextColor(mContext.getResources().getColor(R.color.b_b7b7b7));
//            viewHolder.schedule_img3.setVisibility(View.VISIBLE);
//            viewHolder.schedule_img2.setVisibility(View.GONE);
//            viewHolder.schedule_img.setVisibility(View.GONE);
//            figs = false;
//        }
//        if (mList.get(position).getType().equals("1")) {
//            viewHolder.rrls.setVisibility(View.VISIBLE);
        viewHolder.schedule_time.setTextColor(mContext.getResources().getColor(R.color.b_383838));
        viewHolder.schedule_timeusa.setTextColor(mContext.getResources().getColor(R.color.b_383838));
        viewHolder.schedule_img3.setVisibility(View.GONE);
        viewHolder.schedule_img2.setVisibility(View.GONE);
        viewHolder.schedule_img.setVisibility(View.VISIBLE);
//            figs = true;
//        }


        Log.d(TAG, "读到的数据000      " + mgr.query(0, 0, T));

//        if (figs) {
        //选中是 0
        if (mgr.query(type, Id) == 0) {
            viewHolder.schedule_img2.setVisibility(View.VISIBLE);
            viewHolder.schedule_img.setVisibility(View.GONE);
            viewHolder.schedule_img3.setVisibility(View.GONE);
        }
        //没选中是 1
        if (mgr.query(type, Id) == 1) {
            viewHolder.schedule_img2.setVisibility(View.GONE);
            viewHolder.schedule_img3.setVisibility(View.GONE);
            viewHolder.schedule_img.setVisibility(View.VISIBLE);
        }
//        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).getAdvanceTimeId() != null)
                    Id = Integer.parseInt(mList.get(position).getAdvanceTimeId());
                if (mList.get(position).getType().equals("1")) {
                    //没选中 1
                    if (mgr.query(type, Id) == 1) {
                        int num = mgr.query(0, 0, T);
                        if (num >= 3) {
                            Toast.makeText(mContext, "只能选三个时间段", Toast.LENGTH_SHORT).show();
//                            mgr.update(0, 0, num + 1, T);
                            Log.d(TAG, "读到的数据333    " + mgr.query(0, 0, T));
                        } else {
                            viewHolder.schedule_img2.setVisibility(View.VISIBLE);
                            viewHolder.schedule_img.setVisibility(View.GONE);
                            mgr.update(type, Id, 0);
                            //  现在是点击的选中
//                            num = mgr.query(0, 0, T);
                            Log.d(TAG, "读到的数据3    " + num);
                            if (num >= 3) {

                            } else {
                                mgr.update(0, 0, num + 1, T);
                                Log.d(TAG, "读到的数据4    " + mgr.query(0, 0, T));
                                switch (mgr.query(0, 0, T)) {
                                    case 1:
                                        Precontract.mString = mTobeconfir.get(type).getDate() + " " + mTobeconfir.get(type).getWeekName() + "\n" + viewHolder.schedule_time.getText().toString() + "\n";
                                        Precontract.mTime = viewHolder.schedule_timeusa.getText().toString();
                                        Precontract.mData = "{advanceDate=" + mTobeconfir.get(type).getAdvanceDate() + ",advanceTimeId=" + mList.get(position).getAdvanceTimeId() + "}";
                                        logs();
                                        break;
                                    case 2:
                                        Precontract.mString2 = mTobeconfir.get(type).getDate() + " " + mTobeconfir.get(type).getWeekName() + "\n" + viewHolder.schedule_time.getText().toString() + "\n";
                                        Precontract.mTime2 = viewHolder.schedule_timeusa.getText().toString();
                                        Precontract.mData2 = "{advanceDate=" + mTobeconfir.get(type).getAdvanceDate() + ",advanceTimeId=" + mList.get(position).getAdvanceTimeId() + "}";
                                        logs();
                                        break;
                                    case 3:
                                        Precontract.mString3 = mTobeconfir.get(type).getDate() + " " + mTobeconfir.get(type).getWeekName() + "\n" + viewHolder.schedule_time.getText().toString() + "\n";
                                        Precontract.mTime3 = viewHolder.schedule_timeusa.getText().toString();
                                        Precontract.mData3 = "{advanceDate=" + mTobeconfir.get(type).getAdvanceDate() + ",advanceTimeId=" + mList.get(position).getAdvanceTimeId() + "}";
                                        logs();
                                        break;
                                    default:
                                        return;

                                }
                            }
                        }
                        //选中 0
                    } else if (mgr.query(type, Id) == 0) {
                        viewHolder.schedule_img2.setVisibility(View.GONE);
                        viewHolder.schedule_img.setVisibility(View.VISIBLE);
                        mgr.update(type, Id, 1);
                        int num = mgr.query(0, 0, T);
                        Log.d(TAG, "读到的数据5     " + num);
                        if (num <= 0) {
                            Toast.makeText(mContext, "最少选一个时间段", Toast.LENGTH_SHORT).show();
                            mgr.update(0, 0, num, T);
                            Log.d(TAG, "读到的数据555     " + mgr.query(0, 0, T));
                        } else {
                            mgr.update(0, 0, num - 1, T);
                            Log.d(TAG, "读到的数据6     " + mgr.query(0, 0, T));
                            switch (mgr.query(0, 0, T)) {
                                case 0:
                                    Precontract.mString = null;
                                    Precontract.mData = null;
                                    Log.d(TAG, "mString:    " + Precontract.mString);
                                    Log.d(TAG, "mData:    " + Precontract.mData);
                                    break;
                                case 1:
                                    Precontract.mString2 = null;
                                    Precontract.mData2 = null;
                                    Log.d(TAG, "mString2:   " + Precontract.mString2);
                                    Log.d(TAG, "mData2:   " + Precontract.mData2);
                                    break;
                                case 2:
                                    Precontract.mString3 = null;
                                    Precontract.mData3 = null;
                                    Log.d(TAG, "mString3:   " + Precontract.mString3);
                                    Log.d(TAG, "mData3:   " + Precontract.mData3);
                                    break;
                                default:
                                    return;
                            }
                        }
                    }
                }

                if (mList.get(position).getType().equals("2")) {
                    Log.d(TAG, "onClick: "+mList.get(position).getType());
                }
            }
        });
        return convertView;
    }

    private void logs() {
        Log.d(TAG, "mString :   " + Precontract.mString);
        Log.d(TAG, "mTime :   " + Precontract.mTime);
        Log.d(TAG, "mData :   " + Precontract.mData);
        Log.d(TAG, "mString2 :  " + Precontract.mString2);
        Log.d(TAG, "mTime2 :  " + Precontract.mTime2);
        Log.d(TAG, "mData2 :  " + Precontract.mData2);
        Log.d(TAG, "mString3 :  " + Precontract.mString3);
        Log.d(TAG, "mTime3 :  " + Precontract.mTime3);
        Log.d(TAG, "mData3 :  " + Precontract.mData3);
    }

    private static class ViewHolder {
        private TextView schedule_time;
        private TextView schedule_timeusa;
        private ImageView schedule_img;
        private ImageView schedule_img2;
        private ImageView schedule_img3;
        private RelativeLayout rrls;
    }
}
