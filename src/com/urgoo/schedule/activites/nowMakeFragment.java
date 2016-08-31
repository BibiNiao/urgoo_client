package com.urgoo.schedule.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.urgoo.adapter.NowMakeAdapter;
import com.urgoo.client.R;
import com.urgoo.db.MakeManager;
import com.urgoo.domain.NowMakeBean;
import java.util.ArrayList;

/**
 * Created by duanfei on 2016/6/14.
 */
public class nowMakeFragment extends Fragment {
    private static final String TAG = "make_Fragment : ";
    protected View mRootView;
    protected ListView mListView;
    public int type;
    private ArrayList<NowMakeBean.AdvanceBean> mTobeconfir = new ArrayList<NowMakeBean.AdvanceBean>();
    private NowMakeAdapter mTobeconfirAdapter;
    private MakeManager mMakeManager;
    private String counselorId;
    private RelativeLayout RelativeLayout_rili;

    public void setType(int mType) {
        type = mType;
        Log.d(TAG, "setType: " + type);
    }

    public void setId(String mCounselorId) {
        counselorId = mCounselorId;
        Log.d(TAG, "counselorId: " + counselorId);
    }
    public void setBean(ArrayList<NowMakeBean.AdvanceBean> mCounselorId) {
        mTobeconfir = mCounselorId;
        Log.d(TAG, "mTobeconfir: " + mTobeconfir.toString());
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.schedule_time, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) mRootView.findViewById(R.id.schedule_ListView);
        RelativeLayout_rili = (RelativeLayout) mRootView.findViewById(R.id.RelativeLayout_rili);
        mMakeManager = new MakeManager(getActivity());

        if (mTobeconfir.get(type).getBusyDay().equals("red")) {
            RelativeLayout_rili.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else if (mTobeconfir.get(type).getBusyDay().equals("green")) {
            RelativeLayout_rili.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mTobeconfirAdapter = new NowMakeAdapter(getActivity(), mTobeconfir.get(type).getTimeList(), type, mMakeManager, mTobeconfir);
            mListView.setAdapter(mTobeconfirAdapter);
            getListHeight(mListView);
        }
        Log.d(TAG, "现在是 : " + mTobeconfir.get(type).getDataYanlong());

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
