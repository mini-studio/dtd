package com.mini.app;

import org.mini.frame.application.MiniApplication;

/**
 * Created by Wuquancheng on 15/4/2.
 */
public class CEAppConfig {
    private static String host = null;

    public static String imageServer = "http://api.dtd.la";

    public static String HOST() {
        if (host == null) {
            String mode = CEApplication.instance().getAppMode();
            if ("Debug".equals(mode)) {
                host = "http://api.dtd.la/index.php";
            }
            else if ("Sandbox".equals(mode)) {
                host = "http://api.dtd.la/index.php";
            }
            else if ("Release".equals(mode)) {
                host = "http://api.dtd.la/index.php";
            }
            else {
                host = "http://api.dtd.la/index.php";
            }
            MiniApplication.HOST = host;
        }
        return host;
    }
}
