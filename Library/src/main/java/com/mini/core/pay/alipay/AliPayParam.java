package com.mini.core.pay.alipay;

import android.text.TextUtils;

/**
 * Created by Wuquancheng on 16/2/22.
 */
public class AliPayParam {

    private String tradeNO;
    private String productName;
    private String productDescription;
    private String amount;
    private String notifyURL;

    private String service;
    private String paymentType;
    private String inputCharset;
    private String itBPay;
    private String showUrl;


    private String rsaDate;//可选
    private String appID;//可选


    public String getTradeNO() {
        return tradeNO;
    }

    public void setTradeNO(String tradeNO) {
        this.tradeNO = tradeNO;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getItBPay() {
        return itBPay;
    }

    public void setItBPay(String itBPay) {
        this.itBPay = itBPay;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getRsaDate() {
        return rsaDate;
    }

    public void setRsaDate(String rsaDate) {
        this.rsaDate = rsaDate;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    private String getOrderInfo() throws PayException{
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Config.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Config.SELLER + "\"";
        if (TextUtils.isEmpty(tradeNO)) {
            throw new PayException("订单编号为空");
        }
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + tradeNO + "\"";

        if (TextUtils.isEmpty(productName)) {
            throw new PayException("订单名称为空");
        }
        // 商品名称
        orderInfo += "&subject=" + "\"" + productName + "\"";

        if (TextUtils.isEmpty(productDescription)) {
            throw new PayException("订单详情为空");
        }
        // 商品详情
        orderInfo += "&body=" + "\"" + productDescription + "\"";

        if (TextUtils.isEmpty(amount)) {
            throw new PayException("商品金额为空");
        }
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + amount + "\"";
        if (TextUtils.isEmpty(notifyURL)) {
            throw new PayException("通知页面路径为空");
        }
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notifyURL + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        if (showUrl == null) {
            showUrl = "m.alipay.com";
        }
        orderInfo += "&return_url=\""+showUrl+"\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public String description() throws PayException {
        String orderInfo = getOrderInfo();
        return orderInfo;
    }

    public String signedDescription() throws PayException{
        String orderInfo = getOrderInfo();
        return SignUtils.sign(orderInfo, Config.RSA_PRIVATE);
    }
}
