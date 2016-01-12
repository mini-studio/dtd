package com.mini.activity.send;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.engine.CEApi;

import org.mini.frame.annotation.Action;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我要发件的主界面
 */
public class ExSendPackageSuccessActivity extends MNActivityBase {


    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_sendpackage_sucess);
        this.setTitle("发布成功");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.back_button, this);
        this.findViewById(R.id.view_detail_button, this);
    }

    @Action(R.id.back_button)
    public void onNextButtonTap() {
        back();
    }

    @Action(R.id.view_detail_button)
    public void onViewDetailButtonTap() {
        //startActivity(ExSendPackageConfirmActivity.class);
    }
}
