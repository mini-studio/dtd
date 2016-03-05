package com.dtdinc.dtd.core.model;

/**
 * Created by Wuquancheng on 15/12/13.
 */
public class PostPackageInfo {

    /**
     * 发件人信息
     */
    private PostUserInfo posterInfo;

    /**
     * 收件人信息
     */
    private PostUserInfo recipientsInfo;

    /**
     * 物品名称
     */
    private String packageName;

    /**
     * 物品质量
     */
    private String packageWeight;

    /**
     * 价格
     */
    private String price;

    public PostPackageInfo() {
        this.posterInfo = new PostUserInfo();
        this.recipientsInfo = new PostUserInfo();
    }

    public PostUserInfo getPosterInfo() {
        return posterInfo;
    }

    public void setPosterInfo(PostUserInfo posterInfo) {
        this.posterInfo = posterInfo;
    }

    public PostUserInfo getRecipientsInfo() {
        return recipientsInfo;
    }

    public void setRecipientsInfo(PostUserInfo recipientsInfo) {
        this.recipientsInfo = recipientsInfo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public boolean isCompletive() {
        if (posterInfo == null || posterInfo.isEmpty() || recipientsInfo == null || recipientsInfo.isEmpty()) {
            return false;
        }
        if (packageName == null || packageName.length() == 0) {
            return false;
        }
        if (packageWeight == null || packageWeight.length() == 0) {
            return false;
        }
        return true;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
