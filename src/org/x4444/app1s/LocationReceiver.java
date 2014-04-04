
package org.x4444.app1s;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent calledIntent) {

        try {
            double lat = calledIntent.getDoubleExtra("latitude", -1);
            double lon = calledIntent.getDoubleExtra("longitude", -1);

            Log.i("gps", "Location RECEIVED, latlon: " + lat + "," + lon);

            updateRemote(lat, lon);
        } catch (RuntimeException e) {
            Log.e("gps", e.getMessage(), e);
        }
    }

    private void updateRemote(final double latitude, final double longitude) {
        // HERE YOU CAN PUT YOUR ASYNCTASK TO UPDATE THE LOCATION ON YOUR SERVER
    }
}
