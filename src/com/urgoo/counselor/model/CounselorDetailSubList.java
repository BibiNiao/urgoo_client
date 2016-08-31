package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class CounselorDetailSubList {
    private String type;
    private String url;
    private String videoPic;

    @Override
    public String toString() {
        return "DetailSubListBean{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", videoPic='" + videoPic + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String mVideoPic) {
        videoPic = mVideoPic;
    }
}
