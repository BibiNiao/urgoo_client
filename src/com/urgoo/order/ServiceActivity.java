package com.urgoo.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.counselor.biz.CounselorData;
import com.urgoo.counselor.model.ServiceLongList;
import com.urgoo.order.event.OrderEvent;
import com.urgoo.order.model.OrderServiceEntity;
import com.zw.express.tool.Util;

import java.util.ArrayList;

public class ServiceActivity extends ActivityBase implements View.OnClickListener {

    private LinearLayout ll_breakss;
    private ListView serverList;
    private String servicePrice;
    private TextView tv_beizhu;
    private String extraService;
    private String serviceId;
    private String counselorId;
    private TextView tv_note;
    private ArrayList<ServiceLongList> mServiceLongList;
    private OrderServiceEntity orderServiceEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        extraService = getIntent().getStringExtra("extraService");
        mServiceLongList = new ArrayList<>();
        mServiceLongList.clear();
        mServiceLongList.addAll(CounselorData.getArray());

        initview();
        if (!Util.isEmpty(extraService)) {
            for (int i = 1; i < 30; i++) {
                extraService = extraService.replace(i + ".", "");
            }
        }

        Log.d(" 服务方式 ", "" + mServiceLongList);

        //同理，有备注，显示
        if (!Util.isEmpty(extraService)) {
            tv_note.setVisibility(View.VISIBLE);
            tv_beizhu.setVisibility(View.VISIBLE);
            tv_note.setText(extraService);
        }
        tv_note.setText(extraService);
        serverList.setAdapter(new ServiceLongListAdapter());
        getListHeight(serverList);
    }

    private void initview() {
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);
        ll_breakss = (LinearLayout) findViewById(R.id.ll_breakss);
        serverList = (ListView) findViewById(R.id.serverList);
        ll_breakss.setOnClickListener(this);
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_breakss:
                finish();
                break;
//            case R.id.tv_order:
//                if (orderServiceEntity != null) {
//                    showLoadingDialog();
//                    orderServiceEntity.setServiceId(serviceId);
//                    orderServiceEntity.setCounselorId(counselorId);
//                    orderServiceEntity.setServicePrice(servicePrice);
//                    ServerManager.getInstance(this).getPayTimeDetail(this, orderServiceEntity.getType(), orderServiceEntity.getGrade());
//                }
//                break;
        }
    }
//
//    @Override
//    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
//        super.onResponseSuccess(eventCode, result);
//        dismissLoadingDialog();
//        switch (eventCode) {
//            case EventCodeGetPayTimeDetail:
//                try {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("orderTimeLine", result.get("body").toString());
//                    bundle.putParcelable("orderInfo", orderServiceEntity);
//                    Util.openActivityWithBundle(this, OrderTimeLineActivity.class, bundle);
//                } catch (JSONException mE) {
//                    mE.printStackTrace();
//                }
//                break;
//
//        }
//    }

    //  服务内容 adapter
    private class ServiceLongListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mServiceLongList.size();
        }

        @Override
        public Object getItem(int position) {
            return mServiceLongList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(ServiceActivity.this).inflate(R.layout.conunselor_service_item2, parent, false);
                viewHolder.tv_content_serverName = (TextView) convertView.findViewById(R.id.tv_content_serverName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_content_serverName.setText(mServiceLongList.get(position).getDetailed());
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_content_serverName;
        }
    }

    public void onEvent(OrderEvent event) {
        finish();
    }

}
