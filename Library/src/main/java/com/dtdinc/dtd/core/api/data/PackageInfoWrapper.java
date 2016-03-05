package com.dtdinc.dtd.core.api.data;

import org.mini.frame.http.request.data.MiniDataWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/11/14.
 * 订单信息包装类
 */
public class PackageInfoWrapper extends MiniDataWrapper {

    private List<PackageInfo> order;

    private int total;

    private List<ImageInfo> index_photo;

    private boolean hasMore = true;

    public List<PackageInfo> getOrder() {
        return order;
    }

    public void setOrder(List<PackageInfo> order) {
        this.order = order;
    }

    public void addPackageInfo(PackageInfo packageInfo) {
        if (order == null) {
            order = new ArrayList<PackageInfo>(0);
        }
        order.add(packageInfo);
    }

    public int count() {
        if (order != null) {
            return order.size();
        }
        return 0;
    }

    public void append(PackageInfoWrapper other) {
        if (other != null) {
            if (order == null) {
                order = new ArrayList<PackageInfo>();
            }
            if (other.getOrder() != null) {
                order.addAll(other.getOrder());
            }
        }
    }

    public PackageInfo getPackageInfo(int index) {
        if (order != null && order.size() > index) {
            return order.get(index);
        }
        else {
            return null;
        }
    }

    public List<ImageInfo> getIndex_photo() {
        return index_photo;
    }

    public void setIndex_photo(List<ImageInfo> index_photo) {
        this.index_photo = index_photo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
