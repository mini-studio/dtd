package com.dtdinc.dtd.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQFinishOrderInfo extends MiniREQBaseInfo {

    String phone;
    String id;

    public REQFinishOrderInfo(String phone, String id) {
        super(POST);
        this.phone = phone;
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/gindex/finish_order";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("id", this.id);
        map.put("sid", WHO().getSid());
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
