package com.urgoo.order;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.order.adapter.OrderTimeLineAdatper;
import com.urgoo.order.event.OrderEvent;
import com.urgoo.order.model.OrderServiceEntity;
import com.urgoo.order.model.OrderTimeLine;
import com.urgoo.view.TimeLineListView;
import com.zw.express.tool.Util;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/8/11.
 */
public class OrderTimeLineActivity extends ActivityBase implements View.OnClickListener {
    private OrderTimeLine orderTimeLine;
    private OrderServiceEntity orderServiceEntity;
    private OrderTimeLineAdatper adatper;
    private TimeLineListView listView;
    private String data;
    private TextView tvPrice;
    private View line;
    private String orderString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_timeline);
        EventBus.getDefault().register(this);
        orderString = getIntent().getStringExtra("orderTimeLine");
        orderServiceEntity = getIntent().getParcelableExtra("orderInfo");

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        orderTimeLine = gson.fromJson(orderString, new TypeToken<OrderTimeLine>() {
        }.getType());

        initView();
    }

    public void initView() {
        findViewById(R.id.btn_read).setOnClickListener(this);
        listView = (TimeLineListView) findViewById(R.id.lv_timeline);
        line = findViewById(R.id.line2);
//        listView.setDividerHeight(0);
        findViewById(R.id.back).setOnClickListener(this);
        data = orderTimeLine.getWorkDay() + "个工作日";
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPrice.setText("客户支付平台费用后" + data + getString(R.string.order_timeline_price, orderTimeLine.getWorkDayPercent()));
        SpannableStringBuilder builder = new SpannableStringBuilder(tvPrice.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.common_botton_bar_blue));
        builder.setSpan(redSpan, 9, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrice.setText(builder);

        Log.d("uuuu ", "getPayTimeList: " + orderTimeLine.getPayTimeList());
        if (orderTimeLine.getPayTimeList() != null) {
            adatper = new OrderTimeLineAdatper(this, orderTimeLine.getPayTimeList());
            listView.setAdapter(adatper);
            getListHeight(listView);
        } else {
            listView.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_read:
                Bundle bundle = new Bundle();
                bundle.putParcelable("orderInfo", orderServiceEntity);
                Util.openActivityWithBundle(this, LinkActivity.class, bundle);
                break;
        }
    }

    //  计算list实际高度
    private void getListHeight(ListView mlistview) {
        ListAdapter listAdapter = mlistview.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, mlistview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mlistview.getLayoutParams();
        params.height = totalHeight
                + (mlistview.getDividerHeight() * (listAdapter.getCount() - 1));
        mlistview.setLayoutParams(params);
    }

    public void onEvent(OrderEvent event) {
        finish();
    }
}
