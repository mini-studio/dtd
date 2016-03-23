package com.dtdinc.dtd.core.api.engine;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dtdinc.dtd.app.CEApplication;
import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.api.data.City;
import com.dtdinc.dtd.core.api.data.PackageDetailWrapper;
import com.dtdinc.dtd.core.api.data.PackageInfoWrapper;
import com.dtdinc.dtd.core.api.data.User;
import com.dtdinc.dtd.core.api.info.REQBalanceInfo;
import com.dtdinc.dtd.core.api.info.REQChangeMobileInfo;
import com.dtdinc.dtd.core.api.info.REQChangeNicknameInfo;
import com.dtdinc.dtd.core.api.info.REQFeedbackInfo;
import com.dtdinc.dtd.core.api.info.REQFinishOrderInfo;
import com.dtdinc.dtd.core.api.info.REQForgotCodeInfo;
import com.dtdinc.dtd.core.api.info.REQForgotInfo;
import com.dtdinc.dtd.core.api.info.REQGetOrderInfo;
import com.dtdinc.dtd.core.api.info.REQLineInfo;
import com.dtdinc.dtd.core.api.info.REQLocationInfo;
import com.dtdinc.dtd.core.api.info.REQLoginInfo;
import com.dtdinc.dtd.core.api.info.REQMyGetOrderInfo;
import com.dtdinc.dtd.core.api.info.REQMyPublishOrderInfo;
import com.dtdinc.dtd.core.api.info.REQNearByPackageInfo;
import com.dtdinc.dtd.core.api.info.REQPackageDetailInfo;
import com.dtdinc.dtd.core.api.info.REQPostKeyInfo;
import com.dtdinc.dtd.core.api.info.REQPriceInfo;
import com.dtdinc.dtd.core.api.info.REQPublishOrderInfo;
import com.dtdinc.dtd.core.api.info.REQRegisterInfo;
import com.dtdinc.dtd.core.api.info.REQTakeCashInfo;
import com.dtdinc.dtd.core.api.info.REQUpdatePasswdInfo;
import com.dtdinc.dtd.core.api.info.REQUploadTokenInfo;
import com.dtdinc.dtd.core.api.info.REQVCodeInfo;
import com.dtdinc.dtd.core.api.info.REQWXPrePay;
import com.dtdinc.dtd.core.exception.CEDataException;
import com.dtdinc.dtd.core.model.PostPackageInfo;

import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.http.request.MiniRequest;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.pay.wxpay.WXPayParam;
import org.mini.frame.toolkit.MiniSharedPreferences;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/4/2.
 */
public class CEApi {

    private RequestQueue requestQueue;

    public CEApi() {
        this.requestQueue = Volley.newRequestQueue(CEApplication.instance());
    }

