package com.dtdinc.dtd.core.api.info;

import com.dtdinc.dtd.app.CESystem;

import org.mini.frame.http.request.info.MiniREQBaseInfo;
import org.mini.frame.toolkit.location.MiniLocationManager;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQPackageDetailInfo extends MiniREQBaseInfo {

    String id;
    Double longitude, latitude;

    public REQPackageDetailInfo(String id) {
        this.id = id;
        this.longitude = CESystem.sharedInstance().getLongitude();
        this.latitude = CESystem.sharedInstance().getLatitude();
    }

    @Override
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder("/gindex/order_detail/");
        stringBuilder.append(this.id);
        double lng = MiniLocationManager.shard().longitude();
        if (lng != -1) {
            stringBuilder.append("/").append(lng);
        }
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
