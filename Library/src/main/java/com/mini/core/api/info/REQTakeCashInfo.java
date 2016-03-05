package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 提现
 */
public class REQTakeCashInfo extends MiniREQBaseInfo {

    private String money;

    public REQTakeCashInfo(String money) {
        super(POST);
        this.money = money;
    }

    @Override
    public String getPath() {
        return "/gindex/get_my_money";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone",WHO().getPhone());
        map.put("sid", WHO().getSid());
        map.put("money", money);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
