package com.urgoo.counselor.biz;

import android.util.Log;

import com.urgoo.counselor.model.ServiceLongList;

import java.util.ArrayList;

/**
 * Created by urgoo_01 on 2016/8/11.
 */
public class CounselorData {
    private static ArrayList<ServiceLongList> ServiceLis = new ArrayList<>();

    public void setCounselorServiceLis(ArrayList<ServiceLongList> mCounselorServiceLis) {
        ServiceLis.clear();
        ServiceLis.addAll(mCounselorServiceLis);
        Log.d("zzzz", "setCounselorServiceLis: " + ServiceLis.size());
    }

    public static ArrayList<ServiceLongList> getArray() {
        return ServiceLis;
    }
}
