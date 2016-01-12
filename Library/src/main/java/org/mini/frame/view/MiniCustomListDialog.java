package org.mini.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mini.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gassion on 15/6/19.
 */
public class MiniCustomListDialog {

  private Context context;
  private Dialog dialog;
  private LinearLayout lLayout_content;
  private ScrollView sLayout_content;
  private List<String> sheetItemList = new ArrayList<>();
  private OnClickListener onClickListener;
  private OnDismissListener onDismissListener;
  private Display display;
  private String itemName = "";

  public MiniCustomListDialog(Context context) {
    this.context = context;
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    display = windowManager.getDefaultDisplay();
  }

  public MiniCustomListDialog builder() {
    View view = LayoutInflater.from(context).inflate(R.layout.view_custom_list_dialog, null);
    view.setMinimumWidth(display.getWidth());
    sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
    lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
    dialog = new Dialog(context, R.style.CustomListDialogStyle);
    dialog.setContentView(view);
    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        if (onDismissListener != null) {
          if (itemName.contains(context.getString(R.string.delete))) {
            onDismissListener.onClick(SelectType.type_delete);
          } else if (itemName.contains(context.getString(R.string.collect))) {
            onDismissListener.onClick(SelectType.type_collection);
          } else if (itemName.contains(context.getString(R.string.copy))) {
            onDismissListener.onClick(SelectType.type_copy);
          } else {
            onDismissListener.onClick(SelectType.type_close);
          }
        }

      }
    });
    dialog.setCancelable(true);
    dialog.setCanceledOnTouchOutside(true);
    Window dialogWindow = dialog.getWindow();
    dialogWindow.setGravity(Gravity.CENTER);
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    lp.x = 0;
    lp.y = 0;
    dialogWindow.setAttributes(lp);
    return this;
  }

  public MiniCustomListDialog setItemList(List<String> sheetItemList) {
    this.sheetItemList = sheetItemList;
    return this;
  }

  public MiniCustomListDialog setOnClickListener(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
    return this;
  }

  /**
   * 设置复制
   *
   * @param isCopy
   * @return
   */
  public MiniCustomListDialog setCopy(boolean isCopy) {
    if (isCopy)
      sheetItemList.add(context.getString(R.string.copy));
    return this;
  }

  /**
   * 设置收藏
   *
   * @param isCollection
   * @return
   */
  public MiniCustomListDialog setCollection(boolean isCollection, boolean... homeWork) {
    if (homeWork != null && homeWork.length <= 0) {
      if (!isCollection)
        sheetItemList.add(context.getString(R.string.collect));
      else
        sheetItemList.add(context.getString(R.string.cancel_collect));
    }
    return this;
  }

  /**
   * 设置删除
   *
   * @param isDelete
   * @return
   */
  public MiniCustomListDialog setDelete(boolean isDelete) {
    if (isDelete)
      sheetItemList.add(context.getString(R.string.delete));
    return this;
  }

  public MiniCustomListDialog setOnDismissListener(OnDismissListener onDismissListener) {
    this.onDismissListener = onDismissListener;
    return this;
  }

  private void setSheetItems() {
    if (sheetItemList == null || sheetItemList.size() <= 0) {
      return;
    }

    int size = sheetItemList.size();

    if (size >= 7) {
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayout_content.getLayoutParams();
      params.height = display.getHeight() / 2;
      sLayout_content.setLayoutParams(params);
    }

    for (int i = 1; i <= size; i++) {
      final String name = sheetItemList.get(i - 1);
      TextView textView = new TextView(context);
      textView.setText(name);
      textView.setTextSize(18);
      textView.setTextColor(Color.BLACK);
      textView.setGravity(Gravity.CENTER_VERTICAL);

      if (size == 1) {
        textView.setBackgroundResource(R.drawable.list_select_single_selector);
      } else {
        if (i == 1) {
          textView.setBackgroundResource(R.drawable.list_select_top_selector);
        } else if (i < size) {
          textView.setBackgroundResource(R.drawable.list_select_middle_selector);
        } else {
          textView.setBackgroundResource(R.drawable.list_select_bottom_selector);
        }
      }

      float scale = context.getResources().getDisplayMetrics().density;
      int height = (int) (45 * scale + 0.5f);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (onClickListener!=null){
            dialog.dismiss();
            onClickListener.onClick(name);
          }else{
            itemName = name;
            dialog.dismiss();
          }

        }
      });
      textView.setPadding(40, 0, 0, 0);
      lLayout_content.addView(textView);
    }
  }

  public void show() {
    setSheetItems();
    dialog.show();
  }

  public interface OnDismissListener {
    void onClick(SelectType selectType);
  }

  public interface OnClickListener {
    void onClick(String name);
  }

  public enum SelectType {
    /**
     * 复制
     */
    type_copy,
    /**
     * 删除
     */
    type_delete,
    /**
     * 收藏
     */
    type_collection,
    /**
     * close
     */
    type_close,
  }

}
