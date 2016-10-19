package com.urgoo.account.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/9/28.
 */
public class UserBean implements Parcelable {
    /**
     * 英文标签
     */
    private String lableNameEn;
    /**
     * 年级
     */
    private String gradeName;
    /**
     * 标签
     */
    private String lableNameCn;
    /**
     *  是否填写考试成绩    2 未填写   1 填写
     */
    private String examType;
    /**
     * 是否填写年级   2 未填写  1 填写 访问gradeName
     */
    private String gradeStatus;
    /**
     * 是否填写学术奖项  2 未填写  1 填写
     */
    private String academicAwardsType;
    /**
     * 是否填写标签    2 未填写   1 填写 访问lableNameCn
     */
    private String lableStatus;
    /**
     * 是否填写就读学校  2  未填写  1填写 访问schoolName
     */
    private String schoolStatus;
    /**
     * 是否填写意向国家  2未填写  1 填写 访问targetCountryIdName
     */
    private String targetCountryIdStatus;
    /**
     * 意向国家
     */
    private String targetCountryIdName;
    /**
     * 是否填写课外活动   2 未填写   1 填写
     */
    private String extracurricularActivitiesStatus;
    /**
     * 校内成绩
     */
    private String schoolScoreName;
    /**
     * 就读学校
     */
    private String schoolName;
    /**
     * 是否填写校内成绩
     */
    private String schoolScoreStatus;
    /**
     * 课程类型
     */
    private String schoolCourse;

    protected UserBean(Parcel in) {
        lableNameEn = in.readString();
        gradeName = in.readString();
        lableNameCn = in.readString();
        examType = in.readString();
        gradeStatus = in.readString();
        academicAwardsType = in.readString();
        lableStatus = in.readString();
        schoolStatus = in.readString();
        targetCountryIdStatus = in.readString();
        targetCountryIdName = in.readString();
        extracurricularActivitiesStatus = in.readString();
        schoolScoreName = in.readString();
        schoolName = in.readString();
        schoolScoreStatus = in.readString();
        schoolCourse = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public String getLableNameEn() {
        return lableNameEn;
    }

    public void setLableNameEn(String lableNameEn) {
        this.lableNameEn = lableNameEn;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getLableNameCn() {
        return lableNameCn;
    }

    public void setLableNameCn(String lableNameCn) {
        this.lableNameCn = lableNameCn;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getGradeStatus() {
        return gradeStatus;
    }

    public void setGradeStatus(String gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    public String getAcademicAwardsType() {
        return academicAwardsType;
    }

    public void setAcademicAwardsType(String academicAwardsType) {
        this.academicAwardsType = academicAwardsType;
    }

    public String getLableStatus() {
        return lableStatus;
    }

    public void setLableStatus(String lableStatus) {
        this.lableStatus = lableStatus;
    }

    public String getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public String getTargetCountryIdStatus() {
        return targetCountryIdStatus;
    }

    public void setTargetCountryIdStatus(String targetCountryIdStatus) {
        this.targetCountryIdStatus = targetCountryIdStatus;
    }

    public String getTargetCountryIdName() {
        return targetCountryIdName;
    }

    public void setTargetCountryIdName(String targetCountryIdName) {
        this.targetCountryIdName = targetCountryIdName;
    }

    public String getExtracurricularActivitiesStatus() {
        return extracurricularActivitiesStatus;
    }

    public void setExtracurricularActivitiesStatus(String extracurricularActivitiesStatus) {
        this.extracurricularActivitiesStatus = extracurricularActivitiesStatus;
    }

    public String getSchoolScoreName() {
        return schoolScoreName;
    }

    public void setSchoolScoreName(String schoolScoreName) {
        this.schoolScoreName = schoolScoreName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolScoreStatus() {
        return schoolScoreStatus;
    }

    public void setSchoolScoreStatus(String schoolScoreStatus) {
        this.schoolScoreStatus = schoolScoreStatus;
    }

    public String getSchoolCourse() {
        return schoolCourse;
    }

    public void setSchoolCourse(String schoolCourse) {
        this.schoolCourse = schoolCourse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lableNameEn);
        dest.writeString(gradeName);
        dest.writeString(lableNameCn);
        dest.writeString(examType);
        dest.writeString(gradeStatus);
        dest.writeString(academicAwardsType);
        dest.writeString(lableStatus);
        dest.writeString(schoolStatus);
        dest.writeString(targetCountryIdStatus);
        dest.writeString(targetCountryIdName);
        dest.writeString(extracurricularActivitiesStatus);
        dest.writeString(schoolScoreName);
        dest.writeString(schoolName);
        dest.writeString(schoolScoreStatus);
        dest.writeString(schoolCourse);
    }
}
