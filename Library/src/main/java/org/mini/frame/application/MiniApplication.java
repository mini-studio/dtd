package org.mini.frame.application;

import android.app.Application;

import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.toolkit.manager.MiniAppManager;

/**
 * Created by Wuquancheng on 15/5/16.
 */
public class MiniApplication extends Application {

    public static MiniApplication sharedAppplication;

    protected MiniActivityManager.APP_STATUS appStatus;

    private String mode = null;

    private String channel = null;

    private String appId = null;

    public static String HOST = "http://api.dtd.la/index.php";

    public void onCreate() {
        super.onCreate();
        sharedAppplication = this;
    }

    public void onTerminate() {
        sharedAppplication = null;
        super.onTerminate();
    }

    public void onEnterForeground() {

    }

    public void onEnterBackground() {

    }

    public String getBuild() {
        String build = "";
        try {
            build = MiniAppManager.getAppMetaData("app_build");
            if (build != null && build.length() > 0) {
                build = "(" + build.replace("app_build:","Build:") + ")";
            }
            else {
                build = "";
            }
        } catch (Exception e) {
        }
        return build;
    }

    public String getAppMode() {
        if (mode == null) {
            try {
                mode = MiniAppManager.getAppMetaData("app_mode");
                if (mode != null && mode.length() > 0) {
                    mode = mode.replace("app_mode:", "");
                }
            } catch (Exception e) {
            }
        }
        return mode;
    }

    public String getAppId() {
        if (appId == null) {
            try {
                appId = MiniAppManager.getAppMetaData("appId");
                if (appId != null && appId.length() > 0) {
                    appId = appId.replace("appId:", "");
                }else{
                    appId="20141001";
                }
            } catch (Exception e) {
            }
        }
        return appId;
    }


    public String getAppChannel() {
        if (channel == null) {
            try {
                channel = MiniAppManager.getAppMetaData("app_channel");
                if (channel != null && channel.length() > 0) {
                    channel = channel.replace("app_channel:", "");
                }
            } catch (Exception e) {
            }
        }
        return channel;
    }

    public void onResume() {
        if (appStatus == null || !MiniActivityManager.APP_STATUS.APP_STATUS_FOREGROUND.equals(appStatus)) {
            onEnterForeground();
        }
        appStatus = MiniActivityManager.appStatus(this.getApplicationContext());
    }

    public void onStop() {
        appStatus = MiniActivityManager.appStatus(this.getApplicationContext());
        if (MiniActivityManager.APP_STATUS.APP_STATUS_BACKGROUND.equals(appStatus)) {
            onEnterBackground();
        }
    }
}
