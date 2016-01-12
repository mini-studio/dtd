package org.mini.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mini.R;

import java.util.ArrayList;
import java.util.List;

public class MiniActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<String> sheetItemList;
    private OnSheetItemClickListener listener;
    private Display display;

    public MiniActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public MiniActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_list_select_dialog, null);
        view.setMinimumWidth(display.getWidth());
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setTextColor(Color.DKGRAY);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.ListSelectDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public MiniActionSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setTextColor(Color.BLUE);
        txt_title.setTextSize(15);
        txt_title.setText(title);
        return this;
    }

    public MiniActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public MiniActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public MiniActionSheetDialog addSheetItem(List<String> sheetItemList, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<String>();
        } else {
            this.sheetItemList = sheetItemList;
        }
        this.listener = listener;
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        if (size >= 7) {
            LayoutParams params = (LayoutParams) sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        for (int i = 1; i <= size; i++) {
            final int index = i;
            String name = sheetItemList.get(i - 1);

            TextView textView = new TextView(context);
            textView.setText(name);
            textView.setTextSize(18);
            textView.setTextColor(Color.DKGRAY);
            textView.setGravity(Gravity.CENTER);

            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.list_select_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.list_select_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.list_select_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.list_select_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.list_select_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.list_select_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.list_select_bottom_selector);
                    }
                }
            }

            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index - 1);
                    dialog.dismiss();
                }
            });

            lLayout_content.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }
}
