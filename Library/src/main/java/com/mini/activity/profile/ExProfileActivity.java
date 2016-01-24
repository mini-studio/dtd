package com.mini.activity.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.activity.main.MNSigninActivity;
import com.mini.activity.send.ExFeedbackActivity;
import com.mini.app.CEAppConfig;
import com.mini.app.CESystem;
import com.mini.core.api.data.User;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.toolkit.MiniSystemHelper;
import org.mini.frame.view.MiniCustomDialog;
import org.mini.frame.view.MiniImageView;
import org.w3c.dom.Text;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class ExProfileActivity extends MNActivityBase {

    private TextView profile_name_view;
    private MiniImageView avatar_image_view;
    private Button logoutButton;
    private TextView waiter_mobile_view;
    public static String mobile = "010-65675716";
    private final int REQ_LOGIN = 10000;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_profile);
        //this.setTitle("我的");
        this.setTitleMidImage(R.drawable.asf_03);
        this.hiddenLeftBackButton();
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.profile_layout, this);
        this.findViewById(R.id.become_dispatcher_layout, this);
        this.findViewById(R.id.order_list_layout, this);
        this.findViewById(R.id.layout_contact_waiter, this);
        this.findViewById(R.id.setting_layout, this);
        this.findViewById(R.id.profile_avatar_item, this);
        this.waiter_mobile_view = (TextView)this.findViewById(R.id.waiter_mobile_view);
        this.logoutButton = (Button)this.findViewById(R.id.button_logout, this);
        this.profile_name_view = (TextView)this.findViewById(R.id.profile_name_view, this);
        this.avatar_image_view = (MiniImageView)this.findViewById(R.id.avatar_image_view, this);
        this.waiter_mobile_view.setText(mobile);
        loadUserInfo();
    }

    private void loadUserInfo() {
        User user = WHO();
        if (user != null) {
            if (user.getNickname() != null) {
                profile_name_view.setText(user.getNickname());
            }
            if (user.getHead() != null) {
                String url = user.getHead();
                avatar_image_view.setImageUrl(url, R.drawable.default_avatar);
            }
            else {
                avatar_image_view.setImageResource(R.drawable.default_avatar);
            }
            logoutButton.setText("退出当前用户");
        }
        else {
            avatar_image_view.setImageResource(R.drawable.default_avatar);
            profile_name_view.setText(getResources().getString(R.string.app_name));
            logoutButton.setText("登录");
        }
    }

    public void afterResume() {
        super.afterResume();
        loadUserInfo();
    }

    private boolean checkUser() {
        return checkUser(false);
    }

    private boolean checkUser(boolean directLogin) {
        User user = WHO();
        if (user == null) {
            if (directLogin) {
                startActivity(MNSigninActivity.class);
                return false;
            }
            else {
                MiniCustomDialog.show(this, "提示", "请您先登录", "取消", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(MNSigninActivity.class);
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Action(R.id.profile_layout)
    public void onClickProfileLayout() {
        if (checkUser()) {
            startActivity(ExProfileInfoActivity.class);
        }
    }

    @Action(R.id.become_dispatcher_layout)
    public void onClickBecomeDispatcherLayout() {
        if (checkUser()) {
            startActivity(ExBecomeDispatcherActivity.class);
        }
    }

    /**
     * 我的快件
     */
    @Action(R.id.order_list_layout)
    public void onClickOrderListLayout() {
        if (checkUser()) {
            startActivity(ExOrderListIndexActivity.class);
        }
    }

    /**
     * 联系客服
     */
    @Action(R.id.layout_contact_waiter)
    public void feedback() {
        MiniSystemHelper.call(this.mobile, this);
    }

    /**
     * 设置
     */
    @Action(R.id.setting_layout)
    public void onClickSettingLayoutLayout() {
        startActivity(ExSettingActivity.class);
    }

    /**
     * 退出
     */
    @Action(R.id.button_logout)
    public void actionLogout() {
        User user = WHO();
        if (user != null) {
            CESystem.setCurrentUser(null);
            loadUserInfo();
            this.logoutButton.setText("登录");
        }
        else {
            Intent intent = new Intent(ExProfileActivity.this, MNSigninActivity.class);
            intent.putExtra(MNSigninActivity.TYPE, MNSigninActivity.TYPE_FOR_RESULT);
            startActivityForResult(intent, REQ_LOGIN);
        }
    }

    /**
     * 登录
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @ActivityResult(REQ_LOGIN)
    public void resultForLogin(int requestCode, int resultCode, Intent intent) {
        if (resultCode ==  RESULT_OK) {
            loadUserInfo();
        }
    }

    @Action(R.id.profile_name_view)
    public void actionProfileName() {
        if (checkUser(true)) {
            startActivity(ExProfileInfoActivity.class);
        }
    }

    @Action(R.id.avatar_image_view)
    public void actionAvatarImageView() {
        if (checkUser(true)) {
            startActivity(ExProfileInfoActivity.class);
        }
    }

    @Action(R.id.profile_avatar_item)
    public void actionOnAvatarView() {
        if (checkUser(true)) {
            startActivity(ExProfileInfoActivity.class);
        }
    }
}
