package org.mini.frame.toolkit;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.mini.R;

import org.mini.frame.view.MiniCustomDialog;
import org.mini.frame.wheelview.WheelMain;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wuquancheng on 15/5/2.
 */
public class MiniTimePicker {

    public interface MiniTimePickerListener {
        Date defaultData();
        boolean didSelectDate(Date date);
        String format();
    }

    private Context context;

    private WheelMain  wheelMain;

    private MiniTimePickerListener timePickerListener;

    public MiniTimePicker(Context context, MiniTimePickerListener timePickerListener) {
        this.context = context;
        this.timePickerListener = timePickerListener;
    }


    public View initWheelMain() {
        View  timePickerView = View.inflate(context, R.layout.timepicker, null);
        wheelMain = new WheelMain(timePickerView);
        Calendar calendar = Calendar.getInstance();
        Date date = timePickerListener.defaultData();
        if (date != null) {
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        String format = timePickerListener.format();
        if ("yyyyMMddHHmi".equals(format)) {
            wheelMain.initDateTimePicker(year, month, day, hour, mi);
        }
        else {
            wheelMain.initDateTimePicker(year, month, day, hour);
        }
        return timePickerView;
    }

    /**
     * 选择时间
     */
    private void showPicker() {
        new MiniCustomDialog.Builder(context).setContentView(initWheelMain()).setTitle("选择时间")
            .setBackButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            }).setConfirmButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Date date = wheelMain.getSelectedDate();
                    if (timePickerListener.didSelectDate(date)) {
                        dialog.dismiss();
                    }
                }
        }).create().show();
    }

    public void show() {
        showPicker();
    }
}
