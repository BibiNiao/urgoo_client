package com.urgoo.counselor.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.client.R;
import com.urgoo.collect.event.FollowEvent;
import com.urgoo.common.ShareUtil;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorDetailActivity;
import com.urgoo.counselor.activities.CounselorMoreInterface;
import com.urgoo.counselor.activities.FindCounselorFragment;
import com.urgoo.counselor.activities.StuEvaluationAcitivity;
import com.urgoo.counselor.biz.CounselorManager;
import com.urgoo.counselor.model.Counselor;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by bb on 2016/10/9.
 */
public class ViewPaperAdapter extends PagerAdapter implements View.OnClickListener {
    private Context mContext;
    private List<Counselor> counselorList;
    private float RATIO = 0.862f;
    private CounselorMoreInterface listener;
    private int page;
    private boolean isLoad;

    public ViewPaperAdapter(Context context, List<Counselor> counselorList, CounselorMoreInterface listener, int page, boolean isLoad) {
        this.mContext = context;
        this.counselorList = counselorList;
        this.listener = listener;
        this.page = page;
        this.isLoad = isLoad;
    }

    public void addData(List<Counselor> counselorList, boolean isLoad, int page) {
        this.counselorList.addAll(counselorList);
        this.isLoad = isLoad;
        this.page = page;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return counselorList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup convertView, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_counselor, null);
        SimpleDraweeView sdvAvatar = ViewHolder.get(view, R.id.sdv_avatar);
        RelativeLayout rlAvatar = ViewHolder.get(view, R.id.rl_avatar);
        RatingBar ratingbar = ViewHolder.get(view, R.id.ratingbar);
        Button btnData = ViewHolder.get(view, R.id.btn_data);
        btnData.setTag(position);
        btnData.setOnClickListener(this);
        ImageView ivPlay = ViewHolder.get(view, R.id.iv_play);
        if (Util.isEmpty(counselorList.get(position).getShareVedio())) {
            ivPlay.setVisibility(View.GONE);
        } else {
            ivPlay.setVisibility(View.VISIBLE);
        }
        ivPlay.setTag(position);
        ivPlay.setOnClickListener(this);
        ImageView ivIn = ViewHolder.get(view, R.id.iv_in);
        ivIn.setTag(position);
        ivIn.setOnClickListener(this);
        ImageView ivShare = ViewHolder.get(view, R.id.iv_share);
        ivShare.setTag(position);
        ivShare.setOnClickListener(this);
        ImageView ivCollect = ViewHolder.get(view, R.id.iv_collect);
        ivCollect.setTag(position);
        ivCollect.setOnClickListener(this);
        TextView tvEvaluate = ViewHolder.get(view, R.id.tv_evaluate);
        tvEvaluate.setTag(position);
        tvEvaluate.setText(mContext.getString(R.string.find_pingjia, counselorList.get(position).getStudentWords()));
        tvEvaluate.setOnClickListener(this);
        ratingbar.setRating(counselorList.get(position).getStarMark() / 2);
        if (counselorList.get(position).getIsAttention().equals("1")) {
            ivCollect.setBackgroundResource(R.drawable.ic_find_collected);
        } else {
            ivCollect.setBackgroundResource(R.drawable.ic_find_collect);
        }
        TextView tvLocation = ViewHolder.get(view, R.id.tv_location);
        TextView tvName = ViewHolder.get(view, R.id.tv_name);
        TextView tvSchool = ViewHolder.get(view, R.id.tv_school);
        TextView tvTag1 = ViewHolder.get(view, R.id.tv_tag1);
        TextView tvTag2 = ViewHolder.get(view, R.id.tv_tag2);
        showTag(tvTag1, tvTag2, position);
        tvName.setText(counselorList.get(position).getCnName());
        tvLocation.setText(counselorList.get(position).getHabitualResidence());
        tvSchool.setText(counselorList.get(position).getSchool());
        if (Util.isEmpty(counselorList.get(position).getLinkedin())) {
            ivIn.setVisibility(View.GONE);
        } else {
            ivIn.setVisibility(View.VISIBLE);
        }
        int width = Util.getDeviceWidth(mContext);
        rlAvatar.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (RATIO * width)));
        sdvAvatar.setImageURI(Uri.parse(counselorList.get(position).getUserIcon()));
        convertView.addView(view);
        view.setTag(position);
        return view;
    }

    /**
     * 显示TAG
     *
     * @param tvTag1
     * @param tvTag2
     * @param position
     */
    private void showTag(TextView tvTag1, TextView tvTag2, int position) {
        if (counselorList.get(position).getOrgnizationList() != null && counselorList.get(position).getOrgnizationList().size() > 0) {
            if (counselorList.get(position).getOrgnizationList().size() == 1) {
                tvTag1.setText(counselorList.get(position).getOrgnizationList().get(0));
                tvTag2.setVisibility(View.GONE);
            } else {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
                tvTag1.setText(counselorList.get(position).getOrgnizationList().get(0));
                tvTag2.setText(counselorList.get(position).getOrgnizationList().get(1));
            }
        } else {
            tvTag1.setVisibility(View.GONE);
            tvTag2.setVisibility(View.GONE);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (counselorList.size() > 0 && isLoad) {
            System.out.println("调用接口" + position);
            if (position == (page * 10) + 8) {
                isLoad = false;
                listener.loadMore();
            }
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void onClick(View v) {
        int position;
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.tv_evaluate:
                position = (Integer) v.getTag();
                bundle = new Bundle();
                bundle.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, counselorList.get(position).getCounselorId());
                Util.openActivityWithBundle(mContext, StuEvaluationAcitivity.class, bundle);
                break;
            case R.id.iv_play:
                position = (Integer) v.getTag();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(counselorList.get(position).getShareVedio()), "video/mp4");
                mContext.startActivity(intent);
                break;
            case R.id.btn_data:
                position = (Integer) v.getTag();
                bundle = new Bundle();
                bundle.putString(CounselorDetailActivity.EXTRA_COUNSELOR_ID, counselorList.get(position).getCounselorId());
                bundle.putString(CounselorDetailActivity.EXTRA_TITLE, counselorList.get(position).getCnName());
                Util.openActivityWithBundle(mContext, CounselorDetailActivity.class, bundle);
                break;
            case R.id.iv_in:
                position = (Integer) v.getTag();
                Uri uri = Uri.parse(counselorList.get(position).getLinkedin());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
                break;
            case R.id.iv_collect:
                position = (Integer) v.getTag();
                collectOrCancel((ImageView) v, counselorList.get(position));
                break;
            case R.id.iv_share:
                position = (Integer) v.getTag();
                com.urgoo.domain.ShareDetail shareDetail = counselorList.get(position).getShareDetail();
                ShareUtil.share(mContext, shareDetail.title, shareDetail.text, shareDetail.pic, ZWConfig.URGOOURL_BASE + shareDetail.url);
                break;
        }
    }

    /**
     * 设置按钮状态
     *
     * @param v
     * @param isAttention
     */
    private void setCollectStatus(ImageView v, String isAttention) {
        if (isAttention.equals("1")) {
            v.setImageResource(R.drawable.ic_find_collected);
        } else {
            v.setImageResource(R.drawable.ic_find_collect);
        }
        v.setEnabled(true);
    }

    /**
     * 收藏或者取消收藏
     *
     * @param v
     * @param counselor
     */
    private void collectOrCancel(final ImageView v, final Counselor counselor) {
        if (counselor.getIsAttention().equals("1")) {
            CounselorManager.getInstance(mContext).getCancleFollow(new StringRequestCallBack() {
                @Override
                public void onSuccess(EventCode eventCode, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonCode = new JSONObject(jsonObject.get("header").toString());
                        String code = jsonCode.getString("code");
                        if (code.equals("200")) {
                            Util.shortToast(mContext, "取消收藏");
                            counselor.setIsAttention("0");
                            setCollectStatus(v, "0");
                            EventBus.getDefault().post(new FollowEvent(counselor.getCounselorId(), counselor.getIsAttention()));
                        } else {
                            Util.shortToast(mContext, "操作失败");
                        }
                    } catch (JSONException e) {
                        Util.shortToast(mContext, "操作失败");
                    } finally {
                        v.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(EventCode eventCode, Call call) {
                    Util.shortToast(mContext, "操作失败");
                    v.setEnabled(true);
                }
            }, counselor.getCounselorId(), "1");
        } else {
            CounselorManager.getInstance(mContext).getAddFollow(new StringRequestCallBack() {
                @Override
                public void onSuccess(EventCode eventCode, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonCode = new JSONObject(jsonObject.get("header").toString());
                        String code = jsonCode.getString("code");
                        if (code.equals("200")) {
                            Util.shortToast(mContext, "收藏成功");
                            counselor.setIsAttention("1");
                            setCollectStatus(v, "1");
                            EventBus.getDefault().post(new FollowEvent(counselor.getCounselorId(), counselor.getIsAttention()));
                        } else {
                            Util.shortToast(mContext, "操作失败");
                        }
                    } catch (JSONException e) {
                        Util.shortToast(mContext, "操作失败");
                    } finally {
                        v.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(EventCode eventCode, Call call) {
                    Util.shortToast(mContext, "操作失败");
                    v.setEnabled(true);
                }
            }, counselor.getCounselorId(), "1");
        }
    }
}
