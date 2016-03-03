package com.mini.core.pay.wxpay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.content.Context;

import com.mini.core.pay.PayConstants;
import com.mini.core.pay.PayListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPay {

	public final static String WX_APPID = WXPayParam.WX_APPID;
	
	private IWXAPI wxApi = null;
	
	public PayListener listener = null;
	//-----------------------------------------------------------------
	//单例
	private volatile static WXPay wx_share = null;
	//单例
	public static WXPay shard()
	{
		if(wx_share==null)
		{
			synchronized(WXPay.class)
			{
				if(wx_share==null)
				{
					wx_share = new WXPay();
				}
			}
		}
		return wx_share;
	}
		
	//构造函数
	private WXPay()
	{
		
	}

    public void pay(Context context, WXPayParam payParam, PayListener listener) {

    }
	//-----------------------------------------------------------------
	//初始化
	public void initWxPay(Context context)
	{
		wxApi = WXAPIFactory.createWXAPI(context, WX_APPID, false);
	}
	
	//支付
	public void pay(String payStr)
	{
		if(wxApi != null)
		{
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
		
			wxApi.registerApp(WX_APPID);
			PayReq request = new PayReq();
			
			String[] array = payStr.split("&");
			for(int i=0;i<array.length;i++)
			{
				String[] dataArray = array[i].split("=");
				if (dataArray.length == 2)
				{
					String key   = dataArray[0];
					String value = dataArray[1];
					
					if (key.equals("appid"))
					{
						request.appId = value;
					}
					else if (key.equals("noncestr"))
					{
						request.nonceStr = value;
					}
					else if (key.equals("package"))
		            {
		                try {
							request.packageValue = URLDecoder.decode(value, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
		            }
		            else if (key.equals("partnerid"))
		            {
		                request.partnerId = value;
		            }
		            else if (key.equals("prepayid"))
		            {
		                request.prepayId = value;
		            }
		            else if (key.equals("timestamp"))
		            {
		                request.timeStamp = value; 
		            }
		            else if (key.equals("sign"))
		            {
		                request.sign = value;
		            }
				}
			}
			
			wxApi.sendReq(request);
		}
	}
}
