package com.dtdinc.dtd.activity.post;

import android.widget.EditText;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;

import org.mini.frame.toolkit.MiniKeyboardUtils;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDEditorActivity extends MNActivityBase {

    public static class EditorObject {
        private String title;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {

            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private EditText inputView;

    private EditorObject editorObject;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_change_text_x);
        editorObject = (EditorObject)this.getIntentObject();
        if (editorObject != null) {
            this.setTitle(editorObject.title);
        }
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    private void initView() {
        this.inputView = (EditText)this.findViewById(R.id.edit_text_content);
        if (this.editorObject != null) {
            this.inputView.setText(this.editorObject.getContent());
        }
    }

    public void onNaviRightAction() {
        if (this.editorObject != null) {
            this.editorObject.setContent(this.inputView.getText().toString());
        }
        MiniKeyboardUtils.hideKeyboard(this.inputView);
        setResult(RESULT_OK);
        finish();
    }

}
