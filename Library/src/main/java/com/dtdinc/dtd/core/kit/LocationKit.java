package com.dtdinc.dtd.core.kit;

import com.dtdinc.dtd.core.api.data.City;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.exception.CEDataException;

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
            public void onLocation(final MiniTLocationManager manager, final MiniTLocationManager.MiniTLocationListener listener, final Double longitude, final Double latitude, int error, String reason) {
                if (manager.hasError(error)) {
                    locationListener.locateCity(null, manager.errorReason(error));
                }
                else {
                    new CEApi().currentCity(longitude, latitude, new MiniDataListener<City>() {
                        @Override
                        public void onResponse(City data, CEDataException error) {
                            if (data != null) {
                                data.setLatitude(latitude);
                                data.setLongitude(longitude);
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
