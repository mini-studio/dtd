package org.mini.frame.pay.wxpay;

import android.content.Context;

import org.mini.frame.pay.PayConstants;
import org.mini.frame.pay.PayListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPay {

	public final static String WX_APPID = WXPayParam.WX_APPID;
	
	private IWXAPI wxApi = null;
	
	public PayListener listener = null;

    private static WXPay wxPay = null;

    public static synchronized WXPay sharedInstance() {
        if (wxPay == null) {
            wxPay = new WXPay();
        }
        return wxPay;
    }

	//构造函数
	private WXPay() {
	}

    public void pay(Context context, WXPayParam payParam, PayListener listener) {
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(context, WX_APPID, false);
        }
        if (!wxApi.isWXAppInstalled())
        {
            if(this.listener != null)
            {
                this.listener.onPayCompleted(PayConstants.kCHPayTypeWeixin, PayConstants.kCHPayResultNotInstall, 0, "");
            }
            return ;
        }
        if (!wxApi.isWXAppSupportAPI())
        {
            if(this.listener != null)
            {
                this.listener.onPayCompleted(PayConstants.kCHPayTypeWeixin, PayConstants.kCHPayResultNotSupportApi, 0, "");
            }
            return ;
        }
        this.listener = listener;
        wxApi.registerApp(WX_APPID);
        PayReq request = new PayReq();
        request.appId = payParam.getAppid();
        request.nonceStr = payParam.getNoncestr();
        request.packageValue = payParam.getPackageValue();
        request.partnerId = payParam.getPartnerid();
        request.prepayId = payParam.getPrepayid();
        request.sign = payParam.getSign();
        request.timeStamp = payParam.getTimestamp();
        wxApi.sendReq(request);
    }

    public void pay(Context context, String string, PayListener listener) {
        WXPayParam payParam = WXPayParam.fromHttpQueryString(string);
        pay(context, payParam, listener);
    }
}
