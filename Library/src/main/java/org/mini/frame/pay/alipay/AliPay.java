package org.mini.frame.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import org.mini.frame.pay.PayConstants;
import org.mini.frame.pay.PayListener;

import java.net.URLEncoder;


@SuppressLint("HandlerLeak")
public class AliPay {

	private static final int SDK_PAY_FLAG = 1;
	
	public PayListener listener = null;

	public AliPay() {
    }

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					CEPayResult payResult = new CEPayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
                    if (TextUtils.isEmpty(resultInfo)) {
                        resultInfo = payResult.getMemo();
                    }
					String resultStatus = payResult.getResultStatus();
	
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if ("9000".equals(resultStatus)) {
						if (listener != null)
							listener.onPayCompleted(PayConstants.kCHPayTypeAlipay, PayConstants.kCHPayResultSuccess, Integer.parseInt(resultStatus), resultInfo);
					} 
					else {
						if (listener != null)
							listener.onPayCompleted(PayConstants.kCHPayTypeAlipay, PayConstants.kCHPayResultFailure, Integer.parseInt(resultStatus), resultInfo);
						
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					}
					break;
				}
				default:
					break;
			}
		};
	};

	public void pay(final Activity context, final String payStr, PayListener listener) {
				
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payStr);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
        this.listener = listener;
		//异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

    public void pay(final Activity context, final AliPayParam order, PayListener listener) throws PayException{
        String orderInfo = order.description();
        String sign = SignUtils.sign(orderInfo, Config.RSA_PRIVATE);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        }
        catch (Exception e) {
            throw new PayException("编码错误");
        }
        String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
        pay(context, payInfo, listener);
    }
}
