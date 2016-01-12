package org.mini.frame.toolkit;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.mini.R;

import org.mini.frame.view.MiniCustomDialog;
import org.mini.frame.wheelview.WheelMain;

/**
 * Created by gassion on 15/6/15.
 */
public class MiniTimeHomeWorkPicker {

    public interface MiniTimePickerListener {
        void selectLevelMinutes(int level, int minutes);
    }

    private Context context;

    private WheelMain wheelMain;

    private MiniTimePickerListener timePickerListener;

    public MiniTimeHomeWorkPicker(Context context, MiniTimePickerListener timePickerListener) {
        this.context = context;
        this.timePickerListener = timePickerListener;
    }


    public View initWheelMain() {
        View timePickerView = View.inflate(context, R.layout.view_homework_feedback_dialog, null);
        wheelMain = new WheelMain(timePickerView);
        wheelMain.initDateTimePicker();
        return timePickerView;
    }

    /**
     * 选择时间
     */
    private void showPicker() {
        new MiniCustomDialog.Builder(context).setContentView(initWheelMain())
                .setBackButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                }).setConfirmButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timePickerListener.selectLevelMinutes(wheelMain.getLevel(), wheelMain.getMinutes());
                dialog.dismiss();
            }
        }).create().show();
    }

    public void show() {
        showPicker();
    }
}
