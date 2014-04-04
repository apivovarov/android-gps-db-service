
package org.x4444.app1s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "app1s_prefs";

    public static final String GPS_FREQ_NAME = "gps_freq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        refreshCounters();
    }

    public void buttonListenGps5(View view) {
        startLocationService(3000);
    }

    public void buttonListenGps180(View view) {
        startLocationService(40000);
    }

    public void button6Click(View view) {
        Log.i("gps", "stop service click");
        stopService1();
    }

    public void buttonRefreshClick(View view) {
        refreshCounters();
    }

    private void startLocationService(int gpsFreq) {
        Log.i("gps", "MainActivity th: " + Thread.currentThread());
        Intent locationServIntent = new Intent(this, LocationService.class);
        locationServIntent.putExtra(GPS_FREQ_NAME, gpsFreq);
        startService(locationServIntent);
    }

    protected void stopService1() {
        Intent mServiceIntent = new Intent(this, LocationService.class);
        stopService(mServiceIntent);
    }

    protected void refreshCounters() {
        try {
            TextView textGpsCnt = (TextView)findViewById(R.id.textGpsCnt);
            textGpsCnt.setText(String.valueOf(App1sApp.gpsCnt));

            TextView textDbLocCnt = (TextView)findViewById(R.id.textDbLocCnt);
            int dbLocCnt = App1sApp.locationDao.getCount();
            textDbLocCnt.setText(String.valueOf(dbLocCnt));
        } catch (RuntimeException e) {
            Log.e("gps", e.getMessage());
        }
    }
}
