package org.mini.frame.http.request.data;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class MiniDataWrapper implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer code;
    private String desc;

    private Object info;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public final Object getInfo() {
        return info;
    }

    public <T> T getInfo(Class<T> clazz) {
        if (info != null) {
            if (info.getClass().getName().equals(clazz.getName())) {
                return (T) info;
            }
            else {
                Gson gson = new Gson();
                String string = gson.toJson(info);
                info = gson.fromJson(string,clazz);
                return (T)info;
            }
        }
        return null;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Object getInfoValue(String key) {
        if (info != null && info instanceof Map) {
            return ((Map)info).get(key);
        }
        else {
            return null;
        }
    }

    public boolean hasError() {
        return code != 200 && code != 400 && code != 252;
    }
}
