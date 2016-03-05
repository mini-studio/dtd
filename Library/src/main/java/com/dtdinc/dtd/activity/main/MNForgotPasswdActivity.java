package com.dtdinc.dtd.activity.main;

import android.content.Intent;
import android.widget.EditText;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.define.CEConst;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by Wuquancheng on 15/4/6.
 * 忘记密码
 */
public class MNForgotPasswdActivity extends MNActivityBase {

    private CEApi api = new CEApi();

    private EditText phoneView;

    private static final int REQ_VCODE = 20000;

    public void loadView() {
        this.setContentView(R.layout.activity_forgot_passwd);
        this.setTitle("忘记密码");
        this.setNaviRightStringAction("注册");
        this.findViewById(R.id.sms_code_button, this);
        phoneView = (EditText) findViewById(R.id.phone_view);
    }


    @Action(R.id.app_title_right)
    public void onRightAction() {
        startActivity(MNRegisterActivity.class);
    }

    /**
     * 获取手机验证码
     */
    @Action(R.id.sms_code_button)
    public void actionForgotPasswd() {
        final String phone = phoneView.getText().toString();
        if (phone == null || phone.length() == 0) {
            showMessage("请输入手机号码");
            return;
        }
        showWaiting();
        api.forget(phone, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (CEConst.GET_CODE_OK.equals(data)) {
                    startActivityForResult(MNVCodeActivity.class, phone, REQ_VCODE);
                } else {
                    showMessage("请检查手机号");
                }
            }
        });
    }
    @ActivityResult(REQ_VCODE)
    public void resultForVcode(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            setResult(resultCode);
            finish();
        }
    }
}
