package org.mini.frame.toolkit.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

/**
 * Created by Wuquancheng on 15/5/9.
 */
public class MiniAppManager {
    private static MiniAppManager appManager = null;

    private static Context context;

    private MiniAppManager(Context context) {
        this.context = context;
    }

    public static MiniAppManager instance(Context context) {
        synchronized (MiniAppManager.class) {
            if (appManager == null) {
                appManager = new MiniAppManager(context);
            }
        }
        return appManager;
    }

    public static String getAppMetaData(String key) throws Exception {
        ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        if (ai != null && ai.metaData != null) {
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
