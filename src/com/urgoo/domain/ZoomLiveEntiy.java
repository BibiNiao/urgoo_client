package com.urgoo.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/7/14.
 */
public class ZoomLiveEntiy implements Parcelable {
    private String masterPic;
    private String titleSub;
    private String title;
    private String liveId;

    public String getMasterPic() {
        return masterPic;
    }

    public void setMasterPic(String masterPic) {
        this.masterPic = masterPic;
    }

    public String getTitleSub() {
        return titleSub;
    }

    public void setTitleSub(String titleSub) {
        this.titleSub = titleSub;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public static Creator<ZoomLiveEntiy> getCREATOR() {
        return CREATOR;
    }

    protected ZoomLiveEntiy(Parcel in) {
    }

    public static final Creator<ZoomLiveEntiy> CREATOR = new Creator<ZoomLiveEntiy>() {
        @Override
        public ZoomLiveEntiy createFromParcel(Parcel in) {
            return new ZoomLiveEntiy(in);
        }

        @Override
        public ZoomLiveEntiy[] newArray(int size) {
            return new ZoomLiveEntiy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
