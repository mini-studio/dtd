package com.mini.wxapi;

import com.mini.activity.comm.MNActivityBase;
import com.mini.wxapi.share.CEWeiXinShareParams;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by gassion on 15/8/14.
 */
public class WXEntryActivity extends MNActivityBase implements IWXAPIEventHandler {


  @Override
  protected void loadView() {
    IWXAPI api = WXAPIFactory.createWXAPI(this, CEWeiXinShareParams.PARENTS_APP_ID, false);
    api.handleIntent(getIntent(), this);
  }

  @Override
  public void onReq(BaseReq arg0) {}


  @Override
  public void onResp(BaseResp resp) {
    switch (resp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        // 分享成功
        showMessage("分享成功");
        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        // 分享取消
        showMessage("分享取消");
        break;
      default:
        showMessage("分享失败了["+resp.errCode+"]");
        break;

    }
    finish();
  }

}
