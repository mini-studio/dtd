package com.mini.core.kit;

import com.mini.core.api.data.City;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.location.MiniTLocationManager;

/**
 * Created by Wuquancheng on 16/1/5.
 */
public class LocationKit {

    public interface LocationListener {
        void locateCity(City city, String reason);
    }

    public void locate(final LocationListener locationListener) {
        MiniTLocationManager.MiniTLocationListener listener = new MiniTLocationManager.MiniTLocationListener() {
            @Override
            public void onLocation(final MiniTLocationManager manager, final MiniTLocationManager.MiniTLocationListener listener, Double longitude, Double latitude, int error, String reason) {
                if (manager.hasError(error)) {
                    locationListener.locateCity(null, manager.errorReason(error));
                }
                else {
                    new CEApi().currentCity(longitude, latitude, new MiniDataListener<City>() {
                        @Override
                        public void onResponse(City data, CEDataException error) {
                            if (data != null) {
                                locationListener.locateCity(data, null);
                            }
                            manager.stop();
                        }
                    });
                }
            }
        };
        MiniTLocationManager.newInstance(listener).start();
    }
}
