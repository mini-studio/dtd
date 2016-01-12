package org.mini.frame.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by gassion on 2015/4/23
 */
public class MiniCustomDialog extends Dialog {

  public MiniCustomDialog(Context context, int theme) {
    super(context, theme);
  }

  public MiniCustomDialog(Context context) {
    super(context);
  }

  /**
   * Helper class for creating a custom dialog
   */
  public static class Builder {
    private Context context;
    private String title; // 对话框标题
    private String message; // 对话框内容
    private String backButtonText; // 对话框返回按钮文本
    private String confirmButtonText; // 对话框确定文本
    private View contentView;
    private MiniCustomDialog dialog;

    // 对话框按钮监听事件
    private OnClickListener backButtonClickListener, confirmButtonClickListener;

    public Builder(Context context) {
      this.context = context;
    }

    /**
     * 使用字符串设置对话框消息
     *
     * @return
     */
    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }

    /**
     * 使用资源设置对话框消息
     *
     * @return
     */
    public Builder setMessage(int message) {
      this.message = (String) context.getText(message);
      return this;
    }

    /**
     * 使用资源设置对话框标题信息
     *
     * @param title
     * @return
     */
    public Builder setTitle(int title) {
      this.title = (String) context.getText(title);
      return this;
    }

    /**
     * 使用字符串设置对话框标题信息
     *
     * @param title
     * @return
     */
    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * 设置自定义的对话框内容
     *
     * @param v
     * @return
     */
    public Builder setContentView(View v) {
      this.contentView = v;
      return this;
    }

    /**
     * 设置back按钮的事件和文本
     *
     * @param backButtonText
     * @param listener
     * @return
     */
    public Builder setBackButton(int backButtonText, OnClickListener listener) {
      this.backButtonText = (String) context.getText(backButtonText);
      this.backButtonClickListener = listener;
      return this;
    }

    /**
     * 设置back按钮的事件和文本
     *
     * @param backButtonText
     * @param listener
     * @return
     */
    public Builder setBackButton(String backButtonText, OnClickListener listener) {
      this.backButtonText = backButtonText;
      this.backButtonClickListener = listener;
      return this;
    }

    /**
     * 设置确定按钮事件和文本
     *
     * @param confirmButtonText
     * @param listener
     * @return
     */
    public Builder setConfirmButton(int confirmButtonText, OnClickListener listener) {
      this.confirmButtonText = (String) context.getText(confirmButtonText);
      this.confirmButtonClickListener = listener;
      return this;
    }

    /**
     * 设置确定按钮事件和文本
     *
     * @param listener
     * @return
     */
    public Builder setConfirmButton(String confirmButtonText, OnClickListener listener) {
      this.confirmButtonText = confirmButtonText;
      this.confirmButtonClickListener = listener;
      return this;
    }

    public void cancel() {
      if (dialog != null) {
        dialog.dismiss();
        dialog.cancel();
      }
    }

    /**
     * 创建自定义的对话框
     */
    public MiniCustomDialog create(Boolean... flag) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 实例化自定义的对话框主题
      dialog = new MiniCustomDialog(context, R.style.Dialog);
      if (flag.length > 1) {
        dialog.setCanceledOnTouchOutside(false);
      } else {
        if (flag.length > 0)
          dialog.setCancelable(false);
      }
      View layout = inflater.inflate(R.layout.view_custom_dialog, null);
      LinearLayout layoutButtonBg = (LinearLayout) layout.findViewById(R.id.dialog_button_bg);
      dialog.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

      LinearLayout topLayout = (LinearLayout) layout.findViewById(R.id.dialog_top_layout);
      if (!TextUtils.isEmpty(title)) {
        topLayout.setVisibility(View.VISIBLE);
        // 设置对话框标题
        TextView tvTitle = ((TextView) layout.findViewById(R.id.dialog_title));
        tvTitle.setText(title);
          tvTitle.setTextColor(context.getResources().getColor(R.color.home_work_completion));
      } else {
        topLayout.setVisibility(View.GONE);
      }

      View viewLine =layout.findViewById(R.id.dialog_line);
        viewLine.setBackgroundColor(context.getResources().getColor(R.color.home_work_completion));

