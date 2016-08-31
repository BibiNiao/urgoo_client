package com.urgoo.domain;

import java.util.List;

/**
 * Created by bb on 2016/7/20.
 */
public class CounselorFilterInfo {
    private List<CounselorExperanceList> counselorExperanceList;
    private List<ChineselevelList> chineselevelList;
    private List<ServiceList> serviceList;
    private List<ServiceModeList> serviceModeList;
    private List<OrgList> orgList;
    private List<CountryTypeList> countryTypeList;
    private List<GenderList> genderList;

    public List<ServiceModeList> getServiceModeList() {
        return serviceModeList;
    }

    public void setServiceModeList(List<ServiceModeList> serviceModeList) {
        this.serviceModeList = serviceModeList;
    }

    public List<CounselorExperanceList> getCounselorExperanceList() {
        return counselorExperanceList;
    }

    public void setCounselorExperanceList(List<CounselorExperanceList> counselorExperanceList) {
        this.counselorExperanceList = counselorExperanceList;
    }

    public List<ChineselevelList> getChineselevelList() {
        return chineselevelList;
    }

    public void setChineselevelList(List<ChineselevelList> chineselevelList) {
        this.chineselevelList = chineselevelList;
    }

    public List<ServiceList> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceList> serviceList) {
        this.serviceList = serviceList;
    }

    public List<OrgList> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<OrgList> orgList) {
        this.orgList = orgList;
    }

    public List<CountryTypeList> getCountryTypeList() {
        return countryTypeList;
    }

    public void setCountryTypeList(List<CountryTypeList> countryTypeList) {
        this.countryTypeList = countryTypeList;
    }

    public List<GenderList> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<GenderList> genderList) {
        this.genderList = genderList;
    }
}
