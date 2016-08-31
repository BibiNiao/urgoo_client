package com.urgoo.account.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2016/8/15.
 */
public class SurveyInfo implements Parcelable {
    /**
     * 0单选 1多选
     */
    private String selectType;
    private String type;
    private String title;
    private List<SurveyContent> content;

    protected SurveyInfo(Parcel in) {
        selectType = in.readString();
        type = in.readString();
        title = in.readString();
        content = new ArrayList<>();
        in.readList(this.content, List.class.getClassLoader());
    }

    public static final Creator<SurveyInfo> CREATOR = new Creator<SurveyInfo>() {
        @Override
        public SurveyInfo createFromParcel(Parcel in) {
            return new SurveyInfo(in);
        }

        @Override
        public SurveyInfo[] newArray(int size) {
            return new SurveyInfo[size];
        }
    };

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SurveyContent> getContent() {
        return content;
    }

    public void setContent(List<SurveyContent> content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(selectType);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeList(content);
    }
}
