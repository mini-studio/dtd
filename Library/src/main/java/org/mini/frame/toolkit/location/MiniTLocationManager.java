package org.mini.frame.toolkit.location;

import android.content.Context;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.mini.frame.application.MiniApplication;
import org.mini.frame.log.MiniLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 16/1/4.
 */
public class MiniTLocationManager implements TencentLocationListener {

    private Context context;

    private Double latitude;
    private Double longitude;
    private TencentLocationRequest request;
    private MiniTLocationListener listener;
    private TencentLocationManager locationManager;

    public interface MiniTLocationListener {
        void onLocation(MiniTLocationManager locationManager, final MiniTLocationListener listener, Double longitude, Double latitude, int error, String reason);
    }

    private MiniTLocationManager(MiniTLocationListener listener) {
        this.context = MiniApplication.sharedAppplication;
        this.listener = listener;
    }

    public static MiniTLocationManager newInstance(MiniTLocationListener listener) {
        return new MiniTLocationManager(listener);
    }

    public boolean hasError(int error) {
        return error != TencentLocation.ERROR_OK;
    }

    public String errorReason(int code) {
        String reason = null;
        switch (code) {
            case  TencentLocation.ERROR_OK:
                reason = "OK";
            case  TencentLocation.ERROR_NETWORK:
                reason = "网络错误";
            case  TencentLocation.ERROR_BAD_JSON:
                reason = "位置数据错误";
            case  TencentLocation.ERROR_WGS84:
                reason = "网络错误";
            case  TencentLocation.ERROR_UNKNOWN:
                reason = "网络错误";
            case -1:
                reason = "启动位置服务器错误";
            default:
                reason = "网络错误";
        }
        return reason;
    }

    public void start() {
        request = TencentLocationRequest.create();
        //request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        request.setInterval(5000);
        locationManager = TencentLocationManager.getInstance(context);
        int error = locationManager.requestLocationUpdates(request, this);
        if (error == 0) {
            MiniLogger.get().d("位置服务启动成功");
        }
        else {
            MiniLogger.get().d("位置服务启动失败[" + error + "]");
            if (listener != null) {
                listener.onLocation(this, listener, longitude, latitude, -1, null);
            }
            stop();
        }
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        if (TencentLocation.ERROR_OK == error) {
            latitude = tencentLocation.getLatitude();
            longitude = tencentLocation.getLongitude();
        }
        if (listener != null) {
            listener.onLocation(this, listener, longitude, latitude, error, reason);
        }
        stop();
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        MiniLogger.get().d(name + "," + status + "," + desc);
    }
}
