package org.mini.frame.toolkit;

import android.content.Context;
import android.net.ConnectivityManager;

import static android.net.NetworkInfo.State;

/**
 * by gassion 网络连接的工具类
 */
public class MiniNetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORK_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }
}
