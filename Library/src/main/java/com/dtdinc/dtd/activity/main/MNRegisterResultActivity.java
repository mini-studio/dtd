package com.dtdinc.dtd.activity.main;

import android.content.Intent;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;

import org.mini.frame.annotation.Action;

/**
 * Created by 黄啟华 on 2015/6/24.
 * 注册
 */

public class MNRegisterResultActivity extends MNActivityBase {

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_register_success);
        initView();
        this.setTitle(R.string.register);
        this.setNaviRightStringAction("登录");
    }

    private void initView() {
        findViewById(R.id.button_login, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Action(R.id.button_login)
    public void onLoginButtonTap() {
        startActivity(MNSigninActivity.class);
    }
}
