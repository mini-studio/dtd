package org.mini.frame.toolkit.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class MiniLocationManager {

    Location location;
    public LocationManager locationManager = null;
    private List<MiniLocationListener> locationListeners = new ArrayList<MiniLocationListener>();
    //单例
    private volatile static MiniLocationManager loc_share = null;
    //单例
    public static MiniLocationManager shard()
    {
        synchronized(MiniLocationManager.class) {
            if(loc_share==null) {
                loc_share = new MiniLocationManager();
            }
        }
        return loc_share;
    }

    //构造函数
    private MiniLocationManager() {

    }

    public Location currentLocation() {
        return location;
    }

    public double longitude() {
        if (location != null) {
            return location.getLongitude();
        }
        else {
            return -1;
        }
    }

    public double latitude() {
        if (location != null) {
            return location.getLatitude();
        }
        else {
            return -1;
        }
    }

    public void addListener(MiniLocationListener listener) {
        this.locationListeners.add(listener);
    }

    public void removeListener(MiniLocationListener listener) {
        this.locationListeners.remove(listener);
    }

    private LocationListener gpsListener =  new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            MiniLocationManager.this.location = location;
            for (MiniLocationListener listener : locationListeners) {
                listener.chLocationManagerDidGetLatAndLon();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }

    };

    //判断是否获得数据
    public boolean isGetLatAndLon()
    {
        if (this.location == null) {
            return false;
        }
        return true;
    }

    //开始
    public void start(Activity context)
    {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            this.stop();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10, gpsListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //删除侦听
    public void stop()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(gpsListener);
        }
    }
}
