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
public class REQChangeMobileInfo extends MiniREQBaseInfo {

    String phone;
    String emerPhone;

    public REQChangeMobileInfo(String phone, String emerPhone) {
        super(POST);
        this.phone = phone;
        this.emerPhone = emerPhone;
    }

    @Override
    public String getPath() {
        return "/gindex/change_emergency_phone";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("emergency_phone", this.emerPhone);
        User user = WHO();
        map.put("sid",user.getSid());
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
