package com.mini.activity.profile;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我的账户余额
 */
public class DTDBalanceActivity extends MNActivityBase {

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_balance);
        this.setTitle("我的余额");
        this.initView();
    }

    private void initView() {
        this.setNaivLeftBackAction();
    }
}
