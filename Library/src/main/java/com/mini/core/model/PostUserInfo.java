package com.mini.core.model;

import com.mini.core.api.data.City;

import org.mini.frame.toolkit.MiniTimeUtil;

import java.util.Date;

/**
 * Created by Wuquancheng on 15/12/13.
 */
public class PostUserInfo {

    private String name;
    private String mobile;
    private City city;
    private String address;
    private String time;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }
    public String getFullAddress() {
        if (city != null) {
            return city.getName() + this.address;
        }
        else {
            return this.address;
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCityName() {
        if (city != null) {
            return city.getName();
        }
        else {
            return "";
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.setTime(MiniTimeUtil.formatDate(date, MiniTimeUtil.datetimeFormat));
    }

    public String getPackageTimeString() {
        return MiniTimeUtil.formatDate(date, MiniTimeUtil.package_Format);
    }

    public boolean isEmpty() {
        return (name == null || name.length() == 0) &&
                (mobile == null || mobile.length() == 0) &&
                (address == null || address.length() == 0);
    }

}
