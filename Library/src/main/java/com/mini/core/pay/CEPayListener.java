package com.mini.core.pay;

//支付完成侦听接口
public interface CEPayListener {
	//支付完成
	/*
	 * @param
	 * type    类型  微信支付或者支付宝支付
	 * result  支付结果  成功 失败  未安装 不支持api
	 * errcode 返回错误码
	 * desc    返回错误描述
	 */
	 void chPayDidCompleted(int type, int result, int errcode, String desc);
}
