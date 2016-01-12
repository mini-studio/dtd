package com.mini.core.api.data;

/**
 * Created by Wuquancheng on 15/11/14.
 * 位置信息
 */
public class Location {
    String lat; //纬度
    String lon; //经度

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
