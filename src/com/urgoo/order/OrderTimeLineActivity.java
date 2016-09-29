package com.urgoo.order;

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
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.order.adapter.OrderTimeLineAdatper;
import com.urgoo.order.biz.ServerManager;
import com.urgoo.order.model.OrderServiceEntity;
import com.urgoo.order.model.OrderTimeLine;
import com.urgoo.view.TimeLineListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bb on 2016/8/11.
 */
public class OrderTimeLineActivity extends NavToolBarActivity {
    private OrderTimeLine orderTimeLine;
    private OrderServiceEntity orderServiceEntity;
    private OrderTimeLineAdatper adatper;
    private TimeLineListView listView;
    private String data;
    private TextView tvPrice;
    private View line;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_order_timeline, null);
        setNavTitleText("优顾支付时间预览");
        initViews(view);
        return view;
    }

    private void getTimeLine() {
        showLoadingDialog();
        ServerManager.getInstance(this).getPayTimeDetail(this, orderServiceEntity.getType(), orderServiceEntity.getGrade());
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetPayTimeDetail:
                try {
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    orderTimeLine = gson.fromJson(result.get("body").toString(), new TypeToken<OrderTimeLine>() {
                    }.getType());
                    data = orderTimeLine.getWorkDay() + "个工作日";
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void initViews(View view) {
        listView = (TimeLineListView) view.findViewById(R.id.lv_timeline);
        line = view.findViewById(R.id.line2);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        orderServiceEntity = getIntent().getParcelableExtra("orderInfo");
        getTimeLine();
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

}
