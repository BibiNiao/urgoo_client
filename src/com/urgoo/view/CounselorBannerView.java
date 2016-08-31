package com.urgoo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.urgoo.domain.BannerItem;
import com.urgoo.flashview.FlashView;
import com.urgoo.flashview.FlashViews;
import com.zw.express.tool.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/7/28.
 */
public class CounselorBannerView extends FlashViews {
    private Context mContext;

    public CounselorBannerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CounselorBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void init(List<String> imageUrls) {
        setLunbo();
        setRatio(0.773f);
        loadBannerInfo(imageUrls);
    }

    private void loadBannerInfo(List<String> imageUrls) {
        setImageUris(imageUrls);
    }
}
