package com.dtdinc.dtd.core.api.info;

import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.api.data.City;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 获取附近的订单
 */
public class REQNearByPackageInfo extends MiniREQBaseInfo {

    City city;
    int page;
    Double longitude;
    Double latitude;

    public REQNearByPackageInfo(City city, int page) {
        this.city = city;
        this.longitude = CESystem.sharedInstance().getLongitude();
        this.latitude = CESystem.sharedInstance().getLatitude();
        this.page = page;
    }

    @Override
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder("/gindex/index/");
        if (this.city == null) {
            stringBuilder.append("1");
        }
        else {
            stringBuilder.append(city.getCode());
        }
        if (latitude != null && longitude != null) {
            stringBuilder.append("/").append(longitude).append("/").append(latitude);
            stringBuilder.append("/").append(this.page * 10);
        }
        return stringBuilder.toString();
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        return null;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
