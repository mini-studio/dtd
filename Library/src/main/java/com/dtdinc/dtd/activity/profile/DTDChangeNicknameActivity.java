package com.dtdinc.dtd.activity.profile;

import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.api.data.User;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDChangeNicknameActivity extends MNActivityBase {

    private TextView inputView;
    private TextView changeTitleView;
    private CEApi api = new CEApi();

    User user = WHO();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_change_text);
        this.setTitle("修改昵称");
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    private void initView() {
        this.inputView = (TextView)this.findViewById(R.id.change_input_view);
        this.inputView.setText(user.getNickname());
        this.changeTitleView = (TextView)this.findViewById(R.id.change_title_view);
        this.changeTitleView.setText("昵称");
    }

    public void onNaviRightAction() {
        final String nickname = this.inputView.getText().toString();
        if (nickname == null || nickname.length() == 0) {
            showMessage("请输入昵称后保存");
            return;
        }
        showWaiting();
        api.changeNickname(user.getPhone(), nickname, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (error == null) {
                    if ("change_ok".equals(data)) {
                        user.setNickname(nickname);
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
