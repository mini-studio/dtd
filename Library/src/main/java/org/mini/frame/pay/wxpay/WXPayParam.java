package org.mini.frame.pay.wxpay;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 16/3/3.
 */
public class WXPayParam {
    public final static String WX_APPID = "wxaf61a6504b334c37";

    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static WXPayParam fromJsonString(String jsonString) {
        WXPayParam payParam = new WXPayParam();
        Gson gson = new Gson();
        Map map = gson.fromJson(jsonString, HashMap.class);
        payParam.appid = (String)map.get("appid");
        payParam.partnerid = (String)map.get("partnerid");
        payParam.prepayid = (String)map.get("prepayid");
        payParam.noncestr = (String)map.get("noncestr");
        payParam.timestamp = (String)map.get("timestamp");
        payParam.packageValue = (String)map.get("package");
        payParam.sign = (String)map.get("sign");
        return payParam;
    }

    public static WXPayParam fromHttpQueryString(String queryString) {
        WXPayParam payParam = new WXPayParam();
        String[] items = queryString.split("&");
        if (items != null && items.length > 0) {
            for (String item : items) {
                String[] datas = item.split("=");
                if (datas != null && datas.length == 2) {
                    String key = datas[0];
                    String value = datas[1];
                    if (key.equals("appid")) {
                        payParam.appid = value;
                    }
                    else if (key.equals("noncestr")) {
                        payParam.noncestr = value;
                    }
                    else if (key.equals("package")) {
                        try {
                            payParam.packageValue = URLDecoder.decode(value, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (key.equals("partnerid")) {
                        payParam.partnerid = value;
                    }
                    else if (key.equals("prepayid")) {
                        payParam.prepayid = value;
                    }
                    else if (key.equals("timestamp")) {
                        payParam.timestamp = value;
                    }
                    else if (key.equals("sign")) {
                        payParam.sign = value;
                    }
                }
            }
        }
        return payParam;
    }
}
