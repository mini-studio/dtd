package com.mini.core.manager;

import com.mini.core.api.engine.CEApi;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class HeartbeatManager {

    private static HeartbeatManager heartbeatManager = new HeartbeatManager();

    private HeartbeatThread heartbeatThread;



    private HeartbeatManager() {
    }

    public static HeartbeatManager instance() {
        return heartbeatManager;
    }

    public void start() {
        stopHeartbeatThread();
        heartbeatThread = new HeartbeatThread();
        heartbeatThread.start();
    }

    public void halt() {
        stopHeartbeatThread();
    }

    public void stopHeartbeatThread() {
        if (heartbeatThread != null) {
            heartbeatThread.halt();
        }
    }

    class HeartbeatThread extends Thread{

        private boolean stop = false;

        private CEApi api = new CEApi();

        public void halt() {
            stop = true;
        }

        public void run() {
            while (!stop) {
                heartbeat();
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    ;
                }
            }
        }

        private void heartbeat() {

        }

    }
}
