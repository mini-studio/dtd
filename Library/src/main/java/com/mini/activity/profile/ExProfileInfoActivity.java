package com.mini.activity.profile;

import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.app.CEAppConfig;
import com.mini.core.api.data.User;
import com.mini.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.file.MiniFileUploadManager;
import org.mini.frame.view.MiniImageView;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class ExProfileInfoActivity extends MNActivityBase {

    private TextView nicknameView;
    private MiniImageView imageView;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_profile_info_x);
        this.setTitle("个人信息");
        this.initView();
        this.loadData();
    }

    private void initView() {
        this.findViewById(R.id.layout_avatar, this);
        this.findViewById(R.id.layout_nickname, this);
        this.findViewById(R.id.layout_passwd, this);
        this.nicknameView = (TextView)this.findViewById(R.id.nickname_view);
        this.imageView = (MiniImageView)this.findViewById(R.id.avatar_image_view);
    }

    public void afterResume() {
        loadData();
    }

    private void loadData() {
        User user = WHO();
        this.nicknameView.setText(user.getNickname());
        this.imageView.setImageUrl(user.getHead(),R.drawable.default_avatar);
    }

    @Action(R.id.layout_avatar)
    public void onClickLayoutAvatar() {
        pickImage(new MiniPickImageListener() {
            @Override
            public void onPickImage(String file) {
                onPickImageFile(file);
            }
        });
    }

    public void onPickImageFile(String file) {
        final User user = WHO();
        String serverPath = "http://api.dtd.la/index.php/upload/head/" + user.getPhone() + "/" + user.getSid();
        MiniFileUploadManager uploadManager = MiniFileUploadManager.instance();
        uploadManager.setFileServerPath(serverPath);
        showWaiting();
        uploadManager.upload(file, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (!"session_error".equals(data)) {
                    user.setHead(data);
                    user.save();
                    loadData();
                }
                else {
                    showMessage("上传图片失败");
                }
            }
        });
    }

    @Action(R.id.layout_nickname)
    public void onClickLayoutNickname() {
        startActivity(ExChangeNicknameActivity.class);
    }

    @Action(R.id.layout_passwd)
    public void onClickLayoutPasswd() {
        startActivity(ExChangePasswdActivity.class);
    }

}
