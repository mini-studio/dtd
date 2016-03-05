package com.dtdinc.dtd.core.api.data;

import org.mini.frame.http.request.data.MiniDataWrapper;

import java.util.List;

/**
 * Created by Wuquancheng on 15/11/14.
 * 订单信息包装类
 */
public class PackageDetailWrapper extends MiniDataWrapper {

    private List<PackageInfo> order_info;

    public PackageInfo getOrderInfo() {
        if (order_info != null && order_info.size() > 0)
            return order_info.get(0);
        else {
            return null;
        }
    }

    public List<PackageInfo> getOrder_info() {
        return order_info;
    }

    public void setOrder_info(List<PackageInfo> order_info) {
        this.order_info = order_info;
    }
}
