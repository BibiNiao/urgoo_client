package com.urgoo.main.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;

/**
 * Created by lijie on 2016/5/30.
 */
public class AboutUrgoo extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(imageView);
        setContentView(scrollView);
        imageView.setBackgroundResource(R.drawable.abouturgoo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUrgoo.this.finish();
            }
        });
    }
}
