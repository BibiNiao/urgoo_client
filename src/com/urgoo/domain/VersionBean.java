package com.urgoo.domain;

/**
 * Created by lijie on 2016/5/5.
 */
public class VersionBean {
//    "description": "测试版", 版本描述
//    "deviceType": 2, 设备类型 1：ios 2：Android
//    "downloadUrl": "http://localhost:8080/urgoo/001/001/admin/gotoAdminTop", 下载地址
//    "insertDateTime": "2016-05-05T10:59:19",
//            "insertUserId": 62,
//            "roleType": 2, 用户类型 1：客户端 2：顾问端
//    "status": 1,
//            "updateDateTime": "2016-05-05T11:02:07",
//            "updateUserId": 62,
//            "vesionCode": "1", 版本号
//    "vesionInfoId": 4,
//            "vesionName": "1.0" 版本名称
    private String vesionDescription;
    private String deviceType;
    private String isForce;

    @Override
    public String toString() {
        return "VersionBean{" +
                "vesionDescription='" + vesionDescription + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", insertDateTime='" + insertDateTime + '\'' +
                ", insertUserId='" + insertUserId + '\'' +
                ", roleType='" + roleType + '\'' +
                ", status='" + status + '\'' +
                ", updateDateTime='" + updateDateTime + '\'' +
                ", updateUserId='" + updateUserId + '\'' +
                ", vesionCode='" + vesionCode + '\'' +
                ", vesionInfoId='" + vesionInfoId + '\'' +
                ", vesionName='" + vesionName + '\'' +
                ", isForce='" + isForce + '\'' +
                '}';
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String mIsForce) {
        isForce = mIsForce;
    }

    public String getVesionName() {
        return vesionName;
    }

    public void setVesionName(String vesionName) {
        this.vesionName = vesionName;
    }

    public String getVesionInfoId() {
        return vesionInfoId;
    }

    public void setVesionInfoId(String vesionInfoId) {
        this.vesionInfoId = vesionInfoId;
    }

    public String getVesionCode() {
        return vesionCode;
    }

    public void setVesionCode(String vesionCode) {
        this.vesionCode = vesionCode;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(String insertDateTime) {
        this.insertDateTime = insertDateTime;
    }

    public String getVesionDescription() {
        return vesionDescription;
    }

    public void setVesionDescription(String vesionDescription) {
        this.vesionDescription = vesionDescription;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    private String downloadUrl;
    private String insertDateTime;
    private String insertUserId;
    private String roleType;
    private String status;
    private String updateDateTime;
    private String updateUserId;
    private String vesionCode;
    private String vesionInfoId;
    private String vesionName;


}
