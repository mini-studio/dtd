package com.dtdinc.dtd.activity.profile;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我的账户余额
 */
public class DTDAboutActivity extends MNActivityBase {

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_about);
        this.setTitle("关于");
        this.initView();
    }

    private void initView() {
        this.setNaivLeftBackAction();
    }
}
