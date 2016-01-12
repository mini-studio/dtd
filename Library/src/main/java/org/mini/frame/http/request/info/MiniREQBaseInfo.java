package org.mini.frame.http.request.info;

import android.app.DownloadManager;

import com.android.volley.Request;
import com.mini.app.CEAppConfig;
import com.mini.app.CESystem;

import org.mini.frame.application.MiniApplication;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/4/2.
 */
public abstract class MiniREQBaseInfo {

    private static final String HOST = MiniApplication.HOST;

    public static int POST = Request.Method.POST;
    public static int GET = Request.Method.GET;

    private int method;
    protected String sid;

    public MiniREQBaseInfo() {
        this(GET);
    }

    public MiniREQBaseInfo(int method) {
        this.method = method;
        this.sid = CESystem.instance().currentSid();
    }

    public int getMethod() {
        return method;
    }

    public String getSid() {
        return sid;
    }

    public String getUrl() {
        StringBuilder address = new StringBuilder();
        String path = getPath();
        if (path.startsWith("http://") || path.startsWith("https://")) {
            address.append(path);
        }
        else {
            address.append(HOST).append(path);
        }
        Map<String,String> queryParam = getQueryParamsMap();
        if (queryParam != null && queryParam.size() > 0) {
            StringBuilder param = new StringBuilder();
            for(String key : queryParam.keySet()) {
                String v = queryParam.get(key);
                param.append(key).append("=");
                if (v == null || v.length() == 0) {
                    param.append("&");
                }
                else {
                    try {
                        param.append(URLEncoder.encode(v, "utf-8")).append("&");
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (param.length()>0) {
                param.setLength(param.length()-1);
            }
            if (address.indexOf("?") == -1) {
                address.append("?");
            }
            if (address.charAt(address.length()-1) != '&') {
                address.append("&");
            }
            address.append(param.toString());
        }
        return address.toString();
    }

    public abstract String getPath();

    public abstract Map<String, String> getFormParamsMap();

    public abstract Map<String, String> getQueryParamsMap();

    public Map<String, String> getRequestHeaders() {
        return null;
    }

    public byte[] getPostBody() {
        return null;
    }

    public String cacheKey() {
        return null;
    }
}



