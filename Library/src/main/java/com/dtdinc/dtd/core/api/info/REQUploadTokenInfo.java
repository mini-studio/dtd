package com.dtdinc.dtd.core.api.info;

import com.dtdinc.dtd.core.api.data.User;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.util.HashMap;
import java.util.Map;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 上传推送token
 */
public class REQUploadTokenInfo extends MiniREQBaseInfo {

    String token;

    public REQUploadTokenInfo(String token) {
        super(POST);
        this.token = token;
    }

    @Override
    public String getPath() {
        return "/gindex/token";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        User user = WHO();
        if (user != null) {
            map.put("phone", user.getPhone());
            map.put("token", this.token);
            map.put("sid", user.getSid());
            map.put("system", "2");
        }
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
