package com.urgoo.counselor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/10/25.
 */
public class SearchData implements Parcelable {
    private String countryType;
    private int countryId;
    private String gender;
    private int genderId;
    private String serviceType;
    private int serviceId;
    private String chineseLevelType;
    private int chineseLevelId;
    private String counselorExperanceType;
    private int counselorExperanceId;
    private String organizationType;
    private int organizationId;
    private String positionType;
    private int positionId;
    private String surpriseType;
    private int surpriseId;

    public SearchData(Parcel in) {
        countryType = in.readString();
        countryId = in.readInt();
        gender = in.readString();
        genderId = in.readInt();
        serviceType = in.readString();
        serviceId = in.readInt();
        chineseLevelType = in.readString();
        chineseLevelId = in.readInt();
        counselorExperanceType = in.readString();
        counselorExperanceId = in.readInt();
        organizationType = in.readString();
        organizationId = in.readInt();
        positionType = in.readString();
        positionId = in.readInt();
        surpriseType = in.readString();
        surpriseId = in.readInt();
    }

    public static final Creator<SearchData> CREATOR = new Creator<SearchData>() {
        @Override
        public SearchData createFromParcel(Parcel in) {
            return new SearchData(in);
        }

        @Override
        public SearchData[] newArray(int size) {
            return new SearchData[size];
        }
    };

    public SearchData() {

    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getChineseLevelId() {
        return chineseLevelId;
    }

    public void setChineseLevelId(int chineseLevelId) {
        this.chineseLevelId = chineseLevelId;
    }

    public int getCounselorExperanceId() {
        return counselorExperanceId;
    }

    public void setCounselorExperanceId(int counselorExperanceId) {
        this.counselorExperanceId = counselorExperanceId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getSurpriseId() {
        return surpriseId;
    }

    public void setSurpriseId(int surpriseId) {
        this.surpriseId = surpriseId;
    }

    public void clearData() {
        setCountryType("");
        setCountryId(0);
        setChineseLevelType("");
        setChineseLevelId(0);
        setGender("");
        setGenderId(0);
        setServiceType("");
        setServiceId(0);
        setSurpriseType("");
        setSurpriseId(0);
        setCounselorExperanceType("");
        setCounselorExperanceId(0);
        setPositionType("");
        setPositionId(0);
        setOrganizationType("");
        setOrganizationId(0);
    }

    public String getCountryType() {
        return countryType;
    }

    public void setCountryType(String countryType) {
        this.countryType = countryType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getChineseLevelType() {
        return chineseLevelType;
    }

    public void setChineseLevelType(String chineseLevelType) {
        this.chineseLevelType = chineseLevelType;
    }

    public String getCounselorExperanceType() {
        return counselorExperanceType;
    }

    public void setCounselorExperanceType(String counselorExperanceType) {
        this.counselorExperanceType = counselorExperanceType;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getSurpriseType() {
        return surpriseType;
    }

    public void setSurpriseType(String surpriseType) {
        this.surpriseType = surpriseType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryType);
        dest.writeInt(countryId);
        dest.writeString(gender);
        dest.writeInt(genderId);
        dest.writeString(serviceType);
        dest.writeInt(serviceId);
        dest.writeString(chineseLevelType);
        dest.writeInt(chineseLevelId);
        dest.writeString(counselorExperanceType);
        dest.writeInt(counselorExperanceId);
        dest.writeString(organizationType);
        dest.writeInt(organizationId);
        dest.writeString(positionType);
        dest.writeInt(positionId);
        dest.writeString(surpriseType);
        dest.writeInt(surpriseId);
    }
}
