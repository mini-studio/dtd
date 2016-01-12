package com.mini.core.api.info;

import com.mini.core.api.data.User;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我接收的订单的列表请求参数类
 */
public class REQMyGetOrderInfo extends MiniREQBaseInfo {

    String phone;

    public REQMyGetOrderInfo(String phone) {
        super(GET);
        this.phone = phone;
    }

    @Override
    public String getPath() {
        return "/gindex/my_get_order/" + this.phone + "/" + WHO().getSid();
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
