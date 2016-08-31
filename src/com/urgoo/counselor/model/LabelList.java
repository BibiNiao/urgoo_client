package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class LabelList {
    private String lableCnName;

    @Override
    public String toString() {
        return "LabelListBean{" +
                "lableCnName='" + lableCnName + '\'' +
                '}';
    }

    public String getLableCnName() {
        return lableCnName;
    }

    public void setLableCnName(String mLableCnName) {
        lableCnName = mLableCnName;
    }
}
