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
import com.urgoo.domain.ZhiBoEntity;
import com.urgoo.domain.ZhiBoPinglunEntity;
import com.zw.express.tool.image.ImageCacheSrcInfo;
import com.zw.express.tool.image.ImageWorker;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/7/18.
 */
public class PinglunListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Object> mInfoList;
    private int mInfoType = 0;

    public PinglunListAdapter(Context context, List<Object> mInfoList,
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
                final ZhiBoPinglunEntity entity = (ZhiBoPinglunEntity) o;
                ViewHolderGNQB vhq = null;
                if (view == null) {
                    vhq = new ViewHolderGNQB();
                    view = LayoutInflater.from(mContext).inflate(
                            R.layout.pinglun_item_layouy, null);
                    vhq.tv_info = (TextView) view.findViewById(R.id.tv_info);
                    vhq.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
                    vhq.tv_shijian = (TextView) view.findViewById(R.id.tv_shijian);
                    vhq.img_touxiang = (SimpleDraweeView) view.findViewById(R.id.img_touxiang);
                    vhq.line = view.findViewById(R.id.line);

                    view.setTag(vhq);
                } else {
                    vhq = (ViewHolderGNQB) view.getTag();
                }

                vhq.tv_info.setText(entity.getContent());
                vhq.tv_shijian.setText(entity.getInsertDatetime());
                vhq.tv_nickname.setText(entity.getNickName());
                vhq.img_touxiang.setImageURI(Uri.parse(entity.getUserIcon()));
                if (position == 0) {
                    vhq.line.setVisibility(View.GONE);
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
        TextView tv_info;
        View line;
        TextView tv_nickname;
        TextView tv_shijian;
        SimpleDraweeView img_touxiang;

    }

    private void imageLoad(String url, final ImageView img) {
        ImageCacheSrcInfo icsi = new ImageCacheSrcInfo(url, 84, 84);
        ImageWorker.newInstance(mContext).loadBitmap(icsi, img);
    }


}
