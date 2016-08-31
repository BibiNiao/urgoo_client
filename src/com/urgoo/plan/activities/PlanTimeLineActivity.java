package com.urgoo.plan.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.net.StringRequestCallBack;
import com.urgoo.plan.adapter.PlanFinishAdapter;
import com.urgoo.plan.adapter.PlanNostartAdapter;
import com.urgoo.view.TimeLineListView;

/**
 * Created by bb on 2016/8/8.
 */
public class PlanTimeLineActivity extends ActivityBase implements StringRequestCallBack {
    private ListView lvFinish;
    private TimeLineListView lvNostart;
    private PlanFinishAdapter finishAdapter;
    private PlanNostartAdapter nostartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantimeline);
        initViews();
    }

    /**
     * 动态设置listview的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
            ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
            listView.setLayoutParams(params);
        }
        listView.setDividerHeight(0);
    }

    private void initViews() {
        lvFinish = (ListView) findViewById(R.id.lv_finish);
        View footview = this.getLayoutInflater().inflate(R.layout.timeline_footerview, null);
        lvNostart = (TimeLineListView) footview.findViewById(R.id.lv_nostart);
        nostartAdapter = new PlanNostartAdapter(this);
        finishAdapter = new PlanFinishAdapter(this);

        lvNostart.setAdapter(nostartAdapter);
        setListViewHeightBasedOnChildren(lvNostart);
        lvFinish.addFooterView(footview);
        lvFinish.setAdapter(finishAdapter);
        lvFinish.setDividerHeight(0);
    }
}
