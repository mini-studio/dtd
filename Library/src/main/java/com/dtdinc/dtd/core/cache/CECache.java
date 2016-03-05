package com.dtdinc.dtd.core.cache;

import com.dtdinc.dtd.app.CEApplication;

import org.mini.frame.toolkit.MiniJsonUtil;
import org.mini.frame.toolkit.cache.ACache;

/**
 * Created by Wuquancheng on 15/5/10.
 */
public class CECache {

    public static ACache userCache() {
        String name = "jtce";
        ACache aCache = ACache.get(CEApplication.instance(), name);
        return aCache;
    }

    public static void save(String key, Object object) {
        ACache cache = userCache();
        String value = MiniJsonUtil.toJsonString(object);
        cache.put(key, value);
    }

    public static void save(String key, String value) {
        ACache cache = userCache();
        cache.put(key, value);
    }

    public static String getString(String key) {
        ACache cache = userCache();
        return cache.getAsString(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        ACache cache = userCache();
        String value = cache.getAsString(key);
        if (value != null) {
            return MiniJsonUtil.stringToObject(value, clazz);
        }
        else {
            return null;
        }
    }

    public static void clear() {
        ACache cache = userCache();
        cache.clear();
    }

    public static void remove(String key) {
        ACache cache = userCache();
        cache.remove(key);
    }
}
