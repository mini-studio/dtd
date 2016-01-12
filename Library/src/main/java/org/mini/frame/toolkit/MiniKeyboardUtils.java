package org.mini.frame.toolkit;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Wuquancheng on 15/6/9.
 */
public class MiniKeyboardUtils {
  private static View lastView;

  // 显示虚拟键盘
  public static void showKeyboard(View v) {
    v.setFocusable(true);
    v.setFocusableInTouchMode(true);
    v.requestFocus();
    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    lastView = v;
  }

  // 隐藏虚拟键盘
  public static void hideKeyboard(View v) {
    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    lastView = null;
  }

  public static void hideKeyboard() {
    if (lastView != null) {
      hideKeyboard(lastView);
    }
  }
}
