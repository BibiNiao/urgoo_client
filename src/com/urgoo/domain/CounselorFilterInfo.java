package com.urgoo.domain;

import com.urgoo.counselor.model.PositonList;
import com.urgoo.counselor.model.SurpriseList;

import java.util.List;

/**
 * Created by bb on 2016/7/20.
 */
public class CounselorFilterInfo {
    private List<SurpriseList> surpriseList;
    private List<CounselorExperanceList> counselorExperanceList;
    private List<ChineselevelList> chineselevelList;
    private List<ServiceList> serviceList;
    private List<ServiceModeList> serviceModeList;
    private List<OrgList> orgList;
    private List<CountryTypeList> countryTypeList;
    private List<GenderList> genderList;
    private List<PositonList> positonList;

    public List<PositonList> getPositonList() {
        return positonList;
    }

    public void setPositonList(List<PositonList> positonList) {
        this.positonList = positonList;
    }

    public List<SurpriseList> getSurpriseList() {
        return surpriseList;
    }

    public void setSurpriseList(List<SurpriseList> surpriseList) {
        this.surpriseList = surpriseList;
    }

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
