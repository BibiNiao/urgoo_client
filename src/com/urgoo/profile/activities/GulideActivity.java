package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urgoo.account.activity.HomeActivity;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.message.activities.MainActivity;
import com.zw.express.tool.Util;
import com.zw.express.tool.image.ImageLoaderUtil;
import com.zw.express.tool.log.Log;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/4/11.
 */
public class GulideActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<ImageView> mList = new ArrayList<ImageView>();
    private ImageView imageView1, imageView2, imageView3, imageView4,imageView5;
    private TextView point1Tv, point2Tv, point3Tv;
    private LinearLayout mLinearLayout;
    private Button mBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gulide_activity);
        SPManager.getInstance(this).setVersionCode(String.valueOf(Util.getVersionCode(this)));
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mLinearLayout = (LinearLayout) findViewById(R.id.point_ll);
        point1Tv = (TextView) findViewById(R.id.point1_tv);
        point2Tv = (TextView) findViewById(R.id.point2_tv);
        point3Tv = (TextView) findViewById(R.id.point3_tv);
        mBtn = (Button) findViewById(R.id.enter_btn);
        point1Tv.setBackgroundColor(getResources().getColor(R.color.btn_blue_pressed));
        point2Tv.setBackgroundColor(getResources().getColor(R.color.gray_pressed));
        point3Tv.setBackgroundColor(getResources().getColor(R.color.gray_pressed));
        // mViewPager.setOnPageChangeListener(this);
        imageView1 = new ImageView(this);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.android_01, imageView1);
        imageView2 = new ImageView(this);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.android_02, imageView2);
        imageView3 = new ImageView(this);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.android_03, imageView3);
        imageView4 = new ImageView(this);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.android_04, imageView4);

        imageView5 = new ImageView(this);
        ImageLoaderUtil.displayImage("drawable://" + R.drawable.android_05, imageView5);


        mList.add(imageView1);
        mList.add(imageView2);
        mList.add(imageView3);
        mList.add(imageView4);
        mList.add(imageView5);

        Log.d("MyPager", "" + mList.get(0).getClass().getName() +
                mList.get(1).getClass().getName() +
                mList.get(2).getClass().getName());
        mViewPager.setAdapter(new MyPager(mList));
        mViewPager.setOffscreenPageLimit(1);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Util.isEmpty(SPManager.getInstance(GulideActivity.this).getToken())) {
                    intent = new Intent(GulideActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(GulideActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
        mViewPager.setCurrentItem(0);
    }

    class MyPager extends PagerAdapter {
        private ArrayList<ImageView> mClist;

        public MyPager(ArrayList<ImageView> list) {
            this.mClist = list;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mClist.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mClist.get(position));
        }

        @Override
        public int getCount() {
            return mClist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }
}
