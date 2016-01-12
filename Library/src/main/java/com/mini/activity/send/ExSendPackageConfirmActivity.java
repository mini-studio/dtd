package com.mini.activity.send;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.engine.CEApi;

import org.mini.frame.annotation.Action;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我要发件的主界面
 */
public class ExSendPackageConfirmActivity extends MNActivityBase {


    private CEApi api = new CEApi();
    private PackageInfo packageInfo;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_sendpackage_confirm);
        this.setTitle("我要发件");
        this.initView();
    }

    private void initView() {
        this.packageInfo = (PackageInfo)getIntentObject();
        if (this.packageInfo != null && this.packageInfo.getActivityTitle() != null) {
            this.setTitle(this.packageInfo.getActivityTitle());
        }
        this.findViewById(R.id.confirm_button, this);
    }

    @Action(R.id.confirm_button)
    public void onNextButtonTap() {
        startActivity(ExSendPackagePaymentActivity.class);
    }
}
