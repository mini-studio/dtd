package org.mini.frame.view;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * Created by admin on 2015/6/24.
 */
public class MiniDatePickerDialog extends DatePickerDialog {
    public MiniDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);

    }

    public MiniDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStop() {
        // super.onStop();
    }
}
