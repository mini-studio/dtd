package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQRegisterInfo extends MiniREQBaseInfo {

    String phone;
    String password;
    String code;

    public REQRegisterInfo(String phone, String passwd, String code) {
        super(POST);
        this.phone = phone;
        this.password = passwd;
        this.code = code;
    }

    @Override
    public String getPath() {
        return "/gindex/register";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("password", this.password);
        map.put("code", this.code);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
