package com.dtdinc.dtd.activity.main;

import android.widget.Button;
import android.widget.EditText;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/4/6.
 * 忘记密码
 */
public class MNRestPasswdActivity extends MNActivityBase {

    private CEApi api = new CEApi();

    private EditText inputView;
    private Button button;

    private String phone; //手机号码
    private String code; //验证码

    public void loadView() {
        this.setContentView(R.layout.activity_vcode);
        this.setTitle("忘记密码");
        Map<String, String> map = (Map<String, String>)getIntentObject();
        this.phone = map.get("phone");
        this.code = map.get("code");
        button = (Button)this.findViewById(R.id.sms_code_button, this);
        button.setText("提交");
        inputView = (EditText) findViewById(R.id.input_view);
        inputView.setHint("输入新密码");
    }


    /**
     * 获取手机验证码
     */
    @Action(R.id.sms_code_button)
    public void actionResetPasswd() {
        String newPasswd = inputView.getText().toString();
        if (newPasswd == null || newPasswd.length() == 0) {
            showMessage("请输入新密码");
            return;
        }
        showWaiting();
        api.updatePassword(phone, code, newPasswd, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if ("update_ok".equals(data)) {
                    showMessage("密码修改成功请登录");
                    updatePasswdSuccess();
                } else {
                    showMessage("请检查手机号");
                }
            }
        });

    }

    private void updatePasswdSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