      // 设置对话框内容
      if (message != null) {
        TextView dlgMsg = (TextView) layout.findViewById(R.id.dialog_message);
        dlgMsg.setText(message);
        dlgMsg.setTextColor(context.getResources().getColor(R.color.common_action_view_text));
      } else if (contentView != null) {
        // if no message set
        // 如果没有设置对话框内容，添加contentView到对话框主体
        ((LinearLayout) layout.findViewById(R.id.dialog_content)).removeAllViews();
        ((LinearLayout) layout.findViewById(R.id.dialog_content)).addView(contentView, new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
      }

      if (backButtonText == null && confirmButtonText == null) {
        layout.findViewById(R.id.custom_dialog_divider).setVisibility(View.GONE);
      }

      // 设置返回按钮事件和文本
      Button bckButton = ((Button) layout.findViewById(R.id.dialog_back));
      if (!TextUtils.isEmpty(backButtonText)) {
        layoutButtonBg.setVisibility(View.VISIBLE);
        bckButton.setTextColor(context.getResources().getColor(R.color.common_action_view_text));

        bckButton.setText(backButtonText);

        if (backButtonClickListener != null) {
          bckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              backButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
            }
          });
        }
      } else {
        layout.findViewById(R.id.dialog_back).setVisibility(View.GONE);
        layoutButtonBg.setVisibility(View.GONE);
      }

      // 设置确定按钮事件和文本
      Button cfmButton = ((Button) layout.findViewById(R.id.dialog_confirm));
      if (!TextUtils.isEmpty(confirmButtonText)) {
        layoutButtonBg.setVisibility(View.VISIBLE);
        cfmButton.setText(confirmButtonText);
          cfmButton.setTextColor(context.getResources().getColor(R.color.home_work_completion));
        if (confirmButtonClickListener != null) {
          cfmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              confirmButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            }
          });
        }
      } else {
        layout.findViewById(R.id.dialog_confirm).setVisibility(View.GONE);
      }

      View view = layout.findViewById(R.id.dialog_button_div);

      if (confirmButtonText == null && backButtonText != null) {
        view.setVisibility(View.GONE);
        bckButton.setBackgroundResource(R.drawable.cutom_dialog_center_button_selecter);
      } else if (confirmButtonText != null && backButtonText == null) {
        view.setVisibility(View.GONE);
        cfmButton.setBackgroundResource(R.drawable.cutom_dialog_center_button_selecter);
      }

      Window dialogWindow = dialog.getWindow();
      WindowManager.LayoutParams lp = dialogWindow.getAttributes();
      dialogWindow.setGravity(Gravity.CENTER);

      lp.width = getScreenWidth(context) - getScreenWidth(context) / 6; // 宽度

      dialogWindow.setAttributes(lp);
      dialog.setContentView(layout);

      return dialog;
    }

    public int getScreenWidth(Context context) {
      DisplayMetrics dm = new DisplayMetrics();
      ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
      int screenWidth = dm.widthPixels;
      return screenWidth;
    }

  }

  public static void show(Context context, String title, String message, String confirmButtonText) {
    show(context, title, message, null, null, confirmButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  public static void show(Context context, String title, String message, String confirmButtonText,
      DialogInterface.OnClickListener confirmListener) {
      show(context, title, message, null, null, confirmButtonText, confirmListener);
  }

  public static void show(Context context, String title, String message, String cancelButtonTitle,
        DialogInterface.OnClickListener cancelListener, String confirmButtonText,
        DialogInterface.OnClickListener confirmListener) {
        MiniCustomDialog.Builder builder_updater = new MiniCustomDialog.Builder(context);
        builder_updater.setTitle(title);
        builder_updater.setMessage(message);
        if (confirmListener != null) {
          builder_updater.setConfirmButton(confirmButtonText, confirmListener);
        }
        if (cancelListener != null) {
          builder_updater.setBackButton(cancelButtonTitle, cancelListener);
        }
        MiniCustomDialog dialog = builder_updater.create(false);
        dialog.setOnKeyListener(new OnKeyListener() {
          @Override
          public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
              return true;
          }
        });
        dialog.show();
  }

    public static void show(Context context, String title, String message,String cancelButtonText, String confirmButtonText,
                            DialogInterface.OnClickListener confirmListener) {
        show(context, title, message, cancelButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, confirmButtonText, confirmListener);
    }

}
