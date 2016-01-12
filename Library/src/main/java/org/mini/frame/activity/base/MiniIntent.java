package org.mini.frame.activity.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/6/9.
 */
public class MiniIntent extends Intent {

    public static Map<String,Object> EXCHANGER = new HashMap<String,Object>();
    public static String PARAM = "mini.params";

    public MiniIntent() {
        super();
    }

    public MiniIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public void setObject(Object object) {
        String key = String.valueOf(System.nanoTime());
        EXCHANGER.put(key,object);
        this.putExtra(PARAM, key);
    }

    public static Object getObjectFromIntent(Intent intent) {
        String key = intent.getStringExtra(PARAM);
        if (key != null) {
            Object object = EXCHANGER.get(key);
            if (object != null) {
                EXCHANGER.remove(key);
            }
            return object;
        }
        else {
            return null;
        }
    }

}
