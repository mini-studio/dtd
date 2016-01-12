package com.mini.core.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.mini.core.pay.CEPayConstants;
import com.mini.core.pay.CEPayListener;


@SuppressLint("HandlerLeak")
public class CEAlipayManager {

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	public CEPayListener listener = null;
	//-----------------------------------------------------------------
	//单例
	private volatile static CEAlipayManager aliay_share = null;
	//单例
	public static CEAlipayManager shard()
	{
		if(aliay_share==null)
		{
			synchronized(CEAlipayManager.class)
			{
				if(aliay_share==null)
				{
					aliay_share = new CEAlipayManager();
				}
			}
		}
		return aliay_share;
	}
	
	//构造函数
	private CEAlipayManager()
    {
    }
	
	//-----------------------------------------------------------------
	//Handler
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case SDK_PAY_FLAG: 
				{
					CEPayResult payResult = new CEPayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
					String resultStatus = payResult.getResultStatus();
	
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) 
					{
						if (CEAlipayManager.shard().listener != null)
							CEAlipayManager.shard().listener.chPayDidCompleted(CEPayConstants.kCHPayTypeAlipay,
									CEPayConstants.kCHPayResultSuccess, Integer.parseInt(resultStatus), resultInfo);
					} 
					else 
					{
						if (CEAlipayManager.shard().listener != null)
							CEAlipayManager.shard().listener.chPayDidCompleted(CEPayConstants.kCHPayTypeAlipay,
									CEPayConstants.kCHPayResultFailure, Integer.parseInt(resultStatus), resultInfo);
						
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					}
					break;
				}
				case SDK_CHECK_FLAG: 
				{
					
					break;
				}
				default:
					break;
			}
		};
	};
	
	//支付
	public void pay(final Activity context, final String payStr) {
				
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

		//异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}	
	
	//查询终端设备是否存在支付宝认证账户
	public void check(final Activity context) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}
}
