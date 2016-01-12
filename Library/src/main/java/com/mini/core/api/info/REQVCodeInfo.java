package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 获取手机验证码
 */
public class REQVCodeInfo extends MiniREQBaseInfo {

    /**
     * 需要获取验证码的手机号码
     */
    private String mobile = null;

    public REQVCodeInfo(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String getPath() {
        return "/gindex/get_code/" + this.mobile;
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
