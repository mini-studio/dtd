package com.dtdinc.dtd.activity.main;

import android.content.Intent;
import android.widget.EditText;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/4/6.
 * 忘记密码
 */
public class MNVCodeActivity extends MNActivityBase {

    private CEApi api = new CEApi();

    private EditText inputView;

    private String phone;

    private static final int REQ_UPDATE_PASSWD = 20000;

    public void loadView() {
        this.setContentView(R.layout.activity_vcode);
        this.setTitle("验证码");
        this.phone = (String)getIntentObject();
        this.findViewById(R.id.sms_code_button, this);
        inputView = (EditText) findViewById(R.id.input_view);
    }


    /**
     * 获取手机验证码
     */
    @Action(R.id.sms_code_button)
    public void actionForgotPasswd() {
        final String code = inputView.getText().toString();
        if (code == null || code.length() == 0) {
            showMessage("请输入验证码");
            return;
        }
        showWaiting();
        api.forgetCode(phone, code, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if ("validate_ok".equals(data)) {
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("phone", phone);
                    map.put("code", code);
                    startActivityForResult(MNRestPasswdActivity.class, map, REQ_UPDATE_PASSWD);
                } else {
                    showMessage("请检查验证码");
                }
            }
        });
    }

    @ActivityResult(REQ_UPDATE_PASSWD)
    public void resultUpdatePasswd(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            setResult(resultCode);
            finish();
        }
    }
}
