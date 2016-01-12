package org.mini.frame.toolkit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MiniTextWatcher implements TextWatcher {
	private CharSequence temp;
	private int selectionStart;
	private int selectionEnd;
	private EditText editText;
	private int textLength;
	private String hintInfo;
	private Context context;

	public MiniTextWatcher(Context context, EditText editText, int textLength, String hintInfo) {
		this.editText = editText;
		this.textLength = textLength;
		this.hintInfo = hintInfo;
		this.context = context;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		temp = s;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		ViewGroup.LayoutParams layoutParams= editText.getLayoutParams();
		editText.setLayoutParams(layoutParams);
	}

	@Override
	public void afterTextChanged(Editable s) {
		selectionStart = editText.getSelectionStart();
		selectionEnd = editText.getSelectionEnd();
		if (temp.length() > textLength) {
			Toast.makeText(context,hintInfo,Toast.LENGTH_SHORT).show();
			s.delete(selectionStart - 1, selectionEnd);
			editText.setText(s);
			editText.setSelection(s.length());
		}
	}

}
