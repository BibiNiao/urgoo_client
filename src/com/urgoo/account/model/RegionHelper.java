package com.urgoo.account.model;

import java.util.ArrayList;
import java.util.List;

/***
 * @author mark
 */
public class RegionHelper {

    /**
     * 地区列表配置
     */
    static class Config {
        /**
         * 地区列表
         */
        private static final List<RegionCode> REGIONS = new ArrayList<>();
        private static final RegionCode DEFAULT_REGION = new RegionCode("+86", "中国大陆", "zhongguo", "Z");

        static {
            REGIONS.add(new RegionCode("+61", "澳大利亚", "aodaliya", "A"));

            REGIONS.add(new RegionCode("+32", "比利时", "bilishi", "B"));

            REGIONS.add(new RegionCode("+49", "德国", "deguo", "D"));

            REGIONS.add(new RegionCode("+7", "俄罗斯", "eluosi", "E"));

            REGIONS.add(new RegionCode("+33", "法国", "faguo", "F"));

            REGIONS.add(new RegionCode("+82", "韩国", "hanguo", "H"));

            REGIONS.add(new RegionCode("+1", "加拿大", "jianada", "J"));

            REGIONS.add(new RegionCode("+1", "美国", "meiguo", "M"));

            REGIONS.add(new RegionCode("+81", "日本", "riben", "R"));
            REGIONS.add(new RegionCode("+46", "瑞典", "ruidian", "R"));
            REGIONS.add(new RegionCode("+41", "瑞士", "ruishi", "R"));

            REGIONS.add(new RegionCode("+34", "西班牙", "xibanya", "X"));
            REGIONS.add(new RegionCode("+65", "新加坡", "xinjiapo", "X"));
            REGIONS.add(new RegionCode("+64", "新西兰", "xinxilan", "X"));

            REGIONS.add(new RegionCode("+39", "意大利", "yidali", "Y"));
            REGIONS.add(new RegionCode("+91", "印度", "yingdu", "Y"));
            REGIONS.add(new RegionCode("+44", "英国", "yingguo", "Y"));

            REGIONS.add(new RegionCode("+853", "中国澳门", "zhongguoaomen", "Z"));
            REGIONS.add(DEFAULT_REGION);
            REGIONS.add(new RegionCode("+886", "中国台湾", "zhongguotaiwan", "Z"));
            REGIONS.add(new RegionCode("+852", "中国香港", "zhongguoxianggang", "Z"));
        }
    }

    /**
     * 获取所有地区列表
     *
     * @return
     */
    public static List<RegionCode> getRegions() {
        List<RegionCode> regions = new ArrayList<>();
        regions.addAll(Config.REGIONS);
        return regions;
    }

}
