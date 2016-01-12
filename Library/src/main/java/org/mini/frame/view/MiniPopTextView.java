package org.mini.frame.view;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.R;

import org.mini.frame.toolkit.manager.MiniInputMethodManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/5/2.
 */
public class MiniPopTextView extends LinearLayout implements View.OnClickListener{

    public interface MiniPopTextViewListener {
        String title();
        String defaultContent();
        int textLines();
        int textLength();
        String hintText();
        boolean didInput(String content);
    }

    private View contentView;
    private ImageView cancelImageView;
    private ImageView confirmImageView;
    private TextView titleTextView;
    private EditText editText;

    private RelativeLayout parent;

    private MiniPopTextViewListener popTextViewListener;

    public MiniPopTextView(Context context,MiniPopTextViewListener popTextViewListener) {
        super(context);
        this.popTextViewListener = popTextViewListener;
        this.initViews();
    }

    private void initViews() {
        this.setFocusable(true);
        contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.mini_view_pop_text_view, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(contentView,layoutParams);
        cancelImageView = (ImageView) contentView.findViewById(R.id.cancel_button);
        confirmImageView = (ImageView) contentView.findViewById(R.id.confirm_button);
        titleTextView = (TextView) contentView.findViewById(R.id.text_label);
        editText = (EditText) contentView.findViewById(R.id.text_view);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        String hintText = popTextViewListener.hintText();
        if (hintText != null && hintText.length() > 0) {
            editText.setHint(hintText);
        }
        else {
            editText.setHint("");
        }
        int length = popTextViewListener.textLength();
        if (length > 0) {
            List<InputFilter> lst = new ArrayList<>();
            lst.add(new InputFilter.LengthFilter(length));
            InputFilter[] filters = editText.getFilters();
            if (filters != null && filters.length > 0) {
                for (InputFilter filter : filters) {
                    if (filter instanceof InputFilter.LengthFilter) {
                        continue;
                    }
                    else {
                        lst.add(filter);
                    }
                }
            }
            InputFilter[] inputFilters = lst.toArray(new InputFilter[lst.size()]);
            editText.setFilters(inputFilters);
        }
        int textLines = popTextViewListener.textLines();
        editText.setSingleLine(textLines == 1);
        if (textLines > 0) {
            editText.setMaxLines(textLines);
        }
        cancelImageView.setOnClickListener(this);
        confirmImageView.setOnClickListener(this);
    }

    public void show(RelativeLayout parent) {
        if (this.parent != null) {
            this.parent.removeView(this);
        }
        String title = popTextViewListener.title();
        titleTextView.setText(title);
        String contentText = popTextViewListener.defaultContent();
        if (contentText != null) {
            editText.setText(contentText);
        }
        this.parent = parent;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        parent.addView(this, params);
        contentView.setVisibility(View.VISIBLE);
        MiniInputMethodManager.showKeyboard(editText);
    }

    public void dismiss() {
        MiniInputMethodManager.hidKeyboard(editText);
        contentView.setVisibility(View.GONE);
    }
    public void cancel() {
        dismiss();
    }

    public void onClick(View v) {
        if (v.equals(this.cancelImageView)) {
            cancel();
        }
        else if (v.equals(this.confirmImageView)) {
            String content = editText.getText().toString();
            if (popTextViewListener.didInput(content)) {
                dismiss();
            }
        }
    }
}
