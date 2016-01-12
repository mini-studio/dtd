package org.mini.frame.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by Wuquancheng on 15/5/2.
 */
public class MiniDialog {

    private Button cancelButton;

    private Button confirmButton;

    private View dialogVerticalLine;

    private TextView titleView;

    private TextView messageView;

    private AlertDialog alterDialog;


    private View contentView;

    private Context context;

    public MiniDialog(Context context) {
        super();
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.mini_view_dialog_view, null);
        cancelButton = (Button) contentView.findViewById(R.id.dialog_cancel_button);
        confirmButton = (Button) contentView.findViewById(R.id.dialog_confirm_button);
        titleView = (TextView)contentView.findViewById(R.id.dialog_title);
        messageView = (TextView)contentView.findViewById(R.id.dialog_message);
        dialogVerticalLine = contentView.findViewById(R.id.dialog_vertical_line);
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
        }
    }

    public MiniDialog setCancelButtonListener(View.OnClickListener listener) {
        cancelButton.setOnClickListener(listener);
        return this;
    }

    public MiniDialog setConfirmButtonListener(View.OnClickListener listener) {
        confirmButton.setOnClickListener(listener);
        return this;
    }

    public MiniDialog hidCancelButton() {
        cancelButton.setVisibility(View.GONE);
        dialogVerticalLine.setVisibility(View.GONE);
        return this;
    }

    public MiniDialog setConfirmButtonText(String buttonTitle) {
        confirmButton.setText(buttonTitle);
        return this;
    }

    public MiniDialog setCancelButtonText(String buttonTitle) {
        cancelButton.setText(buttonTitle);
        return this;
    }

    public MiniDialog setTitle(String title) {
        titleView.setText(title);
        return this;
    }

    public MiniDialog setMessage(String message) {
        messageView.setText(message);
        messageView.setVisibility(View.VISIBLE);
        return this;
    }

    public MiniDialog show() {
        alterDialog = new AlertDialog.Builder(context).create();
        alterDialog.show();
        alterDialog.setContentView(contentView);
        return this;
    }
}
