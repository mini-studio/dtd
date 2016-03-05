package com.dtdinc.dtd.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQLoginInfo extends MiniREQBaseInfo {

    String phone;
    String passwd;

    public REQLoginInfo(String phone, String passwd) {
        super(POST);
        this.phone = phone;
        this.passwd = passwd;
    }

    @Override
    public String getPath() {
        return "/gindex/login";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("password", this.passwd);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
