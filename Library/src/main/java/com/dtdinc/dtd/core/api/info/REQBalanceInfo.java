package com.dtdinc.dtd.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 获取手机验证码
 */
public class REQBalanceInfo extends MiniREQBaseInfo {

    /**
     * 需要获取验证码的手机号码
     */
    private String phone = null;

    public REQBalanceInfo(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPath() {
        return "/gindex/my_money/" + this.phone+"/" + WHO().getSid();
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
