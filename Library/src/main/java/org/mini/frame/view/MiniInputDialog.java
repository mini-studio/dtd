package org.mini.frame.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mini.R;

import org.mini.frame.toolkit.manager.MiniInputMethodManager;

/**
 * Created by Wuquancheng on 15/5/9.
 */
public class MiniInputDialog {

    private Button cancelButton;

    private Button confirmButton;

    private View dialogVerticalLine;

    private TextView titleView;

    private EditText editText;

    private AlertDialog alterDialog;

    private View contentView;

    private Context context;

    private Handler handler;

    public MiniInputDialog(Context context) {
        super();
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.mini_view_input_dialog_view, null);
        cancelButton = (Button) contentView.findViewById(R.id.dialog_cancel_button);
        confirmButton = (Button) contentView.findViewById(R.id.dialog_confirm_button);
        titleView = (TextView)contentView.findViewById(R.id.dialog_title);
        editText = (EditText)contentView.findViewById(R.id.dialog_input);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        dialogVerticalLine = contentView.findViewById(R.id.dialog_vertical_line);
        handler = new Handler();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void dismiss() {
        if(alterDialog != null) {
            alterDialog.dismiss();
            hidKeyboard();
        }
    }

    public MiniInputDialog setCancelButtonListener(View.OnClickListener listener) {
        cancelButton.setOnClickListener(listener);
        return this;
    }

    public MiniInputDialog setConfirmButtonListener(View.OnClickListener listener) {
        confirmButton.setOnClickListener(listener);
        return this;
    }

    public MiniInputDialog hidCancelButton() {
        cancelButton.setVisibility(View.GONE);
        dialogVerticalLine.setVisibility(View.GONE);
        return this;
    }

    public MiniInputDialog setConfirmButtonText(String buttonTitle) {
        confirmButton.setText(buttonTitle);
        return this;
    }

    public MiniInputDialog setCancelButtonText(String buttonTitle) {
        cancelButton.setText(buttonTitle);
        return this;
    }

    public MiniInputDialog setTitle(String title) {
        titleView.setText(title);
        return this;
    }

    private void hidKeyboard() {
        MiniInputMethodManager.hidKeyboard(editText);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public MiniInputDialog show() {
        alterDialog = new AlertDialog.Builder(context).create();
        alterDialog.show();
        alterDialog.setContentView(contentView);
        alterDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alterDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MiniInputMethodManager.showKeyboard(editText);
            }
        },100);
        return this;
    }
}
