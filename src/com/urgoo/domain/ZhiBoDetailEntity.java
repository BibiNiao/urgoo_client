package com.urgoo.domain;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ZhiBoDetailEntity {
    private String masterPic;
    private String des;
    private String paticipateCount;
    private String introduce;
    private String liveTimeLong;
    private String zoomNo;
    private String title;
    private String liveStartTime;
    private String liveNotice;
    private String targetId;
    private long balanceTime;
    private String video;
    private String isSign;
    private String status;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getMasterPic() {
        return masterPic;
    }

    public void setMasterPic(String masterPic) {
        this.masterPic = masterPic;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPaticipateCount() {
        return paticipateCount;
    }

    public void setPaticipateCount(String paticipateCount) {
        this.paticipateCount = paticipateCount;
    }

    public String getLiveTimeLong() {
        return liveTimeLong;
    }

    public void setLiveTimeLong(String liveTimeLong) {
        this.liveTimeLong = liveTimeLong;
    }

    public String getZoomNo() {
        return zoomNo;
    }

    public void setZoomNo(String zoomNo) {
        this.zoomNo = zoomNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(String liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public void setLiveNotice(String liveNotice) {
        this.liveNotice = liveNotice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public long getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(long balanceTime) {
        this.balanceTime = balanceTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "ZhiBoDetailEntity{" +
                "masterPic='" + masterPic + '\'' +
                ", des='" + des + '\'' +
                ", paticipateCount='" + paticipateCount + '\'' +
                ", introduce='" + introduce + '\'' +
                ", liveTimeLong='" + liveTimeLong + '\'' +
                ", zoomNo='" + zoomNo + '\'' +
                ", title='" + title + '\'' +
                ", liveStartTime='" + liveStartTime + '\'' +
                ", liveNotice='" + liveNotice + '\'' +
                ", targetId='" + targetId + '\'' +
                ", balanceTime=" + balanceTime +
                ", video='" + video + '\'' +
                ", isSign='" + isSign + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
