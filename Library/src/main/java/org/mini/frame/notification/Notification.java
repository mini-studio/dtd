package org.mini.frame.notification;

import com.dtdinc.dtd.core.define.CEConst;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class Notification {
    private Map<String,Object> info;
    private Object source;
    private String key;

    public Notification() {

    }

    public Notification(String key) {
        this(key, null, null);
    }

    public Notification(String key, Map<String,Object> info) {
        this(key, null, info);
    }

    public Notification(String key, Object source, Map<String,Object> info) {
        this.key = key;
        this.source = source;
        this.info = info;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setInfo(String key, Object v) {
        if (info == null) {
            info = new HashMap<String, Object>();
        }
        info.put(key,v);
    }

    public Object getInfoObject(String key) {
        if (info != null) {
            return info.get(key);
        }
        return null;
    }

    public Object getInfoObject() {
        return getInfoObject(CEConst.CE_NOTIFICATION_BADGE_INFO_KEY);
    }
}
