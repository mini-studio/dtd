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
public class REQGetOrderInfo extends MiniREQBaseInfo {

    String phone;
    String orderId;
    String deliver_info;
    String deliver_way;

    public REQGetOrderInfo(String phone, String orderId, String deliver_way, String deliver_info) {
        super(POST);
        this.phone = phone;
        this.orderId = orderId;
        this.deliver_way = deliver_way;
        this.deliver_info = deliver_info;
    }

    @Override
    public String getPath() {
        return "/gindex/get_order";
    }

    @Override
    public Map<String, String> getFormParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", this.phone);
        User user = WHO();
        String sid = user.getSid();
        map.put("sid", sid);
        map.put("id", this.orderId);
        map.put("deliver_info", this.deliver_info);
        map.put("deliver_way", this.deliver_way);
        return map;
    }

    @Override
    public Map<String, String> getQueryParamsMap() {
        return null;
    }
}
