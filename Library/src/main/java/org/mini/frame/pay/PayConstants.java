package org.mini.frame.pay;

public class PayConstants {
	
	//支付宝支付
	public static int kCHPayTypeAlipay = 0;
	//微信支付
	public static int kCHPayTypeWeixin = 1;
	
	//支付结果
	public static int kCHPayResultSuccess       = 0;  //支付成功
	public static int kCHPayResultFailure       = 1;  //支付失败
	public static int kCHPayResultNotInstall    = 2;  //未安装
	public static int kCHPayResultNotSupportApi = 3;  //不支持相应API
}
