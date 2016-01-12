package org.mini.frame.toolkit;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by admin on 2015/6/17.
 */
public class MiniUtils {
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.heightPixels;
        return screenWidth;
    }

}
