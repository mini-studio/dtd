package org.mini.frame.http.request;

import android.content.Context;
import android.graphics.Point;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.dtdinc.dtd.app.CEApplication;
import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.cache.CECache;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.http.request.data.MiniDataWrapper;
import org.mini.frame.http.request.info.MiniREQBaseInfo;
import org.mini.frame.log.MiniLogger;

import org.mini.frame.toolkit.MiniDeviceUtils;
import org.mini.frame.toolkit.MiniPackageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by Wuquancheng on 15/4/2.
 */
public class MiniRequest<T> extends StringRequest {

    private MiniREQBaseInfo requestInfo;

    public MiniRequest(final Class<T> clazz, final MiniDataListener<T> listener, final MiniREQBaseInfo info) {
        super(info.getMethod(), info.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MiniLogger.get().d(info.getPath());
                MiniLogger.get().d(response);
                MiniRequest.onResponse(response, info, false, listener, clazz);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MiniLogger.get().e(error, "");
                if (listener != null) {
                    if (error instanceof com.android.volley.TimeoutError) {
                        listener.onResponse(null, new CEDataException("网络请求超时", error));
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        listener.onResponse(null, new CEDataException("没有发现可用的网络，请您检查您的网络", error));
                    } else if (error instanceof com.android.volley.NetworkError) {
                        listener.onResponse(null, new CEDataException("连接网络失败，请您检查您的网络", error));
                    } else if (error instanceof com.android.volley.ServerError) {
                        listener.onResponse(null, new CEDataException("服务器发生了错误，请联系管理员", error));
                    } else {
                        listener.onResponse(null, new CEDataException(error.getMessage(), error));
                    }
                }
            }
        });
        this.requestInfo = info;
        setRetryPolicy(new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        final String cacheKey = info.cacheKey();
        if (cacheKey != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String response = CECache.getString(cacheKey);
                        if (response != null) {
                            CEApplication.instance().globalHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    MiniRequest.onResponse(response, info, true, listener, clazz);
                                }
                            });
                        }
                    } catch (Exception e) {
                        MiniLogger.get().e(e);
                    }
                }
            }).start();
        }
    }

    private static <T> void onResponse(String response, final MiniREQBaseInfo info, boolean fromCache, final MiniDataListener<T> listener, Class<T> clazz) {
        try {
            T result;
            if (clazz.equals(String.class)) {
                result = (T)response;
                if (listener != null) {
                    listener.onResponse(result, null);
                }
                return;
            }
            else {
                Gson gson = new Gson();
                result = gson.fromJson(response, clazz);
            }
            if (result instanceof MiniDataWrapper) {
                MiniDataWrapper cheduResultBase = (MiniDataWrapper) result;
                if (cheduResultBase.hasError()) {
                    String desc = cheduResultBase.getDesc();
                    if (listener != null) {
                        listener.onResponse(result, new CEDataException(cheduResultBase.getCode(), desc, new Exception(desc)));
                    }
                    return;
                } else {
                    if (!fromCache) {
                        String cacheKey = info.cacheKey();
                        if (cacheKey != null) {
                            CECache.save(cacheKey, response);
                        }
                    }
                    if (listener != null) {
                        listener.onResponse(result, null);
                    }
                }
            }
            else {
                if (listener != null) {
                    listener.onResponse(null, new CEDataException(CEDataException.ERROR_DATA_FORMAT, "类型错误"));
                }
            }
        } catch (Exception e) {
            MiniLogger.get().e(e, e.getMessage());
            if (listener != null) {
                listener.onResponse(null, new CEDataException(e.getMessage(), e));
            }
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = getStandardHeaders(this.requestInfo.getSid() != null ? this.requestInfo.getSid() : "0");
        Map<String, String> requestHeaders = this.requestInfo.getRequestHeaders();
        if (requestHeaders != null && requestHeaders.size() > 0) {
            for (String key : requestHeaders.keySet()) {
                headers.put(key, requestHeaders.get(key));
            }
        }
        return headers;
    }

    public static Map<String, String> getStandardHeaders(String sid) {
        Context context = CEApplication.instance();
        Map<String, String> headers = new HashMap<String, String>();
        if (sid == null) {
            headers.put("sid", "");
        } else {
            headers.put("sid", sid);
        }
        headers.put("Accept-Encoding", "gzip");
        headers.put("imei", MiniDeviceUtils.getImei(context));
        headers.put("version", String.valueOf(MiniPackageUtils.getAppVersion(context)));
        headers.put("region", MiniDeviceUtils.getLocalization(context));
        headers.put("sys_version", MiniDeviceUtils.getOsVersion());
        headers.put("platform", "3");
        headers.put("device", MiniDeviceUtils.getModel());
        headers.put("language", MiniDeviceUtils.getLanguage(context));
        headers.put("app_type", String.valueOf(CESystem.appType));
        headers.put("app_id", CESystem.getAppId());
        String channel = CESystem.getChannel();
        if (channel != null || channel.length() >= 0) {
            headers.put("app-channel", channel);
        }
        Point screen = MiniDeviceUtils.getScreenSize(context);
        headers.put("screen", screen.x + "X" + screen.y);
        return headers;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return this.requestInfo.getFormParamsMap();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response.headers != null) {
            //"Content-Encoding" -> "gzip"
            String encoding = response.headers.get("Content-Encoding");
            if (encoding == null || encoding.indexOf("gzip") == -1) {
                return super.parseNetworkResponse(response);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        GZIPInputStream gStream = null;
        InputStreamReader reader = null;
        BufferedReader in = null;
        try {
            gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
            reader = new InputStreamReader(gStream);
            in = new BufferedReader(reader);
            String read;
            while ((read = in.readLine()) != null) {
                stringBuilder.append(read);
            }
        } catch (IOException e) {
            return Response.error(new ParseError());
        }
        finally {
            if (reader != null) {
                try { reader.close();}
                catch (Exception e) {}
            }
            if (in != null) {
                try { in.close();}
                catch (Exception e) {}
            }
            if (gStream != null) {
                try { gStream.close();}
                catch (Exception e) {}
            }
        }
        return Response.success(stringBuilder.toString(), HttpHeaderParser.parseCacheHeaders(response));
    }
}
