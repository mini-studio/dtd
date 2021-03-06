package com.dtdinc.dtd.app;

import android.os.Handler;

import com.mini.R;
import com.dtdinc.dtd.core.api.engine.CEApi;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import org.mini.frame.application.MiniApplication;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.toolkit.MiniSharedPreferences;
import org.mini.frame.toolkit.manager.MiniAppManager;

/**
 * Created by Wuquancheng on 15/4/3.
 */
public class CEApplication extends MiniApplication {

    private static CEApplication application = null;

    public static CEApplication instance() {
        return application;
    }

    private CESystem system = null;

    private Handler handler = new Handler();


    public Handler globalHandler() {
        return handler;
    }

    public void onCreate() {
        super.onCreate();
        application = this;
        MiniAppManager.instance(application);
        try {
            String appType = MiniAppManager.getAppMetaData("app_type");
            system = CESystem.instance(application, appType);
            CESystem.channel = getAppChannel();
            CESystem.appId = getAppId();
            CEAppConfig.HOST();
        } catch (Exception e) {
            MiniLogger.get().e(e);
        }
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                String token = o.toString();
                MiniSharedPreferences.instance().setString("token", token);
                MiniLogger.get().d("token is :" + token);
                new CEApi().uploadToken();
            }
            @Override
            public void onFail(Object o, int i, String s) {
                MiniLogger.get().e("token error: %s", o.toString());
            }
        });
    }

    public void onTerminate() {
        super.onTerminate();
        CESystem.destroy();
        system = null;
    }

    public String getApplicationName() {
        String name;
        try {
            name = (String)this.getPackageManager().getApplicationLabel(this.getApplicationInfo());
        } catch (Exception e) {
            name = getResources().getString(R.string.app_name);
        }
        return name;
    }

    public CESystem getSystem() {
        return system;
    }

    public void onEnterForeground() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (system != null) {
                    if (MiniActivityManager.APP_STATUS.APP_STATUS_FOREGROUND.equals(appStatus)) {
                    }
                }
            }
        },2000);
    }

}
