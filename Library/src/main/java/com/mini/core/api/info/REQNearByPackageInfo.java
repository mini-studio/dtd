package com.mini.core.api.info;

import com.mini.activity.comm.MNCityPickerActivity;
import com.mini.core.api.data.City;
import com.mini.core.api.data.Location;

import org.mini.frame.http.request.info.MiniREQBaseInfo;
import org.mini.frame.toolkit.location.MiniLocationManager;
import org.mini.frame.toolkit.location.MiniTLocationManager;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 获取附近的订单
 */
public class REQNearByPackageInfo extends MiniREQBaseInfo {

    City city;

    public REQNearByPackageInfo(City city) {
        this.city = city;
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
        MiniTLocationManager locationManager = MiniTLocationManager.instanceOf();
        Double longitude = locationManager.getLongitude();
        Double latitude = locationManager.getLatitude();
        if (latitude != null && longitude != null) {
            stringBuilder.append("/").append(longitude).append("/").append(latitude);
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
