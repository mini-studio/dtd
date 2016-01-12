package org.mini.frame.view;

import static org.mini.frame.toolkit.MiniDisplayUtil.dip2px;

import org.mini.frame.toolkit.MiniDisplayUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mini.R;


/**
 * Created by Wuquancheng on 15/4/17.
 */
public class MiniChatInputBarView extends RelativeLayout implements TextWatcher, View.OnClickListener {

    public interface MiniChatInputBarListener {
        void onSendText(String text);
        void onStartPickImageFromAlbum();
        void onStartPickImageFromCamera();
        void onSendAudio(String fileName);
    }

    private MiniChatInputBarListener inputBarListener;

    private Button micButton;

    private Button cameraButton;

    private Button imagePickerButton;

    private EditText editText;

    private Button sendButton;

    private View topLine;

    private MiniRecordingButton controlButton;

    private LayoutParams editTextLayoutParams;

    int unit = MiniDisplayUtil.dip2px(10);

    private byte prepareRecording = 0;

    private String draft;

    private int editorRightMarginInNormal = 11*unit - unit/3;

    private int editorRightMarginInEditing = 8*unit;

    private LayoutParams recodingButtonLayoutParams;


    public MiniChatInputBarView(Context context) {
        super(context);
        this.initViews();
    }

    public MiniChatInputBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViews();
    }

    private void initViews() {
        if (isInEditMode()) {
            return;
        }

        int buttonSize = 5*unit;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(1.0f));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topLine = new View(this.getContext());
        topLine.setBackgroundResource(R.color.line_color);
        this.addView(topLine, layoutParams);

        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.leftMargin = 6*unit;
        layoutParams.rightMargin = editorRightMarginInNormal;
        layoutParams.topMargin = unit;
        this.editTextLayoutParams = layoutParams;

        recodingButtonLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        recodingButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        recodingButtonLayoutParams.leftMargin = 6*unit;
        recodingButtonLayoutParams.rightMargin = editorRightMarginInNormal;

        editText = new EditText(this.getContext());
        editText.setId(R.id.chat_input_edit_text);
        editText.setMinLines(1);
        editText.setMaxLines(5);
        editText.setTextSize(MiniDisplayUtil.px2sp(MiniDisplayUtil.dip2px(16)));
        editText.setBackgroundResource(R.drawable.chat_text_normal);
        editText.addTextChangedListener(this);
        editText.setMinHeight(4 * unit);
        this.addView(editText, layoutParams);


        layoutParams = new LayoutParams(buttonSize,buttonSize);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.chat_input_edit_text);
        layoutParams.bottomMargin = unit/2;
        layoutParams.width = 4*unit;
        layoutParams.height = 4*unit;
        layoutParams.leftMargin = unit;
        micButton = new Button(this.getContext());
        micButton.setId(R.id.chat_input_bar_record_btn);
        micButton.setBackgroundResource(R.drawable.chat_record_button_parent);
        micButton.setOnClickListener(this);
        this.addView(micButton, layoutParams);

        layoutParams = new LayoutParams(buttonSize,buttonSize);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.chat_input_edit_text);
        layoutParams.rightMargin = unit;
        layoutParams.width = 4*unit;
        layoutParams.height = 4*unit;
        layoutParams.bottomMargin = unit/2;
        imagePickerButton = new Button(this.getContext());
        imagePickerButton.setId(R.id.chat_input_bar_image_btn);
        imagePickerButton.setBackgroundResource(R.drawable.chat_img_picker_button_parent);
        imagePickerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputBarListener != null) {
                    inputBarListener.onStartPickImageFromAlbum();
                }
            }
        });
        this.addView(imagePickerButton, layoutParams);

        layoutParams = new LayoutParams(buttonSize,buttonSize);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.chat_input_edit_text);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.chat_input_bar_image_btn);
        layoutParams.rightMargin = unit;
        layoutParams.width = 4*unit;
        layoutParams.height = 4*unit;
        layoutParams.bottomMargin = unit/2;
        cameraButton = new Button(this.getContext());
            cameraButton.setBackgroundResource(R.drawable.chat_camera_button_parent);
        cameraButton.setId(R.id.chat_input_bar_camera_btn);
        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputBarListener != null) {
                    inputBarListener.onStartPickImageFromCamera();
                }
            }
        });
        this.addView(cameraButton, layoutParams);
        sendButton = new Button(this.getContext());
        sendButton.getBackground().setAlpha(0);
        sendButton.setText("发送");
        sendButton.setTextSize(16);
            sendButton.setTextColor(getResources().getColor(R.color.common_green));

        layoutParams = new LayoutParams(7*unit,4*unit);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.chat_input_edit_text);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.rightMargin = unit/2;
        layoutParams.bottomMargin = unit/3;
        sendButton.setVisibility(View.GONE);
        this.addView(sendButton, layoutParams);
        sendButton.setOnClickListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = this.editText.getText().length();
        if (length == 0) {
            sendButton.setVisibility(View.GONE);
            cameraButton.setVisibility(View.VISIBLE);
            imagePickerButton.setVisibility(View.VISIBLE);
            this.editTextLayoutParams.rightMargin = editorRightMarginInNormal;
        }
        else {
            sendButton.setVisibility(View.VISIBLE);
            cameraButton.setVisibility(View.GONE);
            imagePickerButton.setVisibility(View.GONE);
            this.editTextLayoutParams.rightMargin = editorRightMarginInEditing;
        }
        this.editText.setLayoutParams(this.editTextLayoutParams);
    }

    public void onClick(View v) {
        if (v.equals(this.sendButton)) {
            this.onSendText();
        }
        else if (v.equals(this.imagePickerButton)) {

        }
        else if (v.equals(this.cameraButton)) {

        }
        else if (v.equals(this.micButton)) {
            onClickRecordButton();
        }
    }

    public void onSendText() {
        if (inputBarListener != null) {
            String string = this.editText.getText().toString().trim();
            inputBarListener.onSendText(string);
            if (!TextUtils.isEmpty(string))
                    this.editText.setText("");

        }
    }

    public void onClickRecordButton() {
        InputMethodManager imm=(InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
        this.requestFocus();
        prepareRecording = (byte)((++prepareRecording)%2);
            this.micButton.setBackgroundResource(prepareRecording == 0 ? R.drawable.chat_record_button_parent : R.drawable.keyboard_selector);
        if (prepareRecording == 1) {
            showControlButton();
        }
        else {
            hideControlButton();
        }
    }

    private void showControlButton() {
        if (this.controlButton != null) {
            this.draft = this.editText.getText().toString();
            this.editText.setText("");
            this.editText.setVisibility(View.INVISIBLE);
            this.controlButton.setVisibility(View.VISIBLE);
            this.controlButton.setHeight(4*unit);
            this.addView(this.controlButton, this.recodingButtonLayoutParams);
            this.controlButton.setText("按住 说话");
        }
    }

    private void hideControlButton() {
        if (this.controlButton != null) {
            this.removeView(this.controlButton);
            this.editText.setVisibility(View.VISIBLE);
            this.controlButton.setVisibility(View.GONE);
            this.editText.setText(this.draft);
            this.editText.setSelection(this.draft.length());
            this.editText.requestFocus();
            this.editText.requestFocusFromTouch();
            this.draft = null;
        }
    }


    public MiniChatInputBarListener getInputBarListener() {
        return inputBarListener;
    }

    public void setInputBarListener(MiniChatInputBarListener inputBarListener) {
        this.inputBarListener = inputBarListener;
    }


    public void setControlButton(final MiniRecordingButton controlButton) {
        this.controlButton = controlButton;
        if (this.controlButton != null) {
                this.controlButton.setBackgroundResource(R.drawable.chat_record_button_shape_selector);
                this.controlButton.setTextSize(16);
                this.controlButton.setTextColor(getResources().getColorStateList(R.color.chat_recording_button_text_selector));
                this.controlButton.setStatusAdapter(new MiniRecordingButton.MiniRecordingButtonStatusAdapter() {
                    @Override
                    public void onMoveOut() {
                            controlButton.setBackgroundResource(R.drawable.teacher_chat_record_button_shape_press);
                        controlButton.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void onMoveIn() {
                            controlButton.setBackgroundResource(R.drawable.teacher_chat_record_button_shape_press);
                        controlButton.setTextColor(Color.WHITE);
                    }

                    public void onTouchUp() {
                            controlButton.setBackgroundResource(R.drawable.teacher_chat_record_button_shape_selector);
                            controlButton.setTextColor(getResources().getColorStateList(R.color.teacher_chat_recording_button_text_selector));
                    }
                });
        }
    }
}
