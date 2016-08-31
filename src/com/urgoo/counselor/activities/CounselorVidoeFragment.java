package com.urgoo.counselor.activities;

import android.content.Intent;
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
public class CounselorVidoeFragment extends Fragment {
    public int type;
    private String url;
    private String VideoPic;
    private SimpleDraweeView im_cont, im_cont2;


    //  播放视频页
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.counselor_vidoe, null);
        im_cont = (SimpleDraweeView) mView.findViewById(R.id.im_cont);
        im_cont.setImageURI(Uri.parse(VideoPic));
        im_cont.setLayoutParams(new RelativeLayout.LayoutParams(Util.getDeviceWidth(getActivity()), (int) (Util.getDeviceWidth(getActivity()) * 0.773f))); //使设置好的布局参数应用到控件
        im_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastVideo(url);
            }
        });
        return mView;
    }

    //  传入img网址的方法
    public void setUrl(String mTAG) {
        url = mTAG;
    }

    //  传入Video网址的方法
    public void setVideoPic(String setVideoPics) {
        VideoPic = setVideoPics;
    }

    //  展示Video的方法
    private void broadcastVideo(String videoURL) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setDataAndType(Uri.parse("http://urgootest.oss-cn-qingdao.aliyuncs.com/test.mp4"),"video/mp4");
        intent.setDataAndType(Uri.parse(videoURL), "video/mp4");
        startActivity(intent);
    }
}
