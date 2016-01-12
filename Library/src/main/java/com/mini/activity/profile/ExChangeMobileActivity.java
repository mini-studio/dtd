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
public class ExChangeMobileActivity extends MNActivityBase {

    private TextView inputView;
    private CEApi api = new CEApi();
    User user = WHO();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_change_text);
        this.setTitle("修改联系电话");
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    private void initView() {
        this.inputView = (TextView)this.findViewById(R.id.change_input_view);
        this.inputView.setText(user.getEmerPhone());
    }

    public void onNaviRightAction() {
        final String emerPhone = this.inputView.getText().toString();
        if (emerPhone == null || emerPhone.length() == 0) {
            showMessage("请输入电话后保存");
            return;
        }
        showWaiting();
        api.changEmergencyPhone(user.getPhone(), emerPhone, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (error == null) {
                    if ("change_ok".equals(data)) {
                        user.setEmerPhone(emerPhone);
                        user.save();
                        back();
                    }
                    else {
                        showMessage("修改电话失败["+data+"]");
                    }
                }
                else {
                    showError(error);
                }
            }
        });
    }

}
