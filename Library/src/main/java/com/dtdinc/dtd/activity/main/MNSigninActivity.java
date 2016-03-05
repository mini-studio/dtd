package com.dtdinc.dtd.activity.main;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.api.data.User;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.define.CEConst;
import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.MiniSharedPreferences;
import org.mini.frame.toolkit.manager.MiniInputMethodManager;

/**
 * Created by Wuquancheng on 15/4/6.
 */
public class MNSigninActivity extends MNActivityBase {

    private CEApi api = new CEApi();

    public static String TYPE = "type";

    private static final int REQ_FORGOT_PASSWD = 20000;

    public  static int TYPE_FOR_RESULT = 1;

    private int type = 0;

    private EditText userNameEditText;
    private EditText passwdEditText;
    private Button signinButton;
    private TextView forgotPasswd;
    private ScrollView scrollView;

    private String last;

    public void loadView() {
        this.setContentView(R.layout.activity_signin);
        this.setTitle("登录");
        this.setNaviRightStringAction("注册");
        Integer forType = this.getIntentValue(TYPE);
        if (forType != null) {
            type = forType;
        }
        signinButton = (Button) this.findViewById(R.id.sigin_button);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        userNameEditText = (EditText) this.findViewById(R.id.sigin_user_name);
        last = MiniSharedPreferences.instance().getString(CEConst.CE_LAST_LOGIN_NAME, "");
        userNameEditText.setText(last);
        passwdEditText = (EditText) this.findViewById(R.id.sigin_passwd);
        //password = MiniSharedPreferences.instance().getString(CEConst.CE_LAST_LOGIN_PASSWORD, "");
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        // 注册
        this.addKeyboardListener((ViewGroup) findViewById(R.id.content_view), R.id.scroll);
        this.findViewById(R.id.forgot_passwd, this);
    }

    private void signin() {
        final String name = userNameEditText.getText().toString();
        if (name == null || name.length() == 0) {
            showMessage(R.string.login_id_can_not_empty);
            return;
        }
        final String passwd = passwdEditText.getText().toString();
        if (passwd == null || passwd.length() == 0) {
            showMessage(R.string.password_not_null);
            return;
        }
        if (userNameEditText.hasFocus()) {
            MiniInputMethodManager.hidKeyboard(userNameEditText);
        } else if (passwdEditText.hasFocus()) {
            MiniInputMethodManager.hidKeyboard(passwdEditText);
        }

        api.login(name, passwd, new MiniDataListener<User>() {
            @Override
            public void onResponse(User data, CEDataException error) {
                didLogin(data, error);
            }
        });


    }

    public void didLogin(User data, CEDataException error) {
        if (error != null) {
            showError(error);
            return;
        }
        if (data == null) {
            showMessage("请检查用户名密码");
            return;
        }
        else {
            CESystem.setCurrentUser(data);
        }
        if (type == 0) {
            startActivity(MNMainTabActivity.class);
        }
        else {
            setResult(RESULT_OK);
        }
        finish();
    }

    protected void onKeyboardShow() {
        EditText editText = userNameEditText.hasFocus() ? userNameEditText : passwdEditText;
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        editText.requestFocus();
    }


    protected void onKeyboardHide() {

    }

    @Action(R.id.app_title_right)
    public void onRightAction() {
        startActivity(MNRegisterActivity.class);
    }

    @Action(R.id.forgot_passwd)
    public void actionForgotPasswd() {
        startActivityForResult(MNForgotPasswdActivity.class, null, REQ_FORGOT_PASSWD);
    }

    @ActivityResult(REQ_FORGOT_PASSWD)
    public void resultForgotPasswd(int requestCode, int resultCode, Intent intent) {

    }
}
