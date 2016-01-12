package org.mini.frame.toolkit.location;

import android.content.Context;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

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
    private boolean started = false;

    public interface MiniTLocationListener {
        void onLocation(final MiniTLocationListener listener, Double longitude, Double latitude);
    }

    private List<MiniTLocationListener> listenerList = new ArrayList<MiniTLocationListener>();

    private static MiniTLocationManager locationManager;

    public static MiniTLocationManager instanceOf(Context context) {
        synchronized (MiniTLocationManager.class) {
            if (locationManager == null) {
                locationManager = new MiniTLocationManager(context);
            }
        }
        return locationManager;
    }

    public static MiniTLocationManager instanceOf() {
        return locationManager;
    }

    private MiniTLocationManager(Context context) {
        this.context = context;
    }

    public void addListener(MiniTLocationListener listener) {
        listenerList.add(listener);
        if (latitude != null && longitude != null) {
            listener.onLocation(listener, longitude, latitude);
        }
    }

    public void removeListener(MiniTLocationListener listener) {
        listenerList.remove(listener);
    }

    public void start() {
        if (started) {
            return;
        }
        TencentLocationRequest request = TencentLocationRequest.create();
        //request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        request.setInterval(600000);
        TencentLocationManager locationManager = TencentLocationManager.getInstance(context);
        int error = locationManager.requestLocationUpdates(request, this);
        if (error == 0) {
            MiniLogger.get().d("位置服务启动成功");
            started = true;
        }
        else {
            MiniLogger.get().d("位置服务启动失败["+error+"]");
            started = false;
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            latitude = tencentLocation.getLatitude();
            longitude = tencentLocation.getLongitude();
            if (latitude > 0 && longitude > 0) {
                for (MiniTLocationListener listener : listenerList) {
                    listener.onLocation(listener, longitude, latitude);
                }
            }
        } else {

        }
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        MiniLogger.get().d(name + "," + status + "," + desc);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
