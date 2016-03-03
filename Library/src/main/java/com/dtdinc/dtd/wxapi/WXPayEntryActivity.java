package com.dtdinc.dtd.wxapi;

import android.content.Intent;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;

import org.mini.frame.pay.PayConstants;
import org.mini.frame.pay.wxpay.WXPay;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends MNActivityBase implements IWXAPIEventHandler {

  private IWXAPI api;

  @Override
  protected void loadView() {
    this.setContentView(R.layout.activity_title);
    api = WXAPIFactory.createWXAPI(this, WXPay.WX_APPID);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq req) {

  }

  @Override
  public void onResp(BaseResp resp) {
    //支付回调成功
    if (WXPay.sharedInstance().listener != null) {
      String errStr = resp.errStr;
      if (errStr == null)
        errStr = "";
      if (resp.errCode == 0) {
          WXPay.sharedInstance().listener.onPayCompleted(PayConstants.kCHPayTypeWeixin, PayConstants.kCHPayResultSuccess, resp.errCode, errStr);
      } else {
          WXPay.sharedInstance().listener.onPayCompleted(PayConstants.kCHPayTypeWeixin, PayConstants.kCHPayResultFailure, resp.errCode, errStr);
      }
    }
    finish();
  }
}
