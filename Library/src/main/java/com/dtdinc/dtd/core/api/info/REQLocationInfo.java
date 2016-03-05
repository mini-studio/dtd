package com.dtdinc.dtd.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQLocationInfo extends MiniREQBaseInfo {

    Double latitude;
    Double longitude;

    public REQLocationInfo(Double longitude, Double latitude) {
        super(POST);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder("/gindex/location");
        if (latitude != null) {
            stringBuilder.append("/").append(latitude);
        }
        if (longitude != null) {
            stringBuilder.append("/").append(longitude);
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
