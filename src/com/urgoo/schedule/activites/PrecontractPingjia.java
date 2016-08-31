package com.urgoo.schedule.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.message.activities.BaseActivity;
import com.urgoo.message.activities.ChatActivity;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;

/**
 * Created by lijie on 2016/6/1.
 */
public class PrecontractPingjia extends BaseActivity {
    private static final String TAG = "mtag";
    LinearLayout breaks;
    Button submit_btn, submits_btn;
    private String advanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingjia);
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("advanceId") != null) {
            advanceId = mIntent.getStringExtra("advanceId");
        }

        breaks = (LinearLayout) findViewById(R.id.back);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submits_btn = (Button) findViewById(R.id.submits_btn);

        breaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 立即聘用顾问");

                Intent intent = new Intent(PrecontractPingjia.this, BaseWebViewActivity.class);
                String URT = ZWConfig.ACTION_PINGYONG + "&counselorId=" + advanceId;
                intent.putExtra(BaseWebViewFragment.EXTRA_URL, URT);
                startActivity(intent);
            }
        });
        submits_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 还有疑问？去找优宝问问吧~");
                startActivity(new Intent(PrecontractPingjia.this, ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
            }
        });
    }
}
