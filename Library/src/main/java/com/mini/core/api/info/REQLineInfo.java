package com.mini.core.api.info;

import com.mini.core.api.data.User;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQLineInfo extends MiniREQBaseInfo {

    String phone;
    String from;
    String to;

    public REQLineInfo(String from, String to) {
        super(POST);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getPath() {
        return "/gindex/line";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        User user = WHO();
        map.put("phone", user.getPhone());
        map.put("sid", user.getSid());
        map.put("from", from);
        map.put("to", to);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
