package com.urgoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.TranslateCounselorEntiy;
import com.zw.express.tool.Util;

import java.util.List;

/**
 * Created by bb on 2016/7/11.
 */
public class CounselorListAdapter extends UltimateViewAdapter<CounselorListAdapter.ViewHolder> {
    private Context context;
    private List<TranslateCounselorEntiy> tcEntiys;
    private OnItemClickListener onItemClickListener;
    private int displayWidth;

    public CounselorListAdapter(Context context, List<TranslateCounselorEntiy> tcEntiys) {
        this.context = context;
        this.tcEntiys = tcEntiys;
        displayWidth = Util.getDeviceWidth(context);
    }

    public void addData(List<TranslateCounselorEntiy> tcEntiys) {
        this.tcEntiys.addAll(tcEntiys);
        notifyDataSetChanged();
    }

    public void clear() {
        clear(tcEntiys);
        notifyDataSetChanged();
    }

    public TranslateCounselorEntiy getItem(int position) {
        if (customHeaderView != null && position > 0) {
            position--;
        } else if (position < 0) {
            position = 0;
        }
        return tcEntiys.get(position);
    }

    private boolean headerCheck(int position) {
        if ((customHeaderView != null ? position <= tcEntiys.size() : position < tcEntiys.size()) && (customHeaderView != null ? position > 0 : true)) {
            return true;
        }
        return false;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_counselorlist, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return tcEntiys.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getItemCount() && headerCheck(position) && !tcEntiys.isEmpty()) {
            TranslateCounselorEntiy tcEntiy = getItem(position);
            if (tcEntiy.getTagArray() != null && tcEntiy.getTagArray().size() > 0) {
                int tagSize = tcEntiy.getTagArray().size();
                tagForVis(tagSize, holder, tcEntiy);
            } else {
                holder.tvTag1.setVisibility(View.GONE);
                holder.tvTag2.setVisibility(View.GONE);
                holder.tvTag3.setVisibility(View.GONE);
                holder.tvTag4.setVisibility(View.GONE);
                holder.tvTag5.setVisibility(View.GONE);
            }
            if (tcEntiy.getServiceTypeArray() != null && tcEntiy.getServiceTypeArray().size() > 0) {
                int serviceType = tcEntiy.getServiceTypeArray().size();
                serviceForVis(serviceType, holder, tcEntiy);
            } else {
                holder.tvService1.setVisibility(View.GONE);
                holder.tvService2.setVisibility(View.GONE);
                holder.tvService3.setVisibility(View.GONE);
            }
            if (Util.isEmpty(tcEntiy.getSchool())) {
                holder.tvSchool.setVisibility(View.GONE);
            } else {
                holder.tvSchool.setVisibility(View.VISIBLE);
                holder.tvSchool.setText(tcEntiy.getSchool());
            }
            if (!Util.isEmpty(tcEntiy.getHabitualResidence())) {
                holder.tvCountry.setText(tcEntiy.getHabitualResidence());
                holder.ivLocation.setVisibility(View.VISIBLE);
            } else {
                holder.ivLocation.setVisibility(View.GONE);
            }



            Util.setImage(holder.sdvAvatar, tcEntiy.getUserIcon());
//            holder.sdvAvatar.setImageURI(Uri.parse("http://s1.iambetter.cn/sns_app/2016/06/08/1465381515288.jpg?width=750&height=438"));
            holder.tvName.setText(tcEntiy.getEnName());
            holder.tvDes.setText(tcEntiy.getSlogan());
        }
    }

    /**
     * 判断显示几个service
     *
     * @param serviceType
     * @param holder
     * @param tcEntiy
     */
    private void serviceForVis(int serviceType, ViewHolder holder, TranslateCounselorEntiy tcEntiy) {
        holder.tvService1.setVisibility(View.VISIBLE);
        holder.tvService2.setVisibility(View.VISIBLE);
        holder.tvService3.setVisibility(View.VISIBLE);
        if (serviceType > 2) {
            holder.tvService1.setText(tcEntiy.getServiceTypeArray().get(0));
            holder.tvService2.setText(tcEntiy.getServiceTypeArray().get(1));
            holder.tvService3.setText(tcEntiy.getServiceTypeArray().get(2));
        } else if (serviceType > 1) {
            holder.tvService1.setText(tcEntiy.getServiceTypeArray().get(0));
            holder.tvService2.setText(tcEntiy.getServiceTypeArray().get(1));
            holder.tvService3.setVisibility(View.GONE);
        } else if (serviceType > 0) {
            holder.tvService1.setText(tcEntiy.getServiceTypeArray().get(0));
            holder.tvService2.setVisibility(View.GONE);
            holder.tvService3.setVisibility(View.GONE);
        }
    }

    /**
     * 判断显示几个tag
     *
     * @param tagSize
     * @param holder
     * @param tcEntiy
     */
    private void tagForVis(int tagSize, ViewHolder holder, TranslateCounselorEntiy tcEntiy) {
        holder.tvTag1.setVisibility(View.VISIBLE);
        holder.tvTag2.setVisibility(View.VISIBLE);
        holder.tvTag3.setVisibility(View.VISIBLE);
        holder.tvTag4.setVisibility(View.VISIBLE);
        holder.tvTag5.setVisibility(View.VISIBLE);
        if (tagSize > 4) {
            holder.tvTag1.setText(tcEntiy.getTagArray().get(0));
            holder.tvTag2.setText(tcEntiy.getTagArray().get(1));
            holder.tvTag3.setText(tcEntiy.getTagArray().get(2));
            holder.tvTag4.setText(tcEntiy.getTagArray().get(3));
            holder.tvTag5.setText(tcEntiy.getTagArray().get(4));
        } else if (tagSize > 3) {
            holder.tvTag1.setText(tcEntiy.getTagArray().get(0));
            holder.tvTag2.setText(tcEntiy.getTagArray().get(1));
            holder.tvTag3.setText(tcEntiy.getTagArray().get(2));
            holder.tvTag4.setText(tcEntiy.getTagArray().get(3));
            holder.tvTag5.setVisibility(View.GONE);
        } else if (tagSize > 2) {
            holder.tvTag1.setText(tcEntiy.getTagArray().get(0));
            holder.tvTag2.setText(tcEntiy.getTagArray().get(1));
            holder.tvTag3.setText(tcEntiy.getTagArray().get(2));
            holder.tvTag4.setVisibility(View.GONE);
            holder.tvTag5.setVisibility(View.GONE);
        } else if (tagSize > 1) {
            holder.tvTag1.setText(tcEntiy.getTagArray().get(0));
            holder.tvTag2.setText(tcEntiy.getTagArray().get(1));
            holder.tvTag3.setVisibility(View.GONE);
            holder.tvTag4.setVisibility(View.GONE);
            holder.tvTag5.setVisibility(View.GONE);
        } else if (tagSize > 0) {
            holder.tvTag1.setText(tcEntiy.getTagArray().get(0));
            holder.tvTag2.setVisibility(View.GONE);
            holder.tvTag3.setVisibility(View.GONE);
            holder.tvTag4.setVisibility(View.GONE);
            holder.tvTag5.setVisibility(View.GONE);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvAvatar;
        public TextView tvName;
        public TextView tvCountry;
        public TextView tvService1;
        public TextView tvService2;
        public TextView tvService3;
        public TextView tvSchool;
        public TextView tvDes;
        public TextView tvTag1;
        public TextView tvTag2;
        public TextView tvTag3;
        public TextView tvTag4;
        public TextView tvTag5;
        public View cvCounselor;
        public ImageView ivLocation;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvAvatar = (SimpleDraweeView) itemView.findViewById(R.id.img_counselor);
                ivLocation = (ImageView) itemView.findViewById(R.id.iv_location);
                cvCounselor = itemView.findViewById(R.id.cv_counselor);
                tvName = (TextView) itemView.findViewById(R.id.tv_nickname);
                tvCountry = (TextView) itemView.findViewById(R.id.tv_country);
                tvService1 = (TextView) itemView.findViewById(R.id.tv_service1);
                tvService2 = (TextView) itemView.findViewById(R.id.tv_service2);
                tvService3 = (TextView) itemView.findViewById(R.id.tv_service3);
                tvSchool = (TextView) itemView.findViewById(R.id.tv_school);
                tvDes = (TextView) itemView.findViewById(R.id.tv_des);
                tvTag1 = (TextView) itemView.findViewById(R.id.tv_tag1);
                tvTag2 = (TextView) itemView.findViewById(R.id.tv_tag2);
                tvTag3 = (TextView) itemView.findViewById(R.id.tv_tag3);
                tvTag4 = (TextView) itemView.findViewById(R.id.tv_tag4);
                tvTag5 = (TextView) itemView.findViewById(R.id.tv_tag5);
                if (onItemClickListener != null) {
                    cvCounselor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(v, getAdapterPosition());
                        }
                    });
                }
            }
        }
    }
}
