package com.urgoo.account.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/8/15.
 */
public class SurveyContent implements Parcelable {
    private String anId;
    private String name;

    public SurveyContent(Parcel in) {
        anId = in.readString();
        name = in.readString();
    }

    public static final Creator<SurveyContent> CREATOR = new Creator<SurveyContent>() {
        @Override
        public SurveyContent createFromParcel(Parcel in) {
            return new SurveyContent(in);
        }

        @Override
        public SurveyContent[] newArray(int size) {
            return new SurveyContent[size];
        }
    };

    public SurveyContent() {

    }

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(anId);
        dest.writeString(name);
    }
}