    public void getVCode(String mobile, MiniDataListener<String> listener) {
        REQVCodeInfo info = new REQVCodeInfo(mobile);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    public void register(String phone, String code, String password, MiniDataListener<String> listener) {
        REQRegisterInfo info = new REQRegisterInfo(phone, password, code);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 附近订单
     * @param city
     * @param listener
     */
    public void nearbyPackageInfos(City city, int page, MiniDataListener<PackageInfoWrapper> listener) {
        REQNearByPackageInfo info = new REQNearByPackageInfo(city, page);
        MiniRequest request = new MiniRequest(PackageInfoWrapper.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 创建线路
     * @param from
     * @param to
     * @param listener
     */
    public void line(String from, String to, MiniDataListener<String> listener) {
        REQLineInfo info = new REQLineInfo(from, to);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 订单详情
     * @param id
     * @param listener
     */
    public void packageDetailInfo(String id, MiniDataListener<PackageDetailWrapper> listener) {
        REQPackageDetailInfo info = new REQPackageDetailInfo(id);
        MiniRequest request = new MiniRequest(PackageDetailWrapper.class, listener, info);
        this.requestQueue.add(request);
    }

    public void login(final String phone, String passwd, final MiniDataListener<User> listener) {
        REQLoginInfo loginInfo = new REQLoginInfo(phone, passwd);
        MiniRequest request = new MiniRequest(String.class, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                User user = null;
                if (error == null) {
                    user = User.fromString(data,phone);
                    listener.onResponse(user, error);
                    uploadToken();
                }
                else {
                    listener.onResponse(user, error);
                }
            }
        }, loginInfo);
        this.requestQueue.add(request);
    }

    /**
     * 获取忘记密码的手机验证码
     * @param phone
     * @param listener
     */
    public void forget(String phone, final MiniDataListener<String> listener) {
        REQForgotInfo info = new REQForgotInfo(phone);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 验证手机号码和验证码
     * @param phone
     * @param listener
     */
    public void forgetCode(String phone, String vCode, final MiniDataListener<String> listener) {
        REQForgotCodeInfo info = new REQForgotCodeInfo(phone, vCode);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 我要接单
     */
    public void getOrder(String phone, String orderId, String deliver_way, String deliver_info, final MiniDataListener<String> listener) {
        REQGetOrderInfo info = new REQGetOrderInfo(phone, orderId, deliver_way, deliver_info);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 创建订单
     */
    public void publishOrder(String phone, PostPackageInfo packageInfo, final MiniDataListener<String> listener) {
        REQPublishOrderInfo info = new REQPublishOrderInfo(phone, packageInfo);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 用户上传推送 token
     * 用户上传token需要判断用户的电话号码和token同时不能为空
     */
    public void uploadToken() {
        User user = WHO();
        String token = MiniSharedPreferences.instance().getString("token", null);
        if (user != null && user.getPhone() != null && token != null && token.length() > 0) {
            //判断该用户的token是否已经上传过了，如果已经上传过了，就不需要再次上传，以免
            //造成服务器的压力
            REQUploadTokenInfo info = new REQUploadTokenInfo(token);
            MiniRequest request = new MiniRequest(String.class, new MiniDataListener<String>() {
                @Override
                public void onResponse(String data, CEDataException error) {
                    if ("ok".equals(data)) {
                        MiniLogger.get().d("upload token success");
                    }
                    else {
                        MiniLogger.get().d("upload token error :" + data);
                    }
                }
            }, info);
            this.requestQueue.add(request);
        }
    }

    /**
     * 验证手机号码和验证码
     * @param phone
     * @param listener
     */
    public void updatePassword(String phone, String vCode, String passwd, final MiniDataListener<String> listener) {
        REQUpdatePasswdInfo info = new REQUpdatePasswdInfo(phone, vCode, passwd);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 定位当前的城市
     * @param listener
     */
    public void currentCity(final Double lng, final Double lat, final MiniDataListener<City> listener) {
        REQLocationInfo info = new REQLocationInfo(lng, lat);
        MiniRequest request = new MiniRequest(String.class, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if (error == null && data != null) {
                    City city = City.cityWithName(data);
                    if (city != null) {
                        city.setLatitude(lat);
                        city.setLongitude(lng);
                    }
                    MiniSharedPreferences.instance().setObject("city", city);
                    CESystem.instance().setCurrentCity(city);
                    listener.onResponse(city, null);
                }
                else {
                    listener.onResponse(null, null);
                }
            }
        }, info);
        this.requestQueue.add(request);
    }

    /**
     * 修改紧急联系手机号码
     * @param phone
     * @param listener
     */
    public void changEmergencyPhone(String phone, String emergencyPhone, final MiniDataListener<String> listener) {
        REQChangeMobileInfo info = new REQChangeMobileInfo(phone, emergencyPhone);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 获取微信订单的信息
     * @param orderId
     * @param listener
     */
    public void wxPayInfo(String orderId, final MiniDataListener<WXPayParam> listener) {
        REQWXPrePay info = new REQWXPrePay(orderId);
        MiniRequest request = new MiniRequest(String.class, new MiniDataListener<String>() {

            @Override
            public void onResponse(String data, CEDataException error) {
                if (error == null) {
                    WXPayParam payParam = WXPayParam.fromJsonString(data);
                    listener.onResponse(payParam, null);
                }
                else {
                    listener.onResponse(null, error);
                }
            }
        }, info);
        this.requestQueue.add(request);
    }

    /**
     *
     * @param phone
     * @param nickname
     * @param listener
     */
    public void changeNickname(String phone, String nickname, final MiniDataListener<String> listener) {
        REQChangeNicknameInfo info = new REQChangeNicknameInfo(phone, nickname);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     *
     * @param phone
     * @param password
     * @param listener
     */
    public void changePassword(String phone, String password, final MiniDataListener<String> listener) {
        REQChangeNicknameInfo info = new REQChangeNicknameInfo(phone, password);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 我的订单
     * @param phone
     * @param listener
     */
    public void myOrder(String phone,MiniDataListener<PackageInfoWrapper> listener) {
        REQMyGetOrderInfo info = new REQMyGetOrderInfo(phone);
        MiniRequest request = new MiniRequest(PackageInfoWrapper.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 我创建的订单
     * @param phone
     * @param listener
     */
    public void myPublishOrder(String phone, MiniDataListener<PackageInfoWrapper> listener) {
        REQMyPublishOrderInfo info = new REQMyPublishOrderInfo(phone);
        MiniRequest request = new MiniRequest(PackageInfoWrapper.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 获取邮件的邮资
     * @param weight
     * @param listener
     */
    public void getPrice(String weight, MiniDataListener<String> listener) {
        REQPriceInfo info = new REQPriceInfo(weight);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 完成订单
     * @param phone
     * @param listener
     */
    public void finishOrder(String phone, String id, MiniDataListener<String> listener) {
        REQFinishOrderInfo info = new REQFinishOrderInfo(phone, id);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 上传收货码
     * @param phone
     * @param listener
     */
    public void postKey(String phone, String id, String key, MiniDataListener<String> listener) {
        REQPostKeyInfo info = new REQPostKeyInfo(phone, id, key);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 上传收货码
     * @param text
     * @param listener
     */
    public void feedback(String text, MiniDataListener<String> listener) {
        REQFeedbackInfo info = new REQFeedbackInfo(text);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

    /**
     * 获取当前余额
     * @param phone
     * @param listener
     */
    public void balanceInfo(String phone, MiniDataListener<String> listener) {
        REQBalanceInfo balanceInfo = new REQBalanceInfo(phone);
        MiniRequest request = new MiniRequest(String.class, listener, balanceInfo);
        this.requestQueue.add(request);
    }

    /**
     * 发起提取余额的申请
     * @param money
     * @param listener
     */
    public void takeCash(String money, MiniDataListener<String> listener) {
        REQTakeCashInfo info = new REQTakeCashInfo(money);
        MiniRequest request = new MiniRequest(String.class, listener, info);
        this.requestQueue.add(request);
    }

}
