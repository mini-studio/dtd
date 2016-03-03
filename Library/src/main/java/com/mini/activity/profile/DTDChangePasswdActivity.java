package com.mini.activity.profile;

import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.User;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDChangePasswdActivity extends MNActivityBase {

    private TextView oriInputView;
    private TextView newInputView;
    private CEApi api = new CEApi();

    User user = WHO();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_change_passwd);
        this.setTitle("修改密码");
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    private void initView() {
        this.oriInputView = (TextView)this.findViewById(R.id.ori_input_view);
        this.newInputView = (TextView)this.findViewById(R.id.new_input_view);
    }

    public void onNaviRightAction() {
        final String oriPasswd = this.oriInputView.getText().toString();
        if (oriPasswd == null || oriPasswd.length() == 0) {
            showMessage("请输入新密码后保存");
            return;
        }
        final String newPasswd = this.newInputView.getText().toString();
        if (!oriPasswd.equals(newPasswd)) {
            showMessage("请确保两次密码一样");
            return;
        }
        showWaiting();
        api.changePassword(user.getPhone(), oriPasswd, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (error == null) {
                    if ("change_ok".equals(data)) {
                        showMessage("密码修改成功");
                        user.save();
                        back();
                    } else {
                        showMessage("修改昵称失败[" + data + "]");
                    }
                } else {
                    showError(error);
                }
            }
        });
    }

}
