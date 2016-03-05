package com.dtdinc.dtd.core.api.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Wuquancheng on 15/12/6.
 */
public class City {
    private String name;
    private String code;

    public static List<String> cityList = Arrays.asList(new String[]{
            "安徽省", "北京市", "重庆市", "福建省", "甘肃省", "广东省", "广西壮族自治区",
            "贵州省", "海南省", "河北省", "河南省", "黑龙江省", "湖北省",
            "湖南省", "吉林省", "江苏省", "江西省", "辽宁省", "内蒙古自治区",
            "宁夏回族自治区", "青海省", "山东省", "山西省", "陕西省", "上海市",
            "四川省", "天津市", "西藏自治区", "新疆维吾尔自治区", "云南省", "浙江省"
    });

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static City cityWithName(String name) {
        City city = new City();
        city.setName(name);
        int index = cityList.indexOf(name);
        if (index != -1) {
            city.setCode(String.valueOf(index));
        }
        return city;
    }

    public boolean isInvalid() {
        return ("-1".equals(this.getCode()));
    }
}
