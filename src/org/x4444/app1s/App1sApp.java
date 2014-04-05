
package org.x4444.app1s;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.x4444.app1s.LocationService.MyLocationListener;
import org.x4444.app1s.db.LocationDao;
import org.x4444.app1s.db.PlateDbHelper;

import android.app.Application;
import android.content.Context;

public class App1sApp extends Application {

    public static Context context;

    public static int gpsCnt;

    public static MyLocationListener myLocationListener;

    public static PlateDbHelper dbHelper;

    public static LocationDao locationDao;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static SimpleDateFormat sdfHhmmss = new SimpleDateFormat("HH:mm:ss", Locale.US);

    /*
     * (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        App1sApp.context = getApplicationContext();

        dbHelper = new PlateDbHelper(context);
        locationDao = new LocationDao();

        sdf.setTimeZone(TimeZone.getDefault());
        sdfHhmmss.setTimeZone(TimeZone.getDefault());
    }

    public static Context getAppContext() {
        return App1sApp.context;
    }
}
