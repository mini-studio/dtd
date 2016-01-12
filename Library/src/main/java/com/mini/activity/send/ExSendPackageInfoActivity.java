package com.mini.activity.send;

import android.widget.Button;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.engine.CEApi;

import org.mini.frame.annotation.Action;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我要发件的主界面
 */
public class ExSendPackageInfoActivity extends MNActivityBase {


    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_sendpackage_info);
        this.setTitle("我要发件");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.next_button, this);
    }

    @Action(R.id.next_button)
    public void onNextButtonTap() {
        startActivity(ExSendPackageConfirmActivity.class);
    }
}
