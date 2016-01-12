package com.mini.core.api.info;

import com.mini.core.model.PostPackageInfo;

import org.mini.frame.http.request.info.MiniREQBaseInfo;
import org.mini.frame.toolkit.location.MiniLocationManager;

import java.util.HashMap;
import java.util.Map;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQPublishOrderInfo extends MiniREQBaseInfo {

    PostPackageInfo packageInfo;
    String phone;

    public REQPublishOrderInfo(String phone,PostPackageInfo packageInfo) {
        super(POST);
        this.phone = phone;
        this.packageInfo = packageInfo;
    }

    @Override
    public String getPath() {
        return "/gindex/publish_order";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("sid", WHO().getSid());
        map.put("source_user", this.packageInfo.getPosterInfo().getName());
        map.put("source_phone", this.packageInfo.getPosterInfo().getMobile());
        map.put("source_city", this.packageInfo.getPosterInfo().getCityName());
        map.put("source_address", this.packageInfo.getPosterInfo().getAddress());
        double longitude = MiniLocationManager.shard().longitude();
        double latitude = MiniLocationManager.shard().latitude();
        if (longitude != -1 && latitude != -1) {
            map.put("source_longitude", String.valueOf(longitude));
            map.put("source_latitude", String.valueOf(latitude));
        }
        map.put("source_time", this.packageInfo.getPosterInfo().getPackageTimeString());
        map.put("destination_user", this.packageInfo.getRecipientsInfo().getName());
        map.put("destination_phone", this.packageInfo.getRecipientsInfo().getMobile());
        map.put("destination_city", this.packageInfo.getRecipientsInfo().getCityName());
        map.put("destination_address", this.packageInfo.getRecipientsInfo().getAddress());
        map.put("arrive_time", this.packageInfo.getRecipientsInfo().getPackageTimeString());
        map.put("name", this.packageInfo.getPackageName());
        map.put("weight", this.packageInfo.getPackageWeight());
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
