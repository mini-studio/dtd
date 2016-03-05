package com.dtdinc.dtd.activity.comm;

import android.os.Bundle;

import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.define.CEConst;

import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.activity.base.MiniActivityBase;

/**
 * Created by Wuquancheng on 15/4/24.
 */
public abstract class MNActivityBase extends MiniActivityBase {

    public static final int REQUEST_DETAIL_CODE = 10000;

    protected CEApi api = new CEApi();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerNotification();
    }

    protected void registerNotification() {
    }

    protected void registerNotificationWithKey(String key) {
        NotificationCenter.defaultNotificationCenter().register(key, this);
    }

    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultNotificationCenter().remove(this);
    }

    protected void onDataChanged() {
        refreshMainTabWithClazz(this.getClass());
    }

    protected void refreshMainTabWithClazz() {
        refreshMainTabWithClazz(this.getClass());
    }
    public static void refreshMainTabWithClazz(Class clazz) {
        NotificationCenter.defaultNotificationCenter().post(CEConst.CE_NOTIFICATION_REFRESH_MAIN_TAB_BADGE, clazz);
    }

}
