package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 获取手机验证码
 */
public class REQWXPrePay extends MiniREQBaseInfo {

    /**
     * 需要获取验证码的手机号码
     */
    private String orderId = null;

    public REQWXPrePay(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getPath() {
        return "/gindex/wx_prepay/" + this.orderId;
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
