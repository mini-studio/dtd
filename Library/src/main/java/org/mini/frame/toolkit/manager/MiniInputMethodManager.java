package org.mini.frame.toolkit.manager;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Wuquancheng on 15/5/9.
 */
public class MiniInputMethodManager {

    public static void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public static void hidKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
