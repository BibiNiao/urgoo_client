package com.urgoo.counselor.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.client.R;
import com.zw.express.tool.ImageLoaderUtils;
import com.zw.express.tool.Util;

/**
 * Created by urgoo_01 on 2016/7/11.
 */

//TODO 真是图片页
public class CounselorimgFragment extends Fragment {
    private SimpleDraweeView imView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.counselor_vidoe, null);
        imView = (SimpleDraweeView) mView.findViewById(R.id.im_cont);
        imView.setImageURI(Uri.parse(url));
        imView.setLayoutParams(new RelativeLayout.LayoutParams(Util.getDeviceWidth(getActivity()), (int) (Util.getDeviceWidth(getActivity()) * 0.773f))); //使设置好的布局参数应用到控件
        imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return mView;
    }

    //  传入img网址的方法
    public void setUrl(String mTAG) {
        url = mTAG;
    }
}
