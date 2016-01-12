package org.mini.frame.toolkit;

import com.google.gson.Gson;

/**
 * Created by Wuquancheng on 15/4/3.
 */
public class MiniJsonUtil {

    public static String toJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T stringToObject(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString,clazz);
    }
}
