package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by urgoo_01 on 2016/7/16.
 */
public class GuwenInfo {

    private ArrayList<ExperienceListBean> experienceList;
    private ArrayList<DetailSubListBean> DetailSubList;
    private ArrayList<ServiceLongListBean> serviceLongList;
    private ArrayList<CounselorServiceListBean> counselorServiceList;
    private ArrayList<LabelListBean> labelList;
    private ArrayList<WorksBean> works;
    private ArrayList<EduList> eduList;
    private String habitualResidence;
    private String attentionCount;
    private String userHxCode;
    private String successCase;
    private String counselorId;
    private String counselorReadCount;
    private String cnName;
    private String serviceMode;
    private String organization;
    private String enName;
    private String advanceCount;
    private String workYear;
    private String selfBio;
    private String levelEducation;
    private String extraService;
    private String slogan;
    private String educationBg;
    private String countyName;
    private String requires;
    private String serviceCounted;
    private String isAttention;
    private String serviceTotal;
    private String successCaseTranz;
    private String selfBioTranz;
    private String requiresTranz;
    private String liveId;
    private String isAdvanceRelation;
    private String advanceId;
    private String status;
    private String type;

    public GuwenInfo(
            String mHabitualResidence,
            String mAttentionCount,
            String mUserHxCode,
            String mSuccessCase,
            String mCounselorId,
            String mCounselorReadCount,
            String mCnName,
            String mServiceMode,
            String mOrganization,
            String mEnName,
            String mAdvanceCount,
            String mWorkYear,
            String mSelfBio,
            String mLevelEducation,
            String mExtraService,
            String mSlogan,
            String mEducationBg,
            String mCountyName,
            String mRequires,
            String serviceCounteda,
            String isAttentiona,
            String serviceTotala,
            String successCaseTranza,
            String selfBioTranza,
            String requiresTranza,
            String liveIda,
            String isAdvanceRelationa,
            String advanceIda,
            String typea,
            String statusa
    ) {
        habitualResidence = mHabitualResidence;
        attentionCount = mAttentionCount;
        userHxCode = mUserHxCode;
        successCase = mSuccessCase;
        counselorId = mCounselorId;
        counselorReadCount = mCounselorReadCount;
        cnName = mCnName;
        serviceMode = mServiceMode;
        organization = mOrganization;
        enName = mEnName;
        advanceCount = mAdvanceCount;
        workYear = mWorkYear;
        selfBio = mSelfBio;
        levelEducation = mLevelEducation;
        extraService = mExtraService;
        slogan = mSlogan;
        educationBg = mEducationBg;
        countyName = mCountyName;
        requires = mRequires;
        serviceCounted = serviceCounteda;
        isAttention = isAttentiona;
        serviceTotal = serviceTotala;
        successCaseTranz = successCaseTranza;
        selfBioTranz = selfBioTranza;
        requiresTranz = requiresTranza;
        liveId = liveIda;
        isAdvanceRelation = isAdvanceRelationa;
        advanceId = advanceIda;
        type = typea;
        status = statusa;
    }

    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String mStatus) {
        status = mStatus;
    }

    public String getIsAdvanceRelation() {
        return isAdvanceRelation;
    }

    public void setIsAdvanceRelation(String mIsAdvanceRelation) {
        isAdvanceRelation = mIsAdvanceRelation;
    }

    public String getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(String mAdvanceId) {
        advanceId = mAdvanceId;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String mLiveId) {
        liveId = mLiveId;
    }

    public String getRequiresTranz() {
        return requiresTranz;
    }

    public void setRequiresTranz(String mRequiresTranz) {
        requiresTranz = mRequiresTranz;
    }

    public String getSuccessCaseTranz() {
        return successCaseTranz;
    }

    public void setSuccessCaseTranz(String mSuccessCaseTranz) {
        successCaseTranz = mSuccessCaseTranz;
    }

    public String getSelfBioTranz() {
        return selfBioTranz;
    }

    public void setSelfBioTranz(String mSelfBioTranz) {
        selfBioTranz = mSelfBioTranz;
    }

    public String getServiceCounted() {
        return serviceCounted;
    }

    public void setServiceCounted(String mServiceCounted) {
        serviceCounted = mServiceCounted;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String mIsAttention) {
        isAttention = mIsAttention;
    }

    public String getServiceTotal() {
        return serviceTotal;
    }

    public void setServiceTotal(String mServiceTotal) {
        serviceTotal = mServiceTotal;
    }

    @Override
    public String toString() {
        return "GuwenInfo{" +
                "habitualResidence='" + habitualResidence + '\'' +
                ", attentionCount='" + attentionCount + '\'' +
                ", userHxCode='" + userHxCode + '\'' +
                ", successCase='" + successCase + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", counselorReadCount='" + counselorReadCount + '\'' +
                ", cnName='" + cnName + '\'' +
                ", serviceMode='" + serviceMode + '\'' +
                ", organization='" + organization + '\'' +
                ", enName='" + enName + '\'' +
                ", advanceCount='" + advanceCount + '\'' +
                ", workYear='" + workYear + '\'' +
                ", selfBio='" + selfBio + '\'' +
                ", levelEducation='" + levelEducation + '\'' +
                ", extraService='" + extraService + '\'' +
                ", slogan='" + slogan + '\'' +
                ", educationBg='" + educationBg + '\'' +
                ", countyName='" + countyName + '\'' +
                ", requires='" + requires + '\'' +
                ", serviceCounted='" + serviceCounted + '\'' +
                ", isAttention='" + isAttention + '\'' +
                ", serviceTotal='" + serviceTotal + '\'' +
                ", successCaseTranz='" + successCaseTranz + '\'' +
                ", selfBioTranz='" + selfBioTranz + '\'' +
                ", requiresTranz='" + requiresTranz + '\'' +
                ", liveId='" + liveId + '\'' +
                '}';
    }

    public String getHabitualResidence() {
        return habitualResidence;
    }

    public void setHabitualResidence(String habitualResidence) {
        this.habitualResidence = habitualResidence;
    }

    public String getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(String attentionCount) {
        this.attentionCount = attentionCount;
    }

    public String getUserHxCode() {
        return userHxCode;
    }

    public void setUserHxCode(String userHxCode) {
        this.userHxCode = userHxCode;
    }

    public String getSuccessCase() {
        return successCase;
    }

    public void setSuccessCase(String successCase) {
        this.successCase = successCase;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public String getCounselorReadCount() {
        return counselorReadCount;
    }

    public void setCounselorReadCount(String counselorReadCount) {
        this.counselorReadCount = counselorReadCount;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getAdvanceCount() {
        return advanceCount;
    }

    public void setAdvanceCount(String advanceCount) {
        this.advanceCount = advanceCount;
    }

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getSelfBio() {
        return selfBio;
    }

    public void setSelfBio(String selfBio) {
        this.selfBio = selfBio;
    }

    public String getLevelEducation() {
        return levelEducation;
    }

    public void setLevelEducation(String levelEducation) {
        this.levelEducation = levelEducation;
    }

    public String getExtraService() {
        return extraService;
    }

    public void setExtraService(String extraService) {
        this.extraService = extraService;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getEducationBg() {
        return educationBg;
    }

    public void setEducationBg(String educationBg) {
        this.educationBg = educationBg;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getRequires() {
        return requires;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }


    public static class ExperienceListBean {
        private int experienceId;
        private String detailed;
        private String endDate;
        private String companyName;
        private String position;
        private String startDate;

        public int getExperienceId() {
            return experienceId;
        }

        public void setExperienceId(int experienceId) {
            this.experienceId = experienceId;
        }

        public String getDetailed() {
            return detailed;
        }

        public void setDetailed(String detailed) {
            this.detailed = detailed;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        @Override
        public String toString() {
            return "ExperienceListBean{" +
                    "experienceId=" + experienceId +
                    ", detailed='" + detailed + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", position='" + position + '\'' +
                    ", startDate='" + startDate + '\'' +
                    '}';
        }
    }

    public static class DetailSubListBean {
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

    public static class ServiceLongListBean {
        private int serviceLongId;
        private String detailed;

        public int getServiceLongId() {
            return serviceLongId;
        }

        public void setServiceLongId(int serviceLongId) {
            this.serviceLongId = serviceLongId;
        }

        public String getDetailed() {
            return detailed;
        }

        public void setDetailed(String detailed) {
            this.detailed = detailed;
        }

        @Override
        public String toString() {
            return "ServiceLongListBean{" +
                    "serviceLongId=" + serviceLongId +
                    ", detailed='" + detailed + '\'' +
                    '}';
        }
    }

    public static class CounselorServiceListBean {
        private double servicePrice;
        private String serviceLife;
        private String serviceName;
        private String serviceid;


        public double getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(double mServicePrice) {
            servicePrice = mServicePrice;
        }

        public String getServiceid() {
            return serviceid;
        }

        public void setServiceid(String mServiceid) {
            serviceid = mServiceid;
        }

        public String getServiceLife() {
            return serviceLife;
        }

        public void setServiceLife(String mServiceLife) {
            serviceLife = mServiceLife;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String mServiceName) {
            serviceName = mServiceName;
        }

        public String getServiceId() {
            return serviceid;
        }

        public void setServiceId(String mServiceId) {
            serviceid = mServiceId;
        }

        @Override
        public String toString() {
            return "CounselorServiceListBean{" +
                    "servicePrice=" + servicePrice +
                    ", serviceLife='" + serviceLife + '\'' +
                    ", serviceName='" + serviceName + '\'' +
                    ", serviceid='" + serviceid + '\'' +
                    '}';
        }
    }

    public static class LabelListBean {
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

    public static class WorksBean {
        private String auther;
        private String title;
        private String workId;
        private String content;
        private String insertDateTime;

        public String getInsertDateTime() {
            return insertDateTime;
        }

        public void setInsertDateTime(String mInsertDateTime) {
            insertDateTime = mInsertDateTime;
        }

        public String getAuther() {
            return auther;
        }

        public void setAuther(String mAuther) {
            auther = mAuther;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String mTitle) {
            title = mTitle;
        }

        public String getWorkId() {
            return workId;
        }

        public void setWorkId(String mWorkId) {
            workId = mWorkId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String mContent) {
            content = mContent;
        }

        @Override
        public String toString() {
            return "WorksBean{" +
                    "auther='" + auther + '\'' +
                    ", title='" + title + '\'' +
                    ", workId='" + workId + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    public static class EduList {
        private int educationId;
        private String major;
        private String educationName;
        private String startTime;
        private String endTime;

        @Override
        public String toString() {
            return "EduList{" +
                    "educationId=" + educationId +
                    ", major='" + major + '\'' +
                    ", educationName='" + educationName + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }

        public int getEducationId() {
            return educationId;
        }

        public void setEducationId(int mEducationId) {
            educationId = mEducationId;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String mMajor) {
            major = mMajor;
        }

        public String getEducationName() {
            return educationName;
        }

        public void setEducationName(String mEducationName) {
            educationName = mEducationName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String mStartTime) {
            startTime = mStartTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String mEndTime) {
            endTime = mEndTime;
        }
    }

}
