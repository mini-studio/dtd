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

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDBecomeDispatcherActivity extends MNActivityBase {

    private TextView contactPhone;

    private TextView line_id_card_view;
    private TextView line_bank_card_view;
    private TextView line_status_view;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_become_dispatcher);
        this.setTitle("成为自由投递人");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.contact_view, this);
        this.findViewById(R.id.line_bank_card_layout, this);
        this.findViewById(R.id.line_id_card_layout, this);
        this.findViewById(R.id.line_layout, this);
        this.contactPhone = (TextView)this.findViewById(R.id.contact_phone);
        this.line_id_card_view = (TextView)this.findViewById(R.id.line_id_card_view);
        this.line_bank_card_view = (TextView)this.findViewById(R.id.line_bank_card_view);
        this.line_status_view = (TextView)this.findViewById(R.id.line_status_view);
    }

    public void afterResume() {
        loadData();
    }

    private void loadData() {
        User user = WHO();
        this.contactPhone.setText(user.getEmerPhone());
        String card = user.getCard();
        if ("1".equals(card)) {
            this.line_id_card_view.setText("未上传");
        }
        else if ("3".equals(card)) {
            this.line_id_card_view.setText("审核通过");
        }
        else {
            this.line_id_card_view.setText("已上传,审核中");
        }
        String bankCard = user.getBankCard();
        if ("1".equals(bankCard)) {
            this.line_bank_card_view.setText("未上传");
        }
        else if ("3".equals(card)) {
            this.line_bank_card_view.setText("审核通过");
        }
        else {
            this.line_bank_card_view.setText("已上传,审核中");
        }

        int lineCount = user.getLineCount();
        if (lineCount > 0) {
            this.line_status_view.setText("已设置");
        }
        else {
            this.line_status_view.setText("未设置");
        }
    }

    @Action(R.id.contact_view)
    private void changeMobile() {
        startActivity(DTDChangeMobileActivity.class);
    }

    @Action(R.id.upload_card_view)
    private void uploadCard() {
        User user = WHO();
        if ("1".equals(user.getCard())) {
            pickImage(new MiniPickImageListener() {
                @Override
                public void onPickImage(String file) {
                    onPickImageFile(file, "card");
                }
            });
        }
    }


    public void onPickImageFile(String file, final String type) {
        final User user = WHO();
        String serverPath = "http://api.dtd.la/index.php/upload/"+type+"/" + user.getPhone() + "/" + user.getSid();
        MiniFileUploadManager uploadManager = MiniFileUploadManager.instance();
        uploadManager.setFileServerPath(serverPath);
        showWaiting();
        uploadManager.upload(file, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if (!"session_error".equals(data)) {
                    if ("card".equals(type)) {
                        user.setCard(CEAppConfig.imageServer + data);
                    }
                    else {
                        user.setBankCard(CEAppConfig.imageServer + data);
                    }
                    user.save();

                    loadData();
                }
                else {
                    showMessage("上传图片失败");
                }
            }
        });
    }

    /**
     * 上传身份证
     */
    @Action(R.id.line_id_card_layout)
    public void actionForUploadIdCard() {
        User user = WHO();
        if ("1".equals(user.getCard())) {
            pickImage(new MiniPickImageListener() {
                @Override
                public void onPickImage(String file) {
                    onPickImageFile(file, "card");
                }
            });
        }
    }

    /**
     * 上传银行卡
     */
    @Action(R.id.line_bank_card_layout)
    public void actionForUploadBankCard() {
        User user = WHO();
        if ("1".equals(user.getBankCard())) {
        pickImage(new MiniPickImageListener() {
            @Override
            public void onPickImage(String file) {
                onPickImageFile(file, "bank_card");
            }
        });
        }
    }

    @Action(R.id.line_layout)
    public void actionForLineLayout() {
        startActivity(DTDLinesActivity.class);
    }
}
