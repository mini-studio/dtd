package org.mini.frame.toolkit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import org.mini.frame.activity.base.MiniActivityBase;
import org.mini.frame.activity.base.MiniFragmentActivityBase;

import java.util.List;

/**
 * Created by Wuquancheng on 15/5/4.
 */
public class MiniActivityManager {

    public enum APP_STATUS {
        APP_STATUS_NOT_RUNNING(0), //没有运行
        APP_STATUS_BACKGROUND(1),  //后台运行
        APP_STATUS_FOREGROUND(2);  //前台运行
        int v;
        APP_STATUS(int value) {
            this.v = value;
        }
    }

    public static Context currentActivity;

    public static boolean appIsActive(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        if (rti != null && rti.size() > 0) {
            return packageName.equals(rti.get(0).topActivity.getPackageName());
        }
        else {
            return false;
        }
    }

    public static APP_STATUS appStatus(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return APP_STATUS.APP_STATUS_BACKGROUND;
                } else {
                    return APP_STATUS.APP_STATUS_FOREGROUND;
                }
            }
        }
        return APP_STATUS.APP_STATUS_NOT_RUNNING;
    }

    public static boolean isTopPushActivity(Context content, Class clazz) {
        ActivityManager mActivityManager = (ActivityManager) content.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return clazz.getClass().getName().equals((rti.get(0).topActivity.getClassName()));
    }

    public static void showWaiting(Activity activity) {
        if (activity instanceof MiniActivityBase) {
            ((MiniActivityBase) activity).showWaiting();
        }
        else if (activity instanceof MiniFragmentActivityBase) {
            ((MiniFragmentActivityBase)activity).showWaiting();
        }
    }

    public static void dismissWaiting(Activity activity) {
        if (activity instanceof MiniActivityBase) {
            ((MiniActivityBase) activity).dismissWaiting();
        }
        else if (activity instanceof MiniFragmentActivityBase) {
            ((MiniFragmentActivityBase)activity).dismissWaiting();
        }
    }
}
