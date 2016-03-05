package com.dtdinc.dtd.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我接收的订单的列表请求参数类
 */
public class REQPriceInfo extends MiniREQBaseInfo {

    String weight;

    public REQPriceInfo(String weight) {
        super(GET);
        this.weight = weight;
    }

    @Override
    public String getPath() {
        return "/gindex/get_price/" + this.weight;
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
