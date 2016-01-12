package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQUpdatePasswdInfo extends MiniREQBaseInfo {

    String phone;
    String vCode;
    String passwd;

    public REQUpdatePasswdInfo(String phone, String vCode, String passwd) {
        super(POST);
        this.phone = phone;
        this.vCode = vCode;
        this.passwd = passwd;
    }

    @Override
    public String getPath() {
        return "/gindex/update_password";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("code", this.vCode);
        map.put("password", this.passwd);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
