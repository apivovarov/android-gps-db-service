
package org.x4444.app1s;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {

    Notification.Builder notifBuilder;

    static final int NOTIF_ID = 1;

    long adjFreq;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int freq = intent.getIntExtra(MainActivity.GPS_FREQ_NAME, 3000);

        int realFreq;
        if (freq <= 10000) {
            // if freq is high - track gsp constantly (every 1 sec)
            realFreq = 1000;
            adjFreq = freq - 200;
        } else {
            // if freq is low then give gps 7 sec to fix gps after long sleep
            realFreq = freq - 7000;
            adjFreq = (int)(realFreq * 0.95);
        }
        Log.i("gps", "LocationService onStartCommand, freq: " + freq + " adjFreq: " + adjFreq
                + " realFreq: " + realFreq);

        if (App1sApp.myLocationListener != null) {
            removeLocationListener();
            try {
                // sleep needed to switch from 3 min to 5 sec gps listener
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        addLocationListener(realFreq);

        notifBuilder.setContentTitle("Location freq: " + freq);
        getNotifMngr().notify(NOTIF_ID, notifBuilder.getNotification());

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i("gps", "LocationService onCreate: " + this);
        super.onCreate();

        notifBuilder = new Notification.Builder(App1sApp.context)
                .setContentTitle("Location freq: -").setContentText("")
                .setSmallIcon(R.drawable.pin_map_gps);

        startForeground(NOTIF_ID, notifBuilder.getNotification());
        Log.i("gps", "started service in foreground");
    }

    @Override
    public void onDestroy() {
        removeLocationListener();
        Log.i("gps", "LocationService onDestroy: " + this);
        super.onDestroy();
    }

    private void addLocationListener(final int freq) {
        try {
            LocationManager lm = getLocationManager();
            App1sApp.myLocationListener = new MyLocationListener();
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, freq, 0f,
                    App1sApp.myLocationListener);
            Log.i("gps", "LocationListener added, freq: " + freq);
        } catch (RuntimeException e) {
            Log.e("gps", e.getMessage(), e);
        }
    }

    protected void removeLocationListener() {
        try {
            if (App1sApp.myLocationListener != null) {
                getLocationManager().removeUpdates(App1sApp.myLocationListener);
                App1sApp.myLocationListener = null;
                Log.i("gps", "LocationListener removed");
            }
        } catch (RuntimeException e) {
            Log.e("gps", e.getMessage(), e);
        }
    }

    class MyLocationListener implements LocationListener {

        long lastUpdate;

        @Override
        public void onLocationChanged(Location location) {
            try {
                if (location != null) {
                    long now = location.getTime();
                    long diff = now - lastUpdate;
                    Log.i("gps", "onLocationChanged. gps time: " + location.getTime() + " diff: "
                            + diff);

                    if (diff >= adjFreq) {
                        lastUpdate = now;
                        // save to DB
                        App1sApp.locationDao.saveLocation(location);

                        App1sApp.gpsCnt++;

                        String datetime = App1sApp.sdfHhmmss.format(new Date(location.getTime()));
                        notifBuilder.setContentText(location.getLatitude() + ","
                                + location.getLongitude() + " " + datetime);
                        getNotifMngr().notify(NOTIF_ID, notifBuilder.getNotification());
                        Log.i("gps", "location notify done");
                    }
                }
            } catch (Exception e) {
                Log.e("gps", e.getMessage(), e);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("gps", "gps disabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("gps", "gps enabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("gps", "gps status: " + status);
        }
    }

    protected LocationManager getLocationManager() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return lm;
    }

    protected NotificationManager getNotifMngr() {
        NotificationManager notifMngr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return notifMngr;
    }
}
