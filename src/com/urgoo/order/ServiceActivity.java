package com.urgoo.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.counselor.biz.CounselorData;
import com.urgoo.counselor.model.ServiceLongList;
import com.zw.express.tool.Util;

import java.util.ArrayList;

public class ServiceActivity extends NavToolBarActivity {

    private ListView serverList;
    private TextView tv_beizhu;
    private String extraService;
    private TextView tv_note;
    private ArrayList<ServiceLongList> mServiceLongList = new ArrayList<>();

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_service, null);
        setNavTitleText("具体服务");
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tv_note = (TextView) view.findViewById(R.id.tv_note);
        tv_beizhu = (TextView) view.findViewById(R.id.tv_beizhu);
        serverList = (ListView) view.findViewById(R.id.serverList);

        extraService = getIntent().getStringExtra("extraService");
        mServiceLongList.clear();
        mServiceLongList.addAll(CounselorData.getArray());

        if (!Util.isEmpty(extraService)) {
            for (int i = 1; i < 30; i++) {
                extraService = extraService.replace(i + ".", "");
            }
        }

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
}
