package com.mini.core.api.info;

import org.mini.frame.http.request.info.MiniREQBaseInfo;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单详情请求参数类
 */
public class REQFeedbackInfo extends MiniREQBaseInfo {

    String key = "bp8Q3aMRjAQisJ";
    String text;

    public REQFeedbackInfo(String text) {
        super(GET);
        this.text = text;
    }

    @Override
    public String getPath() {
        try {
            return "http://api.3joy.cn/index.php/api_ios/feedback/" + this.key + "/" + URLEncoder.encode(this.text, "utf-8");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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
