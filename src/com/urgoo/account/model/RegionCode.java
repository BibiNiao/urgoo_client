package com.urgoo.account.model;

/**
 * @author mark
 */
public class RegionCode {
    /**
     * 区号代码
     */
    public String code;
    /**
     * 地区中文名
     */
    public String name;
    /**
     * 地区中文名拼音
     */
    public String countryNamePY;
    /**
     * 拼音首字母
     */
    public String firstLetterPY;
    public static final int ITEM = 0;  //类型：组
    public static final int SECTION = 1; //类型：内容
    public int type;

    public RegionCode() {

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryNamePY() {
        return countryNamePY;
    }

    public String getFirstLetterPY() {
        return firstLetterPY;
    }

    public void setCountryNamePY(String countryNamePY) {
        this.countryNamePY = countryNamePY;
    }

    public void setFirstLetterPY(String firstLetterPY) {
        this.firstLetterPY = firstLetterPY;
    }

    public RegionCode(String code, String name, String countryNamePY, String firstLetterPY) {
        this.code = code;
        this.name = name;
        this.countryNamePY = countryNamePY;
        this.firstLetterPY = firstLetterPY;
    }
}
