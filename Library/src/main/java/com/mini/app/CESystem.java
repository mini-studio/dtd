package com.mini.app;

import com.mini.core.api.data.City;
import com.mini.core.cache.CECache;
import com.mini.core.api.data.User;
import com.mini.core.define.CEConst;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.mini.core.manager.HeartbeatManager;

import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.toolkit.MiniFramework;
import org.mini.frame.toolkit.MiniSharedPreferences;
import org.mini.frame.toolkit.file.MiniFileDownloadManager;

import java.io.Serializable;

/**
 * Created by Wuquancheng on 15/4/6.
 */
public class CESystem implements Serializable {

    private static CESystem instance = null;
    public static int appType = 1;
    public static String channel = "";
    public static String appId = "";
    private CEApplication application = null;

    private String remoteMessage = null;

    private City currentCity;

    private Double longitude, latitude;

    private static User user;

    private CESystem(CEApplication application, String appTypeConfig) {
        if (appTypeConfig != null) {
            appType = Integer.parseInt(appTypeConfig);
        }
        MiniSharedPreferences.instance(application);
        this.application = application;
    }

    public void setRemoteMessage(String remoteMessage) {
        this.remoteMessage = remoteMessage;
    }

    public String getRemoteMessage() {
        String message = remoteMessage;
        this.remoteMessage = null;
        return message;
    }

    public static CESystem sharedInstance() {
        return instance;
    }

    private void init() {
        MiniFramework.appShortName = "mini-express";
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(application));
        MiniFileDownloadManager.destroy();
        MiniFileDownloadManager.instance(application);
        NotificationCenter.defaultNotificationCenter();
    }

    static CESystem instance(CEApplication application, String appTypeConfig) {
        synchronized (CESystem.class) {
            if (instance == null) {
                instance = new CESystem(application, appTypeConfig);
                instance.init();
            }
        }
        return instance;
    }



    private void deinit() {
        this.application = null;
        HeartbeatManager.instance().halt();
    }

    static void destroy() {
        if (instance != null) {
            instance.deinit();
            instance = null;
        }
    }

    public static String getChannel() {
        if (channel == null) {
            channel = "";
        }
        return channel;
    }

    public static String getAppId() {
        if (appId == null) {
            appId = "";
        }
        return appId;
    }

    public static CESystem instance() {
        return instance;
    }

    public static String fileServer() {
        return "";
    }

    /**
     * 获取当前用户登录后的SID
     * @return
     */
    public static String currentSid() {
        return "";
    }

    /**
     * 获取当前登录的用户
     * @return
     */
    public static User WHO() {
        if (user == null) {
            user = CECache.get(CEConst.KEY_USER, User.class);
        }
        return user;
    }

    public static void setCurrentUser(User u) {
        if (u == null) {
            CECache.remove(CEConst.KEY_USER);
            user = null;
        }
        else {
            CECache.save(CEConst.KEY_USER, u);
        }
    }

    public City getCurrentCity() {
        if (currentCity == null) {
            currentCity = new City();
            currentCity.setName("定位");
            currentCity.setCode("-1");
        }
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
