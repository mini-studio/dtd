package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQForgotCodeInfo extends MiniREQBaseInfo {

    String phone;
    String vCode;

    public REQForgotCodeInfo(String phone, String vCode) {
        super(POST);
        this.phone = phone;
        this.vCode = vCode;
    }

    @Override
    public String getPath() {
        return "/gindex/forget_code";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("code", this.vCode);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
