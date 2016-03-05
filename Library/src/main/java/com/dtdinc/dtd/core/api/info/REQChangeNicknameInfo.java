package com.dtdinc.dtd.core.api.info;

import com.dtdinc.dtd.core.api.data.User;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQChangeNicknameInfo extends MiniREQBaseInfo {

    String phone;
    String nickname;

    public REQChangeNicknameInfo(String phone, String nickname) {
        super(POST);
        this.phone = phone;
        this.nickname = nickname;
    }

    @Override
    public String getPath() {
        return "/gindex/change_nickname";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        map.put("nickname", this.nickname);
        User user = WHO();
        map.put("sid",user.getSid());
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
