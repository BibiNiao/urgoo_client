package com.urgoo.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.DataUtil;
import com.urgoo.domain.SystemInformationEntity;
import com.urgoo.domain.ZhiBoEntity;
import com.zw.express.tool.image.ImageCacheSrcInfo;
import com.zw.express.tool.image.ImageWorker;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/7/14.
 */
public class NewZhiBoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> mInfoList;
    private int mInfoType = 0;
    private imageLoadBusiness imgservice = new imageLoadBusiness();

    public NewZhiBoListAdapter(Context context, List<Object> mInfoList,
                               int mInfoType) {
        this.mContext = context;
        this.mInfoList = mInfoList;
        this.mInfoType = mInfoType;
    }

    @Override
    public int getCount() {
        return mInfoList != null ? mInfoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mInfoList != null && mInfoList.size() > 0) ? mInfoList
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Object o = mInfoList.get(position);
        int num = getCount();
        switch (mInfoType) {
            case 0:
                final ZhiBoEntity entity = (ZhiBoEntity) o;
                ViewHolderGNQB vhq = null;
                if (view == null) {
                    vhq = new ViewHolderGNQB();
                    view = LayoutInflater.from(mContext).inflate(
                            R.layout.zhibolist_item_layout, null);
                    vhq.tv_baomingnumber = (TextView) view.findViewById(R.id.tv_baomingnumber);
                    vhq.tv_daojishi = (TextView) view.findViewById(R.id.tv_daojishi);
                    vhq.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    vhq.tv_miaoshu = (TextView) view.findViewById(R.id.tv_miaoshu);
                    vhq.iv_mingpian = (SimpleDraweeView) view.findViewById(R.id.iv_mingpian);
                    vhq.line = view.findViewById(R.id.line);

                    view.setTag(vhq);
                } else {
                    vhq = (ViewHolderGNQB) view.getTag();
                }

                if ((mInfoList.size() - 1) == position) {
                    vhq.line.setVisibility(View.GONE);
                }
                vhq.iv_mingpian.setImageURI(Uri.parse(entity.getMasterPic()));
                vhq.tv_baomingnumber.setText(entity.getPaticipateCount() + "人已报名");
                vhq.tv_title.setText(entity.getTitle());
                vhq.tv_miaoshu.setText(entity.getDes());
                long timeStr = Long.parseLong(entity.getBalanceTime());
                if (timeStr > 0) {

                    vhq.tv_daojishi.setText(DataUtil.formatDuring2(Integer.parseInt(timeStr + "")) + "后开始");

                   /* CountdownUtil c = new CountdownUtil(timeStr * 1000, vhq.tv_daojishi);
                    c.countdown();*/
                } else {
                    vhq.tv_daojishi.setText("进入直播");
                }

                break;
            case 1:
                break;
            default: {
                break;
            }
        }
        return view;
    }

    class ViewHolderGNQB {
        LinearLayout qbLayout;
        TextView tv_daojishi;
        TextView tv_baomingnumber;
        TextView tv_title;
        TextView tv_miaoshu;
        SimpleDraweeView iv_mingpian;
        View line;
    }

    private void imageLoad(String url, final ImageView img) {
        ImageCacheSrcInfo icsi = new ImageCacheSrcInfo(url, 84, 84);
        ImageWorker.newInstance(mContext).loadBitmap(icsi, img);
    }

    /**
     * 倒计时
     */
    public class CountdownUtil {
        private long time;
        TextView counetdownView;
        CountdownThread thread;
        SimpleDateFormat formatter;
        String hms;

        /**
         * @time:时间差(指定的一段时间长),时间戳
         * @counetdownView：TextView显示倒计时
         */
        public CountdownUtil(long time, TextView counetdownView) {
            this.time = time;
            this.counetdownView = counetdownView;
        }

        /**
         * 倒计时
         */
        public void countdown() {
            formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT +8:00"));//设置时区(北京),如果你不设置这个,你会发现你的时间总会多出来8个小时

            thread = new CountdownThread(time, 1000);// 构造CountDownTimer对象
            thread.start();
        }

        class CountdownThread extends CountDownTimer {
            public CountdownThread(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("mytest20160615","info->"+millisUntilFinished);
                hms = formatter.format(millisUntilFinished);//转化成  "00:00:00"的格式
                //hms = formatTime(millisUntilFinished);//转化成  "00:00:00"的格式
                //counetdownView.setText(hms);
                counetdownView.setText(DataUtil.formatDuring2(Integer.parseInt(millisUntilFinished / 1000 + "")) + "后开始");
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                //倒计时结束时触发
                //type="100";
                /*chatFragment.ll_video.setBackgroundColor(0xFF26bdab);
                chatFragment.ll_voice.setBackgroundColor(0xFF26bdab);*/
                //type = 1;
                counetdownView.setText("00:00:00");
                //tv_operating_title.setText("Send Alert");
                //im_icon.setVisibility(View.GONE);
            }
        }

        /**
         * 终止线程
         */
        public void stopThread() {
            thread.cancel();
        }
    }
}
