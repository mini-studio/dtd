package com.mini.activity.send;

import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.MiniJsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDFeedbackActivity extends MNActivityBase {

    private TextView inputView;
    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_feedback);
        Map object = (Map)getIntentObject();
        this.initView();
        if (object != null) {
            String title = (String)object.get("title");
            String hint = (String)object.get("hint");
            this.setTitle(title);
            this.inputView.setHint(hint);
        }
        else {
            this.setTitle("联系客服");
        }
    }

    private void initView() {
        this.inputView = (TextView)this.findViewById(R.id.input_view);

        this.findViewById(R.id.button_commit, this);
    }

    @Action(R.id.button_commit)
    public void commit() {
        String content = this.inputView.getText().toString();
        if (content == null && content.length() == 0) {
            showMessage("请输入您的问题后提交");
            return;
        }
        api.feedback(content, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if (error != null) {
                    showError(error);
                }
                else {
                    if (data.indexOf("255")== -1) {
                        showMessage("非法请求");
                    }
                    else {
                        showMessage("提交成功");
                        back();
                    }
                }
            }
        });
    }
}
