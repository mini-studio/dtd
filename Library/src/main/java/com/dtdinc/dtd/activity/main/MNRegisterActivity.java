package com.dtdinc.dtd.activity.main;

import android.content.Intent;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by 黄啟华 on 2015/6/24.
 * 注册
 */

public class MNRegisterActivity extends MNActivityBase {
    private static final int REQUEST_REGISTER_CODE = 1;

    private TextView register_phone;
    private TextView register_password;
    private TextView vcode_text;


    @Override
    protected void loadView() {
        setContentView(R.layout.activity_register);
        initView();
        this.setTitle(R.string.register);
    }

    private void initView() {
        this.findViewById(R.id.button_confirm, this);
        this.findViewById(R.id.vcode_button, this);
        this.register_phone = (TextView)this.findViewById(R.id.register_phone);
        this.register_password = (TextView)this.findViewById(R.id.register_password);
        this.vcode_text = (TextView)this.findViewById(R.id.vcode_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Action(R.id.button_confirm)
    public void onConfirmButtonTap() {
        String phone = this.register_phone.getText().toString();
        if (phone == null || phone.length() == 0) {
            showMessage("请输入电话号码");
            return;
        }
        String code = this.vcode_text.getText().toString();
        if (code == null || code.length() == 0) {
            showMessage("请输入验证码");
            return;
        }
        String passwd = this.register_password.getText().toString();
        if (passwd == null || passwd.length() == 0) {
            showMessage("请输入密码");
            return;
        }
        showWaiting();
        api.register(phone, code, passwd, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (error == null) {
                    if ("register_ok".equals(data)) {
                        showMessage("恭喜您注册成功,请登录");
                        MNRegisterActivity.this.back();
                    }
                    else {
                        showMessage("注册失败，请重试");
                    }
                }
                else {
                    showError(error);
                }
            }
        });
    }

    @Action(R.id.vcode_button)
    public void onVCodeButtonTap() {
        String phone = this.register_phone.getText().toString();
        if (phone == null || phone.length() == 0) {
            showMessage("请输入手机号");
            return;
        }
        showWaiting();
        api.getVCode(phone, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if ("get_code_ok".equals(data)) {
                    showMessage("请留意手机短信注册码");
                }
                else if ("already_register".equals(data)) {
                    showMessage("号码已经注册");
                }
                else {
                    showMessage("注册失败，请重试");
                }
            }
        });
    }
}
